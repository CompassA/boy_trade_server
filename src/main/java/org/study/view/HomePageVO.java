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
 * Created on 2020/3/12
 */
@ToString
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HomePageVO {

    private List<CategoryHomeVO> categoryHomeView;

    public HomePageVO(final List<CategoryHomeVO> categoryHomeView) {
        this.categoryHomeView = categoryHomeView;
    }
}
