package com.example.demo.constant;

/**
 * @Description: 统一定单es查询字段
 * @Author: zhouhui2
 * @Date: 2022/6/23 4:45 PM
 */
public interface EsUnifiedOrderPaymentsSearchConstant {
    String NESTED_PAYMENTS = "payments";

    String PAID_AT = "payments.paid_at";

    String TRADE_ID = "payments.trade_id";

    String PAY_STATUS = "payments.state";

    String PAYMENT_METHOD = "payments.payment_method";

    String AMOUNT = "payments.amount";
}
