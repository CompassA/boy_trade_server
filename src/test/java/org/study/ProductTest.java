package org.study;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.study.dao.ProductDao;
import org.study.data.ProductDO;

import java.math.BigDecimal;

/**
 * @author fanqie
 * @date 2020/1/11
 */
public class ProductTest extends BaseTest {

    @Autowired
    private ProductDao productDao;

    @Test
    @Transactional
    @Rollback
    public void upsertTest() {
        final ProductDO product = new ProductDO()
                .setId(1)
                .setCategoryId(1)
                .setName("123")
                .setDescription("2132")
                .setStatus((byte) 0)
                .setIconUrl("adwadad")
                .setPrice(BigDecimal.valueOf(12.50));
        int i = productDao.upsertProduct(product);
        Assert.assertEquals(i, 1);
        product.setCategoryId(2);
        int j = productDao.upsertProduct(product);
        Assert.assertEquals(j, 1);
    }
}
