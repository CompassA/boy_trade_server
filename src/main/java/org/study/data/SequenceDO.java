package org.study.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;


/**
 * @author Tomato
 * Created on 2022.12.31
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SequenceDO {

    private Integer id;

    private String name;

    private Long value;

    private Long step;

    private Timestamp createTime;

    private Timestamp updateTime;
}
