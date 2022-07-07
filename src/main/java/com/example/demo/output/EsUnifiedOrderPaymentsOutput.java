package com.example.demo.output;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

/**
 * @Description: 统一订单支付信息相关
 * @Author: zhouhui2
 * @Date: 2022/6/13 6:55 PM
 */
@Builder
@Data
public class EsUnifiedOrderPaymentsOutput {

    /**
     * 支付时间
     */
    private Long paidAt;
    /**
     * 支付流水号
     */
    private String tradeId;
    /**
     * 支付状态
     */
    private Integer payStatus;
    /**
     * 支付渠道
     */
    private Integer paymentMethod;
    /**
     * 更新时间
     */
    private Long updatedAt;
    /**
     * 支付金额
     */
    private BigDecimal amount;
}
