package com.song.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by feng on 2019/9/8.
 */
@Controller
@RequestMapping(value = "/crab")
public class CrabController {

    @RequestMapping(value = "/index")
    public String index(){
        return "crab/index";
    }
}
