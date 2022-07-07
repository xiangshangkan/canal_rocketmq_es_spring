package com.example.demo.constant;

/**
 * @Description: 统一定单es查询字段
 * @Author: zhouhui2
 * @Date: 2022/6/23 4:45 PM
 */
public interface EsUnifiedOrderSearchConstant {

    /****   主字段     ****/
    String BUSINESS_LINE = "business_line";

    String CUSTOMER_ID = "customer_id";

    String ORDER_ID = "order_id";

    String PLACED_AT = "placed_at";

    String STATE = "state";

    String ORDER_TYPE = "order_type";

    String PLATFORM = "platform";

    String IS_RENEWED = "is_renewed";

    String PLACED_BY_ID = "placed_by_id";

    String SUBTOTAL = "subtotal";

    String PAYMENT_OPTION = "payment_option";

    String OWNER_ID = "owner_id";

    String HASHED_CUSTOMER_PHONE_NUMBER = "hashed_customer_phone_number";

    /***  nested payments  ***/
    String NESTED_PAYMENTS = "payments";

    String PAID_AT = "paid_at";

    String TRADE_ID = "trade_id";

    String PAY_STATUS = "state";

    String PAYMENT_METHOD = "payment_method";

    String UPDATED_AT = "updated_at";

    String AMOUNT = "amount";

    /*** nested order_items ***/
    String NESTED_ORDER_ITEMS = "order_items";

    String SPU_NUMBER = "spu_number";

    String TITLE = "title";

    String SKU_NAME = "sku_name";

    String SKU_NUMBER = "sku_number";

    String ORDER_ITEMS_PLACED_AT = "placed_at";

    /*** nested dzk_customer ***/
    String NESTED_DZK_CUSTOMER = "dzk_customer";

    String USER_ID = "user_id";

    String DZK_CUSTOMER_IS_DELETED = "is_deleted";

    String INTERNAL_COUNSELOR_ID = "internal_counselor_id";

    String DZK_CUSTOMER_CREATED_AT = "created_at";

    /*** nested lbk_order ***/
    String NESTED_LBK_ORDER = "lbk_order";

    String OWNER_TYPE = "owner_type";

    String PACKAGE_ID = "package_id";

    String TERM_ID = "term_id";

    String CLASS_ID = "class_id";

    String ADMIN_INTERNAL_ID = "admin_internal_id";

    String LBK_ORDER_CREATED_AT = "created_at";

    String LBK_ORDER_IS_DELETED = "is_deleted";

}
