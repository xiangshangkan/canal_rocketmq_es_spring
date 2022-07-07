package com.example.demo;

import com.example.demo.input.EsUnifiedOrderInput;
import com.example.demo.output.EsUnifiedOrderOutput;
import java.io.IOException;
import java.util.List;

/**
 * @Description: es 统一订单
 * @Author: zhouhui2
 * @Date: 2022/6/23 8:36 PM
 */
public interface EsUnifiedOrderService {

    /**
     * 统一订单分页
     * @param input
     * @return
     * @throws IOException
     */
    List<EsUnifiedOrderOutput> pageListEsUnifiedOrders(EsUnifiedOrderInput input) throws IOException;

}
