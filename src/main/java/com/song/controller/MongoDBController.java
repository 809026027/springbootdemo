package com.song.controller;

import com.song.mongoentity.MongoPromotion;
import com.song.service.MongoDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by feng on 2019/8/22.
 */
@RestController
public class MongoDBController {
    @Autowired
    private MongoDBService mongoDBService;

    @RequestMapping("/mongoDB/promotion/{key}")
    @ResponseBody
    public MongoPromotion findPromotionList(@PathVariable String key){
        return mongoDBService.findPromotionByTitle(key);
    }

    @RequestMapping("/mongoDB/promotionList")
    @ResponseBody
    public List<MongoPromotion> findPromotionList(){
        return mongoDBService.findPromotionList();
    }

}
