package com.song.listener;

import org.springframework.stereotype.Component;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by feng on 2019/8/18.
 */
@Component
public class RequestListener implements ServletRequestListener {

    public void requestInitialized(ServletRequestEvent sre)  {
        //将所有request请求都携带上httpSession
        HttpServletRequest httpServletRequest = ((HttpServletRequest) sre.getServletRequest());
        ((HttpServletRequest) sre.getServletRequest()).getSession().setAttribute("ip",httpServletRequest.getRemoteAddr());
        ((HttpServletRequest) sre.getServletRequest()).getSession().setAttribute("hostName",httpServletRequest.getRemoteHost());

    }
    public RequestListener() {
    }

    public void requestDestroyed(ServletRequestEvent arg0)  {
    }
}
