package org.study.service.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author fanqie
 * @date 2020/1/11
 */
@Getter
@Setter
@Accessors(chain = true)
@ToString
public class ProductModel {

    private Integer productId;

    private Byte payStatus;

    private Integer categoryId;

    private Integer sales;

    @NotNull(message = "库存不能为空")
    @Min(value = 1, message = "库存不能为空")
    private Integer stock;

    @NotNull(message = "商品发布者信息缺失")
    private Integer userId;

    @NotNull(message = "价格不能为空")
    private BigDecimal price;

    @NotNull(message = "请输入商品名称")
    private String productName;

    @NotNull(message = "请输入商品描述")
    private String description;

    @NotNull(message = "必须上传商品图片")
    private String iconUrl;

    private Timestamp createTime;
}
