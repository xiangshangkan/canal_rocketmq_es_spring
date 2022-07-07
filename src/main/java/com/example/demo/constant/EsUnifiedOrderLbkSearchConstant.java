package com.example.demo.constant;

/**
 * @Description: 统一定单es查询字段
 * @Author: zhouhui2
 * @Date: 2022/6/23 4:45 PM
 */
public interface EsUnifiedOrderLbkSearchConstant {
    String NESTED_LBK_ORDER = "lbk_order";

    String OWNER_TYPE = "lbk_order.owner_type";

    String PACKAGE_ID = "lbk_order.package_id";

    String TERM_ID = "lbk_order.term_id";

    String CLASS_ID = "lbk_order.class_id";

    String ADMIN_INTERNAL_ID = "lbk_order.admin_internal_id";
}
