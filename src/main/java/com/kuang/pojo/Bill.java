package com.kuang.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 梁志超
 * @version 1.0
 * @time 2023/3/25 20:14
 */
@Data
public class Bill {
    private Integer id;
    private String billCode;//账单编号
    private String productName;//商品名称
    private String productDesc;//商品描述
    private String productUnit;//商品单位
    private BigDecimal productCount;//商品数量
    private BigDecimal totalPrice;//总价格
    private Integer isPayment;//是否支付
    private Integer createdBy;//创建的人
    private Date creationDate;//创建的日期
    private Integer modifyBy;//更新者
    private Date modifyDate;//更新日期
    private Integer providerId;//供应商id

    //供应商名称
    private String providerName;




}
