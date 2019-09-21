package com.song.config.filters;

import com.song.utils.RedisUtil;
import com.song.utils.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * Created by 17060342 on 2019/6/5.
 */
@WebFilter(filterName = "sessionFilter",urlPatterns = {"/*"})
public class SessionFilter implements Filter {

    /**
     * log
     */
    private static final Logger logger = LoggerFactory.getLogger(SessionFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("SessionFilter.init");
    }

    /**
     * 因为@ServerEndpoint不支持注入，所以使用SpringUtils获取IOC实例
     */
    private RedisUtil redisUtil = SpringUtil.getBean(RedisUtil.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        logger.debug("SessionFilter.start url:[{}]",request.getRequestURL());

        if(request.getRequestURI().contains("/websocket/")) {
            //5秒内访问超过10次，定义为黑客，锁定3分钟不能访问
            if (redisUtil.get("lockHacker" + request.getRemoteHost()) != null) {
                return;
            }
            Set<String> set = redisUtil.keys("filter" + request.getRemoteHost() + "*");
            if (set != null && set.size() >= 10) {
                redisUtil.set("lockHacker" + request.getRemoteHost(), "1", 300);
                return;
            }
            redisUtil.set("filter" + request.getRemoteHost() + System.currentTimeMillis(), "1", 5);
        }

        filterChain.doFilter(servletRequest, servletResponse);
        logger.debug("SessionFilter.end");
    }

    @Override
    public void destroy() {
        logger.info("SessionFilter.destroy");
    }
}
