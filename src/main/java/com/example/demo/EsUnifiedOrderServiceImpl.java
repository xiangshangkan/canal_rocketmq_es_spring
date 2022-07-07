package com.example.demo;

import com.example.demo.config.ElasticsearchIndexConfig;
import com.example.demo.constant.CommonConstant;
import com.example.demo.constant.EsUnifiedOrderDzkCustomerSearchConstant;
import com.example.demo.constant.EsUnifiedOrderItemsSearchConstant;
import com.example.demo.constant.EsUnifiedOrderLbkSearchConstant;
import com.example.demo.constant.EsUnifiedOrderPaymentsSearchConstant;
import com.example.demo.constant.EsUnifiedOrderSearchConstant;
import com.example.demo.input.EsUnifiedOrderInput;
import com.example.demo.output.EsUnifiedOrderDzkCustomerOutput;
import com.example.demo.output.EsUnifiedOrderItemsOutput;
import com.example.demo.output.EsUnifiedOrderLbkOutput;
import com.example.demo.output.EsUnifiedOrderOutput;
import com.example.demo.output.EsUnifiedOrderPaymentsOutput;
import io.micrometer.core.instrument.util.StringUtils;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.NestedSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortMode;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


import static com.example.demo.constant.EsUnifiedOrderSearchConstant.ADMIN_INTERNAL_ID;
import static com.example.demo.constant.EsUnifiedOrderSearchConstant.AMOUNT;
import static com.example.demo.constant.EsUnifiedOrderSearchConstant.BUSINESS_LINE;
import static com.example.demo.constant.EsUnifiedOrderSearchConstant.CLASS_ID;
import static com.example.demo.constant.EsUnifiedOrderSearchConstant.CUSTOMER_ID;
import static com.example.demo.constant.EsUnifiedOrderSearchConstant.DZK_CUSTOMER_CREATED_AT;
import static com.example.demo.constant.EsUnifiedOrderSearchConstant.DZK_CUSTOMER_IS_DELETED;
import static com.example.demo.constant.EsUnifiedOrderSearchConstant.HASHED_CUSTOMER_PHONE_NUMBER;
import static com.example.demo.constant.EsUnifiedOrderSearchConstant.INTERNAL_COUNSELOR_ID;
import static com.example.demo.constant.EsUnifiedOrderSearchConstant.IS_RENEWED;
import static com.example.demo.constant.EsUnifiedOrderSearchConstant.LBK_ORDER_IS_DELETED;
import static com.example.demo.constant.EsUnifiedOrderSearchConstant.NESTED_DZK_CUSTOMER;
import static com.example.demo.constant.EsUnifiedOrderSearchConstant.NESTED_LBK_ORDER;
import static com.example.demo.constant.EsUnifiedOrderSearchConstant.NESTED_ORDER_ITEMS;
import static com.example.demo.constant.EsUnifiedOrderSearchConstant.NESTED_PAYMENTS;
import static com.example.demo.constant.EsUnifiedOrderSearchConstant.ORDER_ID;
import static com.example.demo.constant.EsUnifiedOrderSearchConstant.ORDER_ITEMS_PLACED_AT;
import static com.example.demo.constant.EsUnifiedOrderSearchConstant.ORDER_TYPE;
import static com.example.demo.constant.EsUnifiedOrderSearchConstant.OWNER_ID;
import static com.example.demo.constant.EsUnifiedOrderSearchConstant.OWNER_TYPE;
import static com.example.demo.constant.EsUnifiedOrderSearchConstant.PACKAGE_ID;
import static com.example.demo.constant.EsUnifiedOrderSearchConstant.PAID_AT;
import static com.example.demo.constant.EsUnifiedOrderSearchConstant.PAYMENT_METHOD;
import static com.example.demo.constant.EsUnifiedOrderSearchConstant.PAYMENT_OPTION;
import static com.example.demo.constant.EsUnifiedOrderSearchConstant.PAY_STATUS;
import static com.example.demo.constant.EsUnifiedOrderSearchConstant.PLACED_AT;
import static com.example.demo.constant.EsUnifiedOrderSearchConstant.PLACED_BY_ID;
import static com.example.demo.constant.EsUnifiedOrderSearchConstant.PLATFORM;
import static com.example.demo.constant.EsUnifiedOrderSearchConstant.SKU_NAME;
import static com.example.demo.constant.EsUnifiedOrderSearchConstant.SKU_NUMBER;
import static com.example.demo.constant.EsUnifiedOrderSearchConstant.SPU_NUMBER;
import static com.example.demo.constant.EsUnifiedOrderSearchConstant.STATE;
import static com.example.demo.constant.EsUnifiedOrderSearchConstant.SUBTOTAL;
import static com.example.demo.constant.EsUnifiedOrderSearchConstant.TERM_ID;
import static com.example.demo.constant.EsUnifiedOrderSearchConstant.TITLE;
import static com.example.demo.constant.EsUnifiedOrderSearchConstant.TRADE_ID;
import static com.example.demo.constant.EsUnifiedOrderSearchConstant.UPDATED_AT;
import static com.example.demo.constant.EsUnifiedOrderSearchConstant.USER_ID;

/**
 * @Description: es 统一订单
 * @Author: zhouhui2
 * @Date: 2022/6/17 3:19 PM
 */
@Service
@Slf4j
public class EsUnifiedOrderServiceImpl implements EsUnifiedOrderService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Autowired
    private ElasticsearchIndexConfig elasticsearchIndexConfig;

    @Override
    public List<EsUnifiedOrderOutput> pageListEsUnifiedOrders(EsUnifiedOrderInput input) throws IOException {

        SearchRequest request = new SearchRequest(elasticsearchIndexConfig.getUnifiedOrderIndex());

        SearchSourceBuilder builder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = mainSearchCondition(input);
        if (input.getUnion()) {
            // 并集情况
            boolQueryBuilder.must(unionSearchCondition(input));
        }
        builder.query(boolQueryBuilder);
        builder.from(0);
        builder.size(10);
        builder.sort(orderBy(input.getOrderBy()).order(this.orderRule(input.getOrderRule())));
        request.source(builder);

        SearchResponse search = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        SearchHits hits = search.getHits();
        List<EsUnifiedOrderOutput> outputs = constructOutput(hits.getHits());
        return outputs;
    }

    private List<EsUnifiedOrderOutput> constructOutput(SearchHit[] arr) {
        if (arr.length < 1) {
            return new ArrayList<>();
        }
        return Arrays.stream(arr).map(it->{
            Map<String, Object> map = it.getSourceAsMap();
            EsUnifiedOrderOutput orderOutput = EsUnifiedOrderOutput.builder()
                    .businessLine(objectToIntegerNullable(map.get(BUSINESS_LINE)))
                    .userId(objectToLongNullable(map.get(CUSTOMER_ID)))
                    .orderId(objectToStringNullable(map.get(ORDER_ID)))
                    .placedAt(objectToLongNullable(map.get(PLACED_AT)))
                    .state(objectToIntegerNullable(map.get(STATE)))
                    .orderType(objectToIntegerNullable(map.get(ORDER_TYPE)))
                    .platform(objectToIntegerNullable(map.get(PLATFORM)))
                    .renew(objectToIntegerNullable(map.get(IS_RENEWED)))
                    .placedById(objectToLongNullable(map.get(PLACED_BY_ID)))
                    .subtotal(objectToBigDecimalNullable(map.get(SUBTOTAL)))
                    .paymentOption(objectToIntegerNullable(map.get(PAYMENT_OPTION)))
                    .ownerId(objectToLongNullable(map.get(OWNER_ID)))
                    .build();

            // 订单支付信息
            List<HashMap<String, Object>> paymentsList = (List<HashMap<String, Object>>)map.get(NESTED_PAYMENTS);
            if (!CollectionUtils.isEmpty(paymentsList)) {
                List<EsUnifiedOrderPaymentsOutput> paymentsOutputs = paymentsList.stream().map(payment->
                    EsUnifiedOrderPaymentsOutput.builder()
                            .paidAt(objectToLongNullable(payment.get(PAID_AT)))
                            .paymentMethod(objectToIntegerNullable(payment.get(PAYMENT_METHOD)))
                            .payStatus(objectToIntegerNullable(payment.get(PAY_STATUS)))
                            .tradeId(objectToStringNullable(payment.get(TRADE_ID)))
                            .updatedAt(objectToLongNullable(payment.get(UPDATED_AT)))
                            .amount(objectToBigDecimalNullable(payment.get(AMOUNT)))
                            .build()
                ).collect(Collectors.toList());
                orderOutput.setOrderPaymentsOutputs(paymentsOutputs);
            }
            // 订单项信息
            List<HashMap<String, Object>> orderItemsList = (List<HashMap<String, Object>>)map.get(NESTED_ORDER_ITEMS);
            if (!CollectionUtils.isEmpty(orderItemsList)) {
                List<EsUnifiedOrderItemsOutput> orderItemsOutputs = orderItemsList.stream().map(orderItem->
                    EsUnifiedOrderItemsOutput.builder()
                            .placedAt(objectToLongNullable(orderItem.get(ORDER_ITEMS_PLACED_AT)))
                            .skuName(objectToStringNullable(orderItem.get(SKU_NAME)))
                            .skuNumber(objectToStringNullable(orderItem.get(SKU_NUMBER)))
                            .spuName(objectToStringNullable(orderItem.get(TITLE)))
                            .spuNumber(objectToStringNullable(orderItem.get(SPU_NUMBER)))
                            .build()
                ).collect(Collectors.toList());
                orderOutput.setOrderItemsOutputs(orderItemsOutputs);
            }
            // 录播课信息
            List<HashMap<String, Object>> lbkOrderList = (List<HashMap<String, Object>>)map.get(NESTED_LBK_ORDER);
            if (!CollectionUtils.isEmpty(lbkOrderList)) {
                List<EsUnifiedOrderLbkOutput> orderLbkOutputs = lbkOrderList.stream().map(lbkOrder->
                    EsUnifiedOrderLbkOutput.builder()
                            .adminInternalId(objectToLongNullable(lbkOrder.get(ADMIN_INTERNAL_ID)))
                            .classId(objectToLongNullable(lbkOrder.get(CLASS_ID)))
                            .deleted(objectToIntegerNullable(lbkOrder.get(LBK_ORDER_IS_DELETED)))
                            .ownerType(objectToIntegerNullable(lbkOrder.get(OWNER_TYPE)))
                            .packageId(objectToLongNullable(lbkOrder.get(PACKAGE_ID)))
                            .termId(objectToLongNullable(lbkOrder.get(TERM_ID)))
                            .build()
                ).collect(Collectors.toList());
                orderOutput.setOrderLbkOutputs(orderLbkOutputs);
            }
            // 定制课信息
            List<HashMap<String, Object>> dzkCustomerList = (List<HashMap<String, Object>>)map.get(NESTED_DZK_CUSTOMER);
            if (!CollectionUtils.isEmpty(dzkCustomerList)) {
                List<EsUnifiedOrderDzkCustomerOutput> orderDzkCustomerOutputs = dzkCustomerList.stream().map(dzkCustomer->
                    EsUnifiedOrderDzkCustomerOutput.builder()
                            .userId(objectToLongNullable(dzkCustomer.get(USER_ID)))
                            .deleted(objectToIntegerNullable(dzkCustomer.get(DZK_CUSTOMER_IS_DELETED)))
                            .internalCounselorId(objectToLongNullable(dzkCustomer.get(INTERNAL_COUNSELOR_ID)))
                            .createdAt(objectToLongNullable(dzkCustomer.get(DZK_CUSTOMER_CREATED_AT)))
                            .build()
                ).collect(Collectors.toList());
                orderOutput.setOrderDzkCustomerOutputs(orderDzkCustomerOutputs);
            }
            return orderOutput;
        }).collect(Collectors.toList());
    }

    private BoolQueryBuilder unionSearchCondition(EsUnifiedOrderInput input) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (!CollectionUtils.isEmpty(input.getCounselorInternalIds())) {
            TermsQueryBuilder counselorTermsQuery = QueryBuilders.termsQuery(EsUnifiedOrderDzkCustomerSearchConstant.INTERNAL_COUNSELOR_ID, input.getCounselorInternalIds());
            boolQueryBuilder.should(QueryBuilders.nestedQuery(EsUnifiedOrderDzkCustomerSearchConstant.NESTED_DZK_CUSTOMER, QueryBuilders.boolQuery().must(counselorTermsQuery), ScoreMode.Total));
        }
        if (!CollectionUtils.isEmpty(input.getOwnerInternalIds())) {
            boolQueryBuilder.should(QueryBuilders.termsQuery(OWNER_ID, input.getOwnerInternalIds()));
        }
        if (!CollectionUtils.isEmpty(input.getPlacedByIds())) {
            boolQueryBuilder.should(QueryBuilders.termsQuery(PLACED_BY_ID, input.getPlacedByIds()));
        }
        if (!CollectionUtils.isEmpty(input.getLbkHeadTeacherIds())) {
            TermsQueryBuilder lbkHeadTeacherTermsQuery = QueryBuilders.termsQuery(EsUnifiedOrderLbkSearchConstant.ADMIN_INTERNAL_ID, input.getLbkHeadTeacherIds());
            boolQueryBuilder.should(QueryBuilders.nestedQuery(EsUnifiedOrderLbkSearchConstant.NESTED_LBK_ORDER, lbkHeadTeacherTermsQuery, ScoreMode.Total));
        }
        return boolQueryBuilder;
    }

    private BoolQueryBuilder mainSearchCondition(EsUnifiedOrderInput input) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (!CollectionUtils.isEmpty(input.getBusinessLines())) {
            boolQueryBuilder.must(QueryBuilders.termsQuery(BUSINESS_LINE, input.getBusinessLines()));
        }
        if (Objects.nonNull(input.getUserId())) {
            boolQueryBuilder.must(QueryBuilders.termQuery(CUSTOMER_ID, input.getUserId()));
        }
        if (Objects.nonNull(input.getOrderId())) {
            boolQueryBuilder.must(QueryBuilders.termQuery(ORDER_ID, input.getOrderId()));
        }
        if (Objects.nonNull(input.getPlacedAtFrom())) {
            boolQueryBuilder.must(QueryBuilders.rangeQuery(PLACED_AT).from(input.getPlacedAtFrom()));
        }
        if (Objects.nonNull(input.getPlacedAtTo())) {
            boolQueryBuilder.must(QueryBuilders.rangeQuery(PLACED_AT).to(input.getPlacedAtTo()));
        }
        if (!CollectionUtils.isEmpty(input.getStates())) {
            boolQueryBuilder.must(QueryBuilders.termsQuery(STATE, input.getStates()));
        }
        if (!CollectionUtils.isEmpty(input.getOrderTypes())) {
            boolQueryBuilder.must(QueryBuilders.termsQuery(ORDER_TYPE, input.getOrderTypes()));

        }
        if (!CollectionUtils.isEmpty(input.getPlatforms())) {
            boolQueryBuilder.must(QueryBuilders.termsQuery(PLATFORM, input.getPlatforms()));
        }
        if (Objects.nonNull(input.getRenew())) {
            boolQueryBuilder.must(QueryBuilders.termQuery(IS_RENEWED, input.getRenew()));
        }
        if (!CollectionUtils.isEmpty(input.getPlacedByIds()) && !input.getUnion()) {
            boolQueryBuilder.must(QueryBuilders.termsQuery(PLACED_BY_ID, input.getPlacedByIds()));
        }
        if (Objects.nonNull(input.getSubTotalMin())) {
            boolQueryBuilder.must(QueryBuilders.rangeQuery(SUBTOTAL).from(input.getSubTotalMin()));
        }
        if (Objects.nonNull(input.getSubTotalMax())) {
            boolQueryBuilder.must(QueryBuilders.rangeQuery(SUBTOTAL).to(input.getSubTotalMax()));
        }
        if (!CollectionUtils.isEmpty(input.getPaymentOptions())) {
            boolQueryBuilder.must(QueryBuilders.termsQuery(PAYMENT_OPTION, input.getPaymentOptions()));
        }
        if (!CollectionUtils.isEmpty(input.getOwnerInternalIds()) && !input.getUnion()) {
            boolQueryBuilder.must(QueryBuilders.termsQuery(OWNER_ID, input.getOwnerInternalIds()));
        }
        if (StringUtils.isNotBlank(input.getHashedCustomerPhoneNumber())) {
            boolQueryBuilder.must(QueryBuilders.termQuery(HASHED_CUSTOMER_PHONE_NUMBER, input.getHashedCustomerPhoneNumber()));
        }
        paymentsSearchCondition(input).ifPresent(boolQueryBuilder::must);
        orderItemsSearchCondition(input).ifPresent(boolQueryBuilder::must);
        dzkCustomerSearchCondition(input).ifPresent(boolQueryBuilder::must);
        lbkOrderSearchCondition(input).ifPresent(boolQueryBuilder::must);
        return boolQueryBuilder;
    }

    /**
     * 支付信息筛选
     * @param input
     * @return
     */
    private Optional<NestedQueryBuilder> paymentsSearchCondition(EsUnifiedOrderInput input) {
        if (
                Objects.nonNull(input.getPaidAtFrom()) || Objects.nonNull(input.getPaidAtTo()) ||
                        StringUtils.isNotBlank(input.getTradeId()) || Objects.nonNull(input.getPayStatus())
                || !CollectionUtils.isEmpty(input.getPaymentMethods())
        ) {
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            if (Objects.nonNull(input.getPaidAtFrom())) {
                boolQueryBuilder.must(QueryBuilders.rangeQuery(EsUnifiedOrderPaymentsSearchConstant.PAID_AT).from(input.getPaidAtFrom()));
            }
            if (Objects.nonNull(input.getPaidAtTo())) {
                boolQueryBuilder.must(QueryBuilders.rangeQuery(EsUnifiedOrderPaymentsSearchConstant.PAID_AT).to(input.getPaidAtTo()));
            }
            if (StringUtils.isNotBlank(input.getTradeId())) {
                boolQueryBuilder.must(QueryBuilders.termQuery(EsUnifiedOrderPaymentsSearchConstant.TRADE_ID, input.getTradeId()));
            }
            if (Objects.nonNull(input.getPayStatus())) {
                boolQueryBuilder.must(QueryBuilders.termQuery(EsUnifiedOrderPaymentsSearchConstant.PAY_STATUS, input.getPayStatus()));
            }
            if (!CollectionUtils.isEmpty(input.getPaymentMethods())) {
                boolQueryBuilder.must(QueryBuilders.termsQuery(EsUnifiedOrderPaymentsSearchConstant.PAYMENT_METHOD, input.getPaymentMethods()));
            }
            return Optional.of(QueryBuilders.nestedQuery(EsUnifiedOrderPaymentsSearchConstant.NESTED_PAYMENTS, boolQueryBuilder, ScoreMode.Total));
        }
        return Optional.empty();
    }

    /**
     * 订单明细信息筛选
     * @param input
     * @return
     */
    private Optional<NestedQueryBuilder> orderItemsSearchCondition(EsUnifiedOrderInput input) {
        if (
                !StringUtils.isBlank(input.getSpuNumber()) || !StringUtils.isBlank(input.getSpuName()) ||
                        !StringUtils.isBlank(input.getSkuNumber()) || !StringUtils.isBlank(input.getSkuName())
        ) {
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            if (!StringUtils.isBlank(input.getSpuNumber())) {
                boolQueryBuilder.must(QueryBuilders.termsQuery(EsUnifiedOrderItemsSearchConstant.SPU_NUMBER, input.getSpuNumber()));
            }
            if (!StringUtils.isBlank(input.getSpuName())) {
                boolQueryBuilder.must(QueryBuilders.termsQuery(EsUnifiedOrderItemsSearchConstant.TITLE, input.getSpuName()));
            }
            if (!StringUtils.isBlank(input.getSkuNumber())) {
                boolQueryBuilder.must(QueryBuilders.termsQuery(EsUnifiedOrderItemsSearchConstant.SKU_NUMBER, input.getSkuNumber()));
            }
            if (!StringUtils.isBlank(input.getSkuName())) {
                boolQueryBuilder.must(QueryBuilders.termsQuery(EsUnifiedOrderItemsSearchConstant.SKU_NAME, input.getSkuName()));
            }
            return Optional.of(QueryBuilders.nestedQuery(EsUnifiedOrderItemsSearchConstant.NESTED_ORDER_ITEMS, boolQueryBuilder, ScoreMode.Total));
        }
        return Optional.empty();
    }

    /**
     * 定制课信息筛选
     * @param input
     * @return
     */
    private Optional<NestedQueryBuilder> dzkCustomerSearchCondition(EsUnifiedOrderInput input) {
        if (!CollectionUtils.isEmpty(input.getCounselorInternalIds())) {
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            if (!CollectionUtils.isEmpty(input.getCounselorInternalIds()) && !input.getUnion()) {
                boolQueryBuilder.must(QueryBuilders.termsQuery(EsUnifiedOrderDzkCustomerSearchConstant.INTERNAL_COUNSELOR_ID, input.getCounselorInternalIds()));
            }
            return Optional.of(QueryBuilders.nestedQuery(EsUnifiedOrderDzkCustomerSearchConstant.NESTED_DZK_CUSTOMER, boolQueryBuilder, ScoreMode.Total));
        }
        return Optional.empty();
    }

    /**
     * 录播课信息筛选
     * @param input
     * @return
     */
    private Optional<NestedQueryBuilder> lbkOrderSearchCondition(EsUnifiedOrderInput input) {
        if (
                !CollectionUtils.isEmpty(input.getOwnerTypes()) || Objects.nonNull(input.getLbkClassId()) ||
                        Objects.nonNull(input.getLbkTermId()) || Objects.nonNull(input.getLbkPacketId()) ||
                        !CollectionUtils.isEmpty(input.getLbkHeadTeacherIds())
        ) {
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            if (!CollectionUtils.isEmpty(input.getOwnerTypes())) {
                boolQueryBuilder.must(QueryBuilders.termsQuery(EsUnifiedOrderLbkSearchConstant.OWNER_TYPE, input.getOwnerTypes()));
            }
            if (Objects.nonNull(input.getLbkClassId())) {
                boolQueryBuilder.must(QueryBuilders.termQuery(EsUnifiedOrderLbkSearchConstant.CLASS_ID, input.getLbkClassId()));
            }
            if (Objects.nonNull(input.getLbkTermId())) {
                boolQueryBuilder.must(QueryBuilders.termQuery(EsUnifiedOrderLbkSearchConstant.TERM_ID, input.getLbkTermId()));
            }
            if (Objects.nonNull(input.getLbkPacketId())) {
                boolQueryBuilder.must(QueryBuilders.termQuery(EsUnifiedOrderLbkSearchConstant.PACKAGE_ID, input.getLbkPacketId()));
            }
            if (!CollectionUtils.isEmpty(input.getLbkHeadTeacherIds()) && !input.getUnion()) {
                boolQueryBuilder.must(QueryBuilders.termsQuery(EsUnifiedOrderLbkSearchConstant.ADMIN_INTERNAL_ID, input.getLbkHeadTeacherIds()));
            }
            return Optional.of(QueryBuilders.nestedQuery(EsUnifiedOrderLbkSearchConstant.NESTED_LBK_ORDER, boolQueryBuilder, ScoreMode.Total));
        }
        return Optional.empty();
    }

    private FieldSortBuilder orderBy(String orderBy) {
        if (EsUnifiedOrderSearchConstant.PLACED_AT.equals(orderBy)) {
            return SortBuilders.fieldSort(EsUnifiedOrderSearchConstant.PLACED_AT);
        } else if (EsUnifiedOrderPaymentsSearchConstant.PAID_AT.equals(orderBy)) {
            return  SortBuilders.fieldSort(EsUnifiedOrderPaymentsSearchConstant.PAID_AT).sortMode(SortMode.MAX).setNestedSort(new NestedSortBuilder(EsUnifiedOrderPaymentsSearchConstant.NESTED_PAYMENTS));
        } else {
            return SortBuilders.fieldSort(EsUnifiedOrderSearchConstant.PLACED_AT);
        }
    }

    private SortOrder orderRule(Integer key) {
        if (CommonConstant.Integer_1.equals(key)) {
            return SortOrder.ASC;
        } else {
            return SortOrder.DESC;
        }
    }

    private Long objectToLongNullable(Object value) {
        return value == null ? null : Long.valueOf(String.valueOf(value));
    }
    private Integer objectToIntegerNullable(Object value) {
        return value == null ? null : Integer.valueOf(String.valueOf(value));
    }
    private String objectToStringNullable(Object value) {
        return value ==  null ? null : String.valueOf(value);
    }
    private BigDecimal objectToBigDecimalNullable(Object value) {
        return value == null ? null : BigDecimal.valueOf((Double) value);
    }
}
