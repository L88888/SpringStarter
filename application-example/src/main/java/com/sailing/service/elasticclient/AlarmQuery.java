package com.sailing.service.elasticclient;

import com.sailing.commons.data.es.starter.SailingRestHighLevelClient;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: spring-starter
 * @description: 检索elasticsearch中的预警数据信息
 * @author: LIULEI
 * @create: 2020-08-05 19:18:
 **/
@Slf4j
@Component
public class AlarmQuery {


    @Autowired
    private SailingRestHighLevelClient sailingRestHighLevelClient;

    /**
     * 查询elasticsearch中是否包含指定的索引信息
     */
    public Map queryAlarmData(){
        log.info("检索人脸预警数据.");
        try {
            // 索引名称
            String index_ = "ys_caseinfo";
            // {
//            "query": {
//                "bool": {
//                    "must": [
//                    {
//                        "term": {
//                        "name.keyword": "杨国华"
//                    }
//                    }
//],
//                    "must_not": [ ],
//                    "should": [ ]
//                }
//            },
//            "from": 0,
//                    "size": 10,
//                    "sort": [ ],
//            "aggs": { }
//        }

            BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
            queryBuilder.must(QueryBuilders.matchQuery("name","杨国华"));
            SearchResponse searchResponse = this.searchMessage(index_, queryBuilder);
            Map responseMap = new HashMap();
            responseMap.put("data", searchResponse);
            return responseMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询elasticsearch
     * @param index_ 索引
     * @param boolQueryBuilder 查询语句
     * @return
     */
    private SearchResponse searchMessage(String index_, BoolQueryBuilder boolQueryBuilder) {
        try {
            SearchRequest searchRequest = new SearchRequest(index_);
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.size(100);
            sourceBuilder.query(boolQueryBuilder);
            searchRequest.source(sourceBuilder);
            log.info(searchRequest.toString());
            SearchResponse searchResponse = sailingRestHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);
            searchResponse.getHits().forEach(message -> {
                try {
                    String sourceAsString = message.getSourceAsString();
                    log.info(sourceAsString);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            return searchResponse;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Couldn't get Detail");
        }
    }
}
