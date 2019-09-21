package com.song.service;

import com.song.mongoentity.MongoPromotion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by feng on 2019/8/31.
 */
@Service
public class MongoDBService {
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 创建对象
     */
    public void savePromotion(MongoPromotion promotion) {
        mongoTemplate.save(promotion);
    }

    /**
     * 根据标题查询对象
     * @return
     */
    public MongoPromotion findPromotionByTitle(String title) {
        Query query=new Query(Criteria.where("title").is(title));
        MongoPromotion mgt =  mongoTemplate.findOne(query , MongoPromotion.class);
        return mgt;
    }

    /**
     * 根据用户名查询对象
     * @return
     */
    public List<MongoPromotion> findPromotionList() {
        Query query=new Query();
        List<MongoPromotion> mgt =  mongoTemplate.find(query , MongoPromotion.class);
        return mgt;
    }

    /**
     * 更新对象
     */
    public void updatePromotion(MongoPromotion promotion) {
        Query query=new Query(Criteria.where("id").is(promotion.getId()));
        Update update= new Update().set("title", promotion.getTitle()).set("content", promotion.getContent());
        //更新查询返回结果集的第一条
        mongoTemplate.updateFirst(query,update,MongoPromotion.class);
        //更新查询返回结果集的所有
        // mongoTemplate.updateMulti(query,update,PromotionEntity.class);
    }

    /**
     * 删除对象
     * @param id
     */
    public void deletePromotionById(Integer id) {
        Query query=new Query(Criteria.where("id").is(id));
        mongoTemplate.remove(query,MongoPromotion.class);
    }
}
