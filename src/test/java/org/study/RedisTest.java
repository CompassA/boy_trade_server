package org.study;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.study.view.CartDTO;

/**
 * @author fanqie
 * Created on 2020/2/9
 */
public class RedisTest extends BaseTest {

    @Resource
    private RedisTemplate<Object, Object> redisTemplate;

    @Test
    public void hashOpsTest() {
        final CartDTO cartDTO = new CartDTO().setUserId(1).setProductId(2).setNum(1);
        final String userId = cartDTO.getUserId().toString();
        final String productId = cartDTO.getProductId().toString();
        redisTemplate.opsForHash().put(userId, productId, "1");
        Assert.assertNotEquals(1, redisTemplate.opsForHash().get(userId, productId));
    }
}
