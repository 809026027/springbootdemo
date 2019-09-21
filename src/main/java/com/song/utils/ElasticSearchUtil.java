package com.song.utils;

/**
 * Created by 17060342 on 2019/6/21.
 */

import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ElasticSearchUtil {
    @Autowired
    private TransportClient transportClient;


    /**
     * 创建索引并添加映射
     */
    public void createIndexAndMapping(String indexName, String typeName,XContentBuilder mapping){

        CreateIndexRequestBuilder cib = transportClient.admin().indices().prepareCreate(indexName);
        cib.addMapping(typeName, mapping).execute().actionGet();
    }

    /**
     * 创建索引
     * @param indexName 索引名称，相当于数据库名称
     * @param typeName 索引类型，相当于数据库中的表名
     * @param id id名称，相当于每个表中某一行记录的标识，手动设置或者es自动生成（设置null）
     * @param data doc
     */
    public void createIndexAndDocument(String indexName, String typeName, String id,XContentBuilder data) {
        IndexRequestBuilder requestBuilder = transportClient.prepareIndex(indexName,
                typeName, id);//设置索引名称，索引类型，id
        requestBuilder.setSource(data).execute().actionGet();//创建索引
    }


    /**
     * 执行搜索
     * @param indexName 索引名称
     * @param typeName 索引类型
     * @param queryBuilder 查询条件
     *
     * 使用QueryBuilder
     * 1.常规查询
     * QueryBuilder queryBuilder = QueryBuilders.termQuery("name", "kimchy");
     * termQuery("key", obj) 完全匹配
     * termsQuery("key", obj1, obj2..)   一次匹配多个值
     * matchQuery("key", Obj) 单个匹配, field不支持通配符, 前缀具高级特性
     * multiMatchQuery("text", "field1", "field2"..);  匹配多个字段, field有通配符忒行
     * matchAllQuery();         匹配所有文件
     *
     * 2.组合查询 AND NOT OR
     * QueryBuilder queryBuilder = QueryBuilders.boolQuery()
    .must(QueryBuilders.termQuery("user", "kimchy"))
    .mustNot(QueryBuilders.termQuery("message", "nihao"))
    .should(QueryBuilders.termQuery("gender", "male"));

     * 3.模糊查询
     * QueryBuilder queryBuilder = QueryBuilders.fuzzyQuery("user", "kimch");
     * @return
     */
    public SearchResponse searcher(String indexName, String typeName,
                                   QueryBuilder queryBuilder) {
        SearchResponse searchResponse = transportClient.prepareSearch(indexName)
                .setTypes(typeName).setQuery(queryBuilder).execute()
                .actionGet();//执行查询
        return searchResponse;
    }


    /**
     * 执行搜索
     * @param indexName 索引名称
     * @param typeName 索引类型
     * @param queryBuilder 查询条件
     *
     * 使用QueryBuilder
     * 1.常规查询
     * QueryBuilder queryBuilder = QueryBuilders.termQuery("name", "kimchy");
     * termQuery("key", obj) 完全匹配
     * termsQuery("key", obj1, obj2..)   一次匹配多个值
     * matchQuery("key", Obj) 单个匹配, field不支持通配符, 前缀具高级特性
     * multiMatchQuery("text", "field1", "field2"..);  匹配多个字段, field有通配符忒行
     * matchAllQuery();         匹配所有文件
     *
     * 2.组合查询 AND NOT OR
     * QueryBuilder queryBuilder = QueryBuilders.boolQuery()
    .must(QueryBuilders.termQuery("user", "kimchy"))
    .mustNot(QueryBuilders.termQuery("message", "nihao"))
    .should(QueryBuilders.termQuery("gender", "male"));

     * 3.模糊查询
     * QueryBuilder queryBuilder = QueryBuilders.fuzzyQuery("user", "kimch");
     * @param sortMap
     * @return
     * curl -XPUT localhost:9200/es/_mapping/promotion -d "{"""properties""":{"""createtime""":{"""type""":"""text""","""fielddata""":true}}}"
     */
    public SearchResponse searcherPage(String indexName, String typeName,
                                       QueryBuilder queryBuilder, Map<String, Object> sortMap) {
        SearchRequestBuilder searchRequestBuilder = transportClient.prepareSearch(indexName);
        for(Map.Entry<String,Object> entry : sortMap.entrySet()){
            if("from".equals(entry.getKey())){
                searchRequestBuilder.setFrom(Integer.valueOf(entry.getValue().toString()));
            }
            else if("size".equals(entry.getKey())){
                searchRequestBuilder.setSize(Integer.valueOf(entry.getValue().toString()));
            }else {
                if("1".equals(entry.getValue())){
                    searchRequestBuilder.addSort(entry.getKey(), SortOrder.DESC);
                }else {
                    searchRequestBuilder.addSort(entry.getKey(),SortOrder.ASC);
                }
            }
        }
        SearchResponse searchResponse = searchRequestBuilder
                .setTypes(typeName).setQuery(queryBuilder).execute()
                .actionGet();//执行查询
        return searchResponse;
    }

    /**
     * 更新索引
     * @param indexName 索引名称
     * @param typeName 索引类型
     * @param data doc
     */
    public void updateIndex(String indexName, String typeName,XContentBuilder data) {
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index(indexName);//设置索引名称
        updateRequest.type(typeName);//设置索引类型
        updateRequest.doc(data);//更新数据
        transportClient.update(updateRequest).actionGet();//执行更新
    }

    /**
     * 删除索引
     * @param indexName
     */
    public void deleteIndex(String indexName) {
        transportClient.admin().indices().prepareDelete(indexName) .execute().actionGet();
    }

    /**
     * 删除文档
     * @param indexName
     * @param typeName
     * @param id
     */
    public void deleteDoc(String indexName, String typeName, String id) {
        transportClient.prepareDelete(indexName, typeName, id).get();
    }
}
