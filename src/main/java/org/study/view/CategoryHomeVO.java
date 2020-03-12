package org.study.view;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author fanqie
 * @date 2020/3/12
 */
@ToString
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryHomeVO {

    private Integer categoryId;

    private List<ProductVO> topFive;

    public CategoryHomeVO(final Integer categoryId, final List<ProductVO> topFive) {
        this.categoryId = categoryId;
        this.topFive = topFive;
    }
}
