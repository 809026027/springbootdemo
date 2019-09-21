package com.feng.test;

import com.song.configuration.Entry;
import com.song.entity.Promotion;
import com.song.mapper.PromotionMapper;
import com.song.mapper.UserMapper;
import com.song.utils.DateUtil;
import com.song.utils.ElasticSearchUtil;
import com.song.utils.UUIDGenerator;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by 17060342 on 2019/6/21.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Entry.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class ElasticSearchUtilTest {
    /**
     * log日志
     */
    private static final Logger logger = LoggerFactory.getLogger(UserServiceTest.class);

    @Resource
    private ElasticSearchUtil elasticSearchUtil;

    @Resource
    private PromotionMapper promotionMapper;

    @Test
    public void testCreateIndexAndMapping() {
        //创建对象，设置集群名称和IP地址
        String indexName = "es";//索引名称
        String typeName = "promotion";//类型名称
        XContentBuilder mapping = null;
        try {
            mapping = XContentFactory.jsonBuilder()
                    .startObject()
                        .startObject("properties") //设置之定义字段
                            .startObject("title")
                                .field("type","text") //设置数据类型
                            .endObject()
                            .startObject("content")
                                .field("type","text")
                            .endObject()
                            .startObject("createtime")
                                .field("type","date")  //设置Date类型
                                .field("format","yyyy-MM-dd HH:mm:ss") //设置Date的格式
                            .endObject()
                        .endObject()
                    .endObject();
            //1.创建索引(ID可自定义也可以自动创建，此处使用自定义ID)
            elasticSearchUtil.createIndexAndMapping(indexName, typeName, mapping);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testCreateIndexAndDocument() {
        //创建对象，设置集群名称和IP地址
        String indexName = "es";//索引名称
        String typeName = "promotion";//类型名称
        XContentBuilder mapping = null;
        try {
            mapping = XContentFactory.jsonBuilder().startObject()
                    .field("title","192.138.1.2")
                    .field("content","这是JAVA有关的书籍")
                    .field("createtime", DateUtil.getFormatCurDate())
                    .endObject();
            //1.创建索引(ID可自定义也可以自动创建，此处使用自定义ID)
            elasticSearchUtil.createIndexAndDocument(indexName, typeName, UUIDGenerator.getUUID(), mapping);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSearch() {
        String indexName = "es";//索引名称
        String typeName = "promotion";//类型名称
        //2.执行查询
        //(1)创建查询条件
        QueryBuilder queryBuilder = QueryBuilders.termQuery("title", "suning");//搜索name为kimchy的数据
        //(2)执行查询
        SearchResponse searchResponse = elasticSearchUtil.searcher(indexName, typeName,
                queryBuilder);
        //(3)解析结果
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit searchHit : searchHits) {
            String id = searchHit.getId();
            Map<String,Object> result = searchHit.getSourceAsMap();
            String title = result.get("title").toString();
            String content = result.get("content").toString();
            String createtime = result.get("createtime").toString();
            System.out.println(id);
            System.out.println(title);
            System.out.println(content);
            System.out.println(createtime);
        }
    }

    @Test
    public void testUpdateIndex() {
        String indexName = "es";//索引名称
        String typeName = "promotion";//类型名称
        XContentBuilder mapping = null;
        try {
            mapping = XContentFactory.jsonBuilder()
                    .startObject()
                        .startObject("properties") //设置之定义字段
                            .startObject("title")
                                .field("type","text") //设置数据类型
                            .endObject()
                            .startObject("content")
                                .field("type","text")
                            .endObject()
                            .startObject("createtime")
                                .field("type","date")  //设置Date类型
                                .field("format","yyyy-MM-dd HH:mm:ss") //设置Date的格式
                            .endObject()
                        .endObject()
                    .endObject();
            //1.创建索引(ID可自定义也可以自动创建，此处使用自定义ID)
            elasticSearchUtil.updateIndex(indexName, typeName, mapping);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeleteIndex() {
        String indexName = "es";//索引名称
        elasticSearchUtil.deleteIndex(indexName);
    }

    @Test
    public void testInitData() {
        //创建对象，设置集群名称和IP地址
        List<Promotion> list = promotionMapper.findAllPromotions();
        String indexName = "es";//索引名称
        String typeName = "promotion";//类型名称
        XContentBuilder mapping = null;
        try {
            for(Promotion promotion : list){
                mapping = XContentFactory.jsonBuilder().startObject()
                        .field("title",promotion.getTitle())
                        .field("content",promotion.getContent())
                        .field("createtime", promotion.getCreatetime().substring(0,promotion.getCreatetime().length() - 2))
                        .endObject();
                //1.创建索引(ID可自定义也可以自动创建，此处使用自定义ID)
                elasticSearchUtil.createIndexAndDocument(indexName, typeName, promotion.getId() + "", mapping);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
