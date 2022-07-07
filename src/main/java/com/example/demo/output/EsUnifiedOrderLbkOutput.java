package com.example.demo.output;

import lombok.Builder;
import lombok.Data;

/**
 * @Description: 统一订单录播课信息相关
 * @Author: zhouhui2
 * @Date: 2022/6/13 6:55 PM
 */
@Data
@Builder
public class EsUnifiedOrderLbkOutput {
    /**
     * 归属类型
     */
    private Integer ownerType;
    /**
     * 录播课课包id
     */
    private Long packageId;
    /**
     * 录播课课期id
     */
    private Long termId;
    /**
     * 录播课班级id
     */
    private Long classId;
    /**
     * 录播课班主任id
     */
    private Long adminInternalId;
    /**
     * 是否删除
     */
    private Integer deleted;
    /**
     * 创建时间
     */
    private Long createdAt;
}
