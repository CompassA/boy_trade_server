package org.study.view;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * @author fanqie
 * @date 2020/3/12
 */
@ToString
@Getter
public class HomePageVO {

    private final List<CategoryHomeVO> categoryHomeView;

    public HomePageVO(final List<CategoryHomeVO> categoryHomeView) {
        this.categoryHomeView = categoryHomeView;
    }
}
