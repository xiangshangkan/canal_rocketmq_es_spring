package com.example.demo.output;

import lombok.Builder;
import lombok.Data;

/**
 * @Description: 统一订单定制课信息相关
 * @Author: zhouhui2
 * @Date: 2022/6/13 6:55 PM
 */
@Builder
@Data
public class EsUnifiedOrderDzkCustomerOutput {
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 电销跟进人Id
     */
    private Long internalCounselorId;
    /**
     * 是否删除
     */
    private Integer deleted;
    /**
     * 创建时间
     */
    private Long createdAt;
}
