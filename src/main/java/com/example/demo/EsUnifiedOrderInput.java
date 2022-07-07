package com.example.demo;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

/**
 * @Description: 有内部权限的用户请求体
 * @Author: zhouhui2
 * @Date: 2022/6/13 6:55 PM
 */
@Data
public class EsUnifiedOrderInput {

    /**
     * 订单归属业务线
     */
    private List<Integer> businessLines;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 订单id
     */
    private String orderId;
    /**
     * 手机号hash
     */
    private String hashedCustomerPhoneNumber;
    /**
     * 下单时间开始
     */
    private Long placedAtFrom;
    /**
     * 下单时间结束
     */
    private Long placedAtTo;
    /**
     * 订单状态
     */
    private List<Integer> states;
    /**
     * 订单类型
     */
    private List<Integer> orderTypes;
    /**
     * 订单来源
     */
    private List<Integer> platforms;
    /**
     * 开单人idList
     */
    private List<Long> placedByIds;
    /**
     * 是否续费
     */
    private Integer renew;
    /**
     * 订单金额下限
     */
    private BigDecimal subTotalMin;
    /**
     * 订单金额上限
     */
    private BigDecimal subTotalMax;
    /**
     * 付款方式
     */
    private List<Integer> paymentOptions;
    /**
     * 归属人id
     */
    // todo 未测
    private List<Long> ownerInternalIds;


    /**
     * 支付时间开始
     */
    private Long paidAtFrom;
    /**
     * 支付时间结束
     */
    private Long paidAtTo;
    /**
     * 支付流水号
     */
    private String tradeId;
    /**
     * 支付状态
     */
    private List<Integer> payStatus;
    /**
     * 支付渠道
     */
    private List<Integer> paymentMethods;


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
     * 跟进人id
     */
    // todo 未测
    private List<Long> counselorInternalIds;

    /**
     * 归属类型
     */
    // todo 未测
    private List<Integer> ownerTypes;
    /**
     * 录播课课包id
     */
    // todo 未测
    private Long lbkPacketId;
    /**
     * 录播课课期id
     */
    // todo 未测
    private Long lbkTermId;
    /**
     * 录播课班级id
     */
    // todo 未测
    private Long lbkClassId;
    /**
     * 录播课班主任id
     */
    // todo 未测
    private List<Long> lbkHeadTeacherIds;
    /**
     * 并集查询类型
     */
    private Boolean union;
    /**
     * 排序字段
     */
    private String orderBy;
    /**
     * 排序规则
     */
    private Integer orderRule;
    /**
     * 页码
     */
    private Long page = 1L;
    /**
     * 每页条数
     */
    private Long limit = 10L;
}
