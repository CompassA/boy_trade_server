package org.study.view;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * @author fanqie
 * @date 2020/3/11
 */
@Getter
@ToString
public class PageVO {

    private final Integer pageNo;

    private final List<ProductVO> views;

    public PageVO(final Integer pageNo, final List<ProductVO> views) {
        this.pageNo = pageNo;
        this.views = views;
    }
}
