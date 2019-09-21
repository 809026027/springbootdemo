package com.song.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.song.entity.EmailBean;
import com.song.entity.Promotion;
import com.song.mapper.PromotionMapper;
import com.song.service.UserService;
import com.song.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by feng on 2019/5/26.
 */
@Controller
@RequestMapping(value = "/promotion")
public class PromotionController {
    @Autowired
    private UserService userService;

    @Autowired
    private EmailBean emailBean;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired(required = false)
    private PromotionMapper promotionMapper;

    @RequestMapping(value = "/list")
    public String list(@RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum,
                       @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize,
                       Model model){
        PageHelper.startPage(pageNum, pageSize);
        Page<Promotion> list = promotionMapper.findPagePromotions();
        model.addAttribute("list", list.getResult());
        model.addAttribute("total", list.getPages());
        model.addAttribute("pageNum", list.getPageNum());
        model.addAttribute("pageSize", list.getPageSize());
        model.addAttribute("pre", list.getPageNum() > 1 ? list.getPageNum() - 1 : 1);
        model.addAttribute("next", list.getPageNum() < list.getPages() ? list.getPageNum() + 1 : list.getPages());
        if(list.getPageNum() <= 3){
            model.addAttribute("start", 1);
            model.addAttribute("end", 5);
        }else if(list.getPageNum() >= list.getPages() - 2){
            model.addAttribute("start", list.getPages() - 5);
            model.addAttribute("end", list.getPages());
        }else{
            model.addAttribute("start", list.getPageNum() - 2);
            model.addAttribute("end", list.getPageNum() + 2);
        }

        return "promotion/list";
    }
}