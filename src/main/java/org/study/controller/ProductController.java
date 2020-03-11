package org.study.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.study.cache.LocalCacheBean;
import org.study.controller.response.ServerResponse;
import org.study.error.ServerException;
import org.study.error.ServerExceptionBean;
import org.study.service.ProductService;
import org.study.service.RedisService;
import org.study.service.SessionService;
import org.study.service.model.ProductModel;
import org.study.service.model.enumdata.CacheType;
import org.study.util.ModelToViewUtil;
import org.study.util.MyStringUtil;
import org.study.view.PageVO;
import org.study.view.ProductVO;

import java.util.List;
import java.util.Optional;

/**
 * @author fanqie
 * @date 2020/1/12
 */
@RestController
@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private LocalCacheBean cache;

    @PutMapping(value = ApiPath.Product.CREATE)
    public ServerResponse createProduct(
            @RequestParam("userId") final Integer userId,
            @RequestParam("token") final String token,
            @RequestBody final ProductVO product) throws ServerException {
        //登录态方可创建
        if (!sessionService.isLogin(token, userId)) {
            throw new ServerException(ServerExceptionBean.USER_NOT_LOGIN_EXCEPTION);
        }

        final ProductModel productModel = new ProductModel()
                .setCategoryId(product.getCategoryId())
                .setStock(product.getStock())
                .setPrice(product.getPrice())
                .setProductName(product.getProductName())
                .setDescription(product.getDescription())
                .setIconUrl(product.getIconUrl())
                .setUserId(userId);
        final ProductModel modelStatus = productService.create(productModel);
        final Optional<ProductVO> productVO = ModelToViewUtil.getProductVO(modelStatus);
        if (!productVO.isPresent()) {
            throw new ServerException(ServerExceptionBean.PRODUCT_CREATE_EXCEPTION);
        }
        cache.getMainPageCache().invalidate();
        return ServerResponse.create(productVO.get());
    }

    @GetMapping(value = ApiPath.Product.PAGE)
    public ServerResponse getPageView(
            @RequestParam("prePage") Integer prePage, @RequestParam("targetPage") int targetPage,
            @RequestParam("preLastId") int preLastId) {
        //get from cache
        final PageVO pageVO = cache.getMainPageCache().get(targetPage);
        if (pageVO != null) {
            return ServerResponse.create(pageVO);
        }

        //get from mysql
        final List<ProductVO> views = ModelToViewUtil.getProductViews(
                (preLastId != 0 && prePage < targetPage)
                        ? productService.selectNextPage(preLastId, prePage, targetPage, null)
                        : productService.selectPageNormal(targetPage, null));

        //update cache
        final PageVO page = new PageVO(targetPage, views);
        cache.getMainPageCache().put(targetPage, page);
        return ServerResponse.create(page);
    }

    @GetMapping(value = ApiPath.Product.USER_PRODUCTS)
    public ServerResponse getProductByUserId(
            @RequestParam("userId") final Integer userId,
            @RequestParam("token") final String token) throws ServerException {
        if (!sessionService.isLogin(token, userId)) {
            throw new ServerException(ServerExceptionBean.USER_NOT_LOGIN_EXCEPTION);
        }
        return ServerResponse.create(
                ModelToViewUtil.getProductViews(productService.selectByUserId(userId)));
    }

    @GetMapping(value = ApiPath.Product.DETAIL)
    public ServerResponse getProductInfo(@RequestParam("productId") final Integer productId)
            throws ServerException {
        //取缓存
        final String key = MyStringUtil.getCacheKey(productId, CacheType.PRODUCT);
        final Optional<ProductVO> cache = redisService.getCacheWithoutLocalCache(key);
        if (cache.isPresent()) {
            return ServerResponse.create(cache.get());
        }

        //查询数据库
        final ProductModel productModel = productService.selectByPrimaryKey(productId);
        return ModelToViewUtil.getProductVO(productModel)
                .map(productVO -> {
                    productVO.setPaidNum(productService.getPaidNum(productId));
                    redisService.cacheDataWithoutLocalCache(key, productVO);
                    return ServerResponse.create(productVO);
                }).orElse(ServerResponse.fail(ServerExceptionBean.PRODUCT_NOT_EXIST_EXCEPTION));
    }
}
