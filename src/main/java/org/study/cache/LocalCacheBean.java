package org.study.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.study.service.ProductService;
import org.study.service.RedisService;
import org.study.service.model.enumdata.CacheType;
import org.study.util.ModelToViewUtil;
import org.study.util.MyStringUtil;
import org.study.view.CategoryHomeVO;
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

    @Autowired
    private RedisService redisService;

    private MyCache<Integer, PageVO> mainPageCache;

    private MyCache<String, PageVO> categoryPageCache;

    private Cache<Integer, HomePageVO> homeCache;

    @PostConstruct
    public void initCache() {
        //init main page cache
        mainPageCache = LRUFactory.create();
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

        //init home page cache
        homeCache = CacheBuilder.newBuilder()
                .initialCapacity(1)
                .maximumSize(1)
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .build();
        final List<CategoryHomeVO> topFives = new ArrayList<>(ProductService.CATEGORY_LIST.size());
        for (final Integer id : ProductService.CATEGORY_LIST) {
            List<ProductVO> vo = ModelToViewUtil.getProductViews(productService.selectTopFive(id));
            topFives.add(new CategoryHomeVO(id, vo));
        }
        final HomePageVO data = new HomePageVO(topFives);
        final String redisKey = MyStringUtil.getCacheKey(0, CacheType.HOME_PAGE);
        redisService.cacheDataWithoutLocalCache(redisKey, data);
        homeCache.put(HOME_PAGE_CACHE_KEY, data);

        //init category page cache
        categoryPageCache = LRUFactory.create();
    }

    public HomePageVO getHomePageCache() {
        return homeCache.getIfPresent(HOME_PAGE_CACHE_KEY);
    }

    public void cacheHomePage(final HomePageVO homePageVO) {
        homeCache.put(HOME_PAGE_CACHE_KEY, homePageVO);
    }

    public PageVO getPageCache(final Integer typeId, final Integer page) {
        if (typeId == null) {
            return mainPageCache.get(page);
        }
        return categoryPageCache.get(getCategoryPageKey(typeId, page));
    }

    public void cachePage(final Integer typeId, final Integer page, final PageVO pageVO) {
        if (typeId == null) {
            mainPageCache.put(page, pageVO);
        }
        categoryPageCache.put(getCategoryPageKey(typeId, page), pageVO);
    }

    public void invalidatePageCache() {
        mainPageCache.invalidate();
        categoryPageCache.invalidate();
    }

    private String getCategoryPageKey(final Integer typeId, final Integer page) {
        return String.format("%d:%d", typeId, page);
    }
}
