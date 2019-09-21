package com.song.controller;

import com.song.bloomfilter.BloomFilters;
import com.song.entity.User;
import com.song.service.UserService;
import com.song.entity.EmailBean;
import com.song.utils.EmailUtil;
import com.song.utils.RedisUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by feng on 2019/5/26.
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {

    /**
     * log
     */
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @Autowired
    private EmailBean emailBean;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private BloomFilters bloomFilters;

    @RequestMapping(value = "/index")
    public String index(){
        return "user/index";
    }

    @RequestMapping(value = "/show")
    @ResponseBody
    public String show(@RequestParam(value = "name")String name){
        if(redisUtil != null && redisUtil.get(name) != null){
            return redisUtil.get(name).toString();
        }
        /**
        if (!bloomFilters.mightContain(name)){
            logger.info("bloomFilters is null for key{}",name);
            return "null";
        }
         */
        if (!bloomFilters.isExist("redis",name)){
            logger.info("bloomFilters is null for key{}",name);
            return "null";
        }
        User user = userService.findUserByName(name);
        if(null != user){
            redisUtil.set(name,user.toString());
            return user.getId()+"/"+user.getName()+"/"+user.getPassword();
        }
        else return "null";
    }

    @RequestMapping(value = "/storeCookies")
    @ResponseBody
    public int storeCookies(HttpServletRequest request, @RequestParam(value = "key")String key){
        return userService.storeCookies(request,key);
    }

    @RequestMapping(value = "/sendEmail")
    @ResponseBody
    public boolean sendEmail(HttpServletRequest request){
        boolean isSend = EmailUtil.sendEmail("这是一封测试邮件", new String[]{"809026027@qq.com"}, null, "<h3><a href='http://www.baidu.com'>百度一下，你就知道</a></h3>", null,emailBean);
        return isSend;
    }

    @RequestMapping(value = "/redis")
    @ResponseBody
    public String redis(@RequestParam(value = "key")String key,@RequestParam(value = "value")String value){
        redisUtil.set(key,value);
        return String.valueOf(redisUtil.get(key));
    }

}