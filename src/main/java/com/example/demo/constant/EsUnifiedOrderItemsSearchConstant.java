package com.example.demo.constant;

/**
 * @Description: 统一定单es查询字段
 * @Author: zhouhui2
 * @Date: 2022/6/23 4:45 PM
 */
public interface EsUnifiedOrderItemsSearchConstant {
    String NESTED_ORDER_ITEMS = "order_items";

    String SPU_NUMBER = "order_items.spu_number";

    String TITLE = "order_items.title";

    String SKU_NAME = "order_items.sku_name";

    String SKU_NUMBER = "order_items.sku_number";
}
