package com.song.controller;

import com.song.entity.Promotion;
import com.song.service.ElasticService;
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
public class ElasticController {
    @Autowired
    private ElasticService elasticService;

    @RequestMapping("/es/promotionList/{key}")
    @ResponseBody
    public List<Promotion> findPromotionList(@PathVariable String key){
        return elasticService.findPromotionList(key);
    }

    @RequestMapping("/es/promotionList")
    @ResponseBody
    public List<Promotion> findPromotionList(){
        return elasticService.findPromotionList("");
    }

}
