package com.cqkk.config;

import com.cqkk.entity.MyLog;
import com.cqkk.mapper.LogMapper;
import com.github.pagehelper.util.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @program: lxmAndkk
 * @description: 日志配置
 * @author: luo kk
 * @create: 2021-06-17 13:40
 */
@Aspect
@Component
public class LogConfig {

    private Log log = LogFactory.getLog(LogConfig.class);

    @Resource
    private LogMapper logMapper;

    @Pointcut("execution(* com.cqkk.controller..*.*(..))")
    private void pointCut() {
    }

    @Around("pointCut()")
    public Object doArround(ProceedingJoinPoint point) {
        log.info("======接口日志记录=======");
        MyLog myLog = new MyLog();
        Object result = null;

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String stringUrl = request.getRequestURL().toString();
        //获取客户端ip
        String addr = request.getRemoteAddr();
        //Session获取登录用户
        String username = (String) request.getSession().getAttribute("username");

        //类名
        String className = point.getTarget().getClass().getName();
        log.info("className===>" + className);
        //方法
        String methodName = point.getSignature().getName();
        log.info("methodName===>" + methodName);
        //###################上面代码为方法执行前#####################
        try {
            result = point.proceed();
            log.info("result===>" + result);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        //###################下面代码为方法执行后#####################
        myLog.setUrl(stringUrl);
        myLog.setAddDate(new Date());
        myLog.setIp(addr);
        myLog.setStatus("0");
        myLog.setUrlName(className);
        myLog.setUserName(!StringUtil.isEmpty(username) ? username : "ceshiAdmin");

        logMapper.insert(myLog);
        return result;
    }
}
