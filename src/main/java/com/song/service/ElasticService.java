package com.song.service;

import com.song.entity.Promotion;
import com.song.utils.DateUtil;
import com.song.utils.ElasticSearchUtil;
import com.song.utils.StringUtil;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by feng on 2019/8/22.
 */
@Service
public class ElasticService {
    //spring data 自己的测试模板
    @Autowired
    ElasticSearchUtil elasticSearchUtil;

    public List<Promotion> findPromotionList(String key){
        String indexName = "es";//索引名称
        String typeName = "promotion";//类型名称
        Map<String, Object> sortMap = new HashMap<String, Object>();
        sortMap.put("createtime","1");
        sortMap.put("from","0");
        sortMap.put("size","10");
        //2.执行查询
        //(1)创建查询条件
        QueryBuilder queryBuilder = null;
        if(StringUtil.isNotNull(key)){
            queryBuilder = QueryBuilders.termQuery("title", key);
        }else {
            queryBuilder = QueryBuilders.matchAllQuery();
        }
        //(2)执行查询
        SearchResponse searchResponse = elasticSearchUtil.searcherPage(indexName, typeName,
                queryBuilder,sortMap);
        //(3)解析结果
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        Promotion promotion = null;;
        List<Promotion> list = new ArrayList<Promotion>();
        for (SearchHit searchHit : searchHits) {
            Map<String, Object> result = searchHit.getSourceAsMap();
            String id = searchHit.getId();
            String title = result.get("title").toString();
            String content = result.get("content").toString();
            String createtime = result.get("createtime").toString();
            promotion = new Promotion();
            promotion.setId(Long.valueOf(id));
            promotion.setTitle(title);
            promotion.setContent(content);
            promotion.setCreatetime(createtime);
            list.add(promotion);
        }
        return list;
    }

    public void addPromotion(String id,String title, String content) {
        //创建对象，设置集群名称和IP地址
        String indexName = "es";//索引名称
        String typeName = "promotion";//类型名称
        XContentBuilder mapping = null;
        try {
            mapping = XContentFactory.jsonBuilder().startObject()
                    .field("title",title)
                    .field("content",content)
                    .field("createtime", DateUtil.getFormatCurDate())
                    .endObject();
            elasticSearchUtil.createIndexAndDocument(indexName, typeName, id, mapping);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
