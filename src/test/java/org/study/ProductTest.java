package org.study;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.study.dao.ProductMapper;
import org.study.dao.ProductSaleMapper;
import org.study.dao.ProductStockMapper;
import org.study.data.ProductDO;
import org.study.data.ProductSaleDO;
import org.study.data.ProductStockDO;

import java.math.BigDecimal;

/**
 * @author fanqie
 * @date 2020/1/11
 */
public class ProductTest extends BaseTest {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductStockMapper productStockMapper;

    @Autowired
    private ProductSaleMapper productSaleMapper;

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
