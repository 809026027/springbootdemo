package com.song.aop;

import com.alibaba.fastjson.JSON;
import com.song.utils.UUIDGenerator;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.expression.Lists;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by 17060342 on 2019/6/5.
 */
@Aspect
@Component
public class LogAspect {
    private Logger logger = LoggerFactory.getLogger("LogAspect");

    ThreadLocal<String> webHead = new ThreadLocal<String>();
    ThreadLocal<String> serviceHead = new ThreadLocal<String>();

    @Pointcut("execution(public * com.song.controller.*.*(..))")
    public void webLog(){}

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String declaringTypeName = joinPoint.getSignature().getDeclaringTypeName();
        String[] arrayClassNames = declaringTypeName.split("\\.");
        String head = arrayClassNames[arrayClassNames.length - 1] + "." + joinPoint.getSignature().getName();
        webHead.set(head);
        List list = new ArrayList();
        for(Object obj : joinPoint.getArgs()){
            if((obj instanceof ServletRequest)){
                list.add(((ServletRequest) obj).getParameterMap());
            }else {
                list.add(obj);
            }
        }
        // 记录下请求内容
        logger.info(head + "() params:" + JSON.toJSONString(list));
    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        logger.info(webHead.get() + "() return:" + ret);
    }


    @Pointcut("execution(public * com.song.service.*.*(..))")
    public void serviceLog(){}

    @Before("serviceLog()")
    public void doServiceBefore(JoinPoint joinPoint) throws Throwable {
        // 接收到请求，记录请求内容
        String head = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        serviceHead.set(head);
        // 记录下请求内容
        logger.info(head + "() params:" + Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(returning = "ret", pointcut = "serviceLog()")
    public void doAfterServiceReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        logger.info(serviceHead.get() + "() return:" + ret);
    }
}
