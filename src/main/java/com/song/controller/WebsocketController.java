package com.song.controller;

import com.song.utils.IpAddressUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 17060342 on 2019/6/12.
 */
@Controller
@RequestMapping("websocket")
public class WebsocketController {

    @Value("${server.port}")
    private String port;

    /**
     *
     * @param topic
     * @param myname
     * @return
     */
    @RequestMapping("index/{topic}/{myname}")
    public String index(HttpServletRequest request,@PathVariable("topic")String topic, @PathVariable("myname")String myname, Model model){
        model.addAttribute("ip", IpAddressUtil.getLocalIpAddress());
        model.addAttribute("port", port);
        model.addAttribute("topic",topic);
        model.addAttribute("myname",myname);
        return "websocket/index";
    }
}
