package org.study;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.study.dao.ProductMapper;
import org.study.dao.ProductSaleMapper;
import org.study.dao.ProductStockMapper;
import org.study.data.ProductDO;
import org.study.data.ProductSaleDO;
import org.study.data.ProductStockDO;
import org.study.test.config.SpringMockTestBase;

import java.math.BigDecimal;

/**
 * @author fanqie
 * Created on 2020/1/11
 */
public class ProductTest extends SpringMockTestBase {

    private final ProductMapper productMapper;
    private final ProductStockMapper productStockMapper;
    private final ProductSaleMapper productSaleMapper;

    public ProductTest() {
        this.productMapper = this.context.getBean(ProductMapper.class);
        this.productStockMapper = this.context.getBean(ProductStockMapper.class);
        this.productSaleMapper = this.context.getBean(ProductSaleMapper.class);
    }

    @Test
    @Transactional
    @Rollback
    public void upsertTest() {
        //插入商品
        final ProductDO product = new ProductDO()
                .setCategoryId(1)
                .setName("123")
                .setDescription("2132")
                .setStatus((byte) 0)
                .setIconUrl("adwadad")
                .setPrice(BigDecimal.valueOf(12.50));
        Assert.assertEquals(productMapper.upsertProduct(product), 1);

        //插入销量
        final ProductSaleDO productSaleDO = new ProductSaleDO()
                .setProductId(product.getId());
        Assert.assertEquals(1, productSaleMapper.initProductSale(productSaleDO));

        //插入库存
        final ProductStockDO productStockDO = new ProductStockDO()
                .setProductId(product.getId())
                .setStock(25);
        Assert.assertEquals(1, productStockMapper.initProductStock(productStockDO));
    }
}
