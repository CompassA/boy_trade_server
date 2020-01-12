package org.study.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.study.error.ServerException;
import org.study.error.ServerExceptionBean;
import org.study.model.ProductModel;
import org.study.response.ServerResponse;
import org.study.service.ProductService;
import org.study.util.ModelToViewUtil;
import org.study.view.ProductVO;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * @author fanqie
 * @date 2020/1/12
 */
@RestController
@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
public class ProductController extends BaseController {

    @Autowired
    private ProductService productService;

    @PutMapping(value = ApiPath.Product.CREATE)
    public ServerResponse createProduct(
            @RequestParam("categoryId") final Integer categoryId,
            @RequestParam("stock") final Integer stock,
            @RequestParam("price") final BigDecimal price,
            @RequestParam("productName") final String productName,
            @RequestParam("description") final String description)
            throws ServerException {
        final ProductModel productModel = new ProductModel()
                .setCategoryId(categoryId)
                .setStock(stock)
                .setPrice(price)
                .setProductName(productName)
                .setDescription(description);
        ProductModel modelStatus = productService.create(productModel);
        final Optional<ProductVO> productVO = ModelToViewUtil.getProductVO(modelStatus);
        if (!productVO.isPresent()) {
            throw new ServerException(ServerExceptionBean.PRODUCT_CREATE_EXCEPTION);
        }
        return ServerResponse.create(productVO.get());
    }
}
