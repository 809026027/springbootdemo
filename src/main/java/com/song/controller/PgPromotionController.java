package com.song.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.song.pgmapper.PgPromotionMapper;
import com.song.pgentity.PgPromotion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by feng on 2019/5/26.
 */
@Controller
@RequestMapping(value = "/pgPromotion")
public class PgPromotionController {

    @Autowired
    private PgPromotionMapper pgPromotionMapper;

    @RequestMapping(value = "/list")
    @ResponseBody
    public String list(@RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum,
                       @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize,
                       Model model){
        PageHelper.startPage(pageNum, pageSize);
        Page<PgPromotion> list = pgPromotionMapper.findPagePgPromotions();
        return JSON.toJSONString(list);
    }

    @RequestMapping(value = "/add")
    @ResponseBody
    public String list(@RequestParam(value = "title",defaultValue = "title")String title,
                       @RequestParam(value = "content",defaultValue = "content")String content){
        int result = pgPromotionMapper.addPgPromotion(title,content);
        return JSON.toJSONString(result);
    }
}