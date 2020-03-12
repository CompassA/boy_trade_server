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
public class CategoryHomeVO {

    private final Integer categoryId;

    private final List<ProductVO> topFive;

    public CategoryHomeVO(final Integer categoryId, final List<ProductVO> topFive) {
        this.categoryId = categoryId;
        this.topFive = topFive;
    }
}
