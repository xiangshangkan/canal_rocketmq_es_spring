package com.example.demo.output;

import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * @Description: 统一订单信息
 * @Author: zhouhui2
 * @Date: 2022/6/13 6:55 PM
 */
@Data
@Builder
public class EsUnifiedOrderOutput {

    /**
     * 订单归属业务线
     */
    private Integer businessLine;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 订单id
     */
    private String orderId;
    /**
     * 下单时间
     */
    private Long placedAt;
    /**
     * 订单状态
     */
    private Integer state;
    /**
     * 订单类型
     */
    private Integer orderType;
    /**
     * 订单来源
     */
    private Integer platform;
    /**
     * 是否续费
     */
    private Integer renew;
    /**
     * 开单人
     */
    private Long placedById;

    /**
     * 订单金额
     */
    private BigDecimal subtotal;
    /**
     * 付款方式
     */
    private Integer paymentOption;
    /**
     * 归属人id
     */
    private Long ownerId;
    /**
     * 订单明细
     */
    private List<EsUnifiedOrderItemsOutput> orderItemsOutputs;
    /**
     * 订单支付明细
     */
    private List<EsUnifiedOrderPaymentsOutput> orderPaymentsOutputs;
    /**
     * 录播课信息
     */
    private List<EsUnifiedOrderLbkOutput> orderLbkOutputs;
    /**
     * 定制课信息
     */
    private List<EsUnifiedOrderDzkCustomerOutput> orderDzkCustomerOutputs;
}
