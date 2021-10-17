package com.cqkk.config;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

/*自定义异常处理*/
@RestControllerAdvice
public class MyControllerAdvice {

    /*拦截全局异常 Exception.class*/
    @ExceptionHandler(value = Exception.class)
    public String errHandler(Exception e) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", -200);
        map.put("msg", e.getMessage());
        return JSONObject.toJSONString(map);
    }

    /*拦截捕捉自定义异常 MyException.class*/
    @ExceptionHandler(value = MyException.class)
    public String myErrException(MyException ex) {
        System.out.println("myErrException");
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", ex.getCode());
        map.put("msg", ex.getMsg());
        return JSONObject.toJSONString(map);
    }

}
