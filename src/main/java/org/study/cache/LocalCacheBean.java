package org.study.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.study.service.ProductService;
import org.study.util.ModelToViewUtil;
import org.study.view.HomePageVO;
import org.study.view.PageVO;
import org.study.view.ProductVO;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author fanqie
 * @date 2020/3/12
 */
@Component
public class LocalCacheBean {

    private static final Integer HOME_PAGE_CACHE_KEY = 0;

    @Autowired
    private ProductService productService;

    @Getter
    private MyCache<Integer, PageVO> mainPageCache;

    @Getter
    private MyCache<String, PageVO> categoryPageCache;

    private Cache<Integer, HomePageVO> homeCache;

    @PostConstruct
    public void initCache() {
        mainPageCache = LRUFactory.getCache();
        final List<ProductVO> views = ModelToViewUtil.getProductViews(
                productService.selectFromBegin(10));

        final Map<Integer, List<ProductVO>> pages = new HashMap<>(10);
        for (int i = 0; i < views.size(); ++i) {
            final int pageNo = (i / ProductService.PAGE_SIZE) + 1;
            if (!pages.containsKey(pageNo)) {
                pages.put(pageNo, new ArrayList<>(ProductService.PAGE_SIZE));
            }
            pages.get(pageNo).add(views.get(i));
        }
        pages.forEach((k, v) -> mainPageCache.put(k, new PageVO(k, v)));

        homeCache = CacheBuilder.newBuilder()
                .initialCapacity(1)
                .maximumSize(1)
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .build();
    }

    public HomePageVO getHomePageCache() {
        return homeCache.getIfPresent(HOME_PAGE_CACHE_KEY);
    }

    public void cacheHomePage(final HomePageVO homePageVO) {
        homeCache.put(HOME_PAGE_CACHE_KEY, homePageVO);
    }
}
