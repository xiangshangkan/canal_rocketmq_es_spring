package com.example.demo.output;

import lombok.Builder;
import lombok.Data;

/**
 * @Description: 统一订单订单明细相关
 * @Author: zhouhui2
 * @Date: 2022/6/13 6:55 PM
 */
@Data
@Builder
public class EsUnifiedOrderItemsOutput {
    /**
     * 商品编码
     */
    private String spuNumber;
    /**
     * 商品名称
     */
    private String spuName;
    /**
     * 规格名称
     */
    private String skuName;
    /**
     * 规格编码
     */
    private String skuNumber;
    /**
     * 创建时间
     */
    private Long placedAt;
}
