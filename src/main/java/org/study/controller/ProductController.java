package org.study.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.study.error.ServerException;
import org.study.error.ServerExceptionBean;
import org.study.model.ProductModel;
import org.study.model.UserModel;
import org.study.response.ServerResponse;
import org.study.service.ProductService;
import org.study.service.SessionService;
import org.study.util.ModelToViewUtil;
import org.study.view.ProductVO;

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

    @PutMapping(value = ApiPath.Product.CREATE)
    public ServerResponse createProduct(
            @RequestBody final ProductVO product) throws ServerException {
        //登录态方可创建
        final Optional<UserModel> userModel = sessionService.getUserModel();
        if (!sessionService.isLogin() || !userModel.isPresent()) {
            throw new ServerException(ServerExceptionBean.PRODUCT_CREATE_EXCEPTION);
        }
        final Integer userId = userModel.get().getUserId();
        if (!userId.equals(product.getUserId())) {
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
        return ServerResponse.create(productVO.get());
    }

    @GetMapping(value = ApiPath.Product.INFO)
    public ServerResponse getAllProduct() throws ServerException {
        return ServerResponse.create(ModelToViewUtil.getProductViews(productService.getAllProduct()));
    }
}
