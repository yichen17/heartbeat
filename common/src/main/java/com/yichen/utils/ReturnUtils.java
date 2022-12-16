package com.yichen.utils;

import com.yichen.model.ReturnT;

/**
 * @author Qiuxinchao
 * @version 1.0
 * @date 2022/6/25 21:29
 * @describe 返回结果工具类
 */
public class ReturnUtils {

    public static ReturnT<?> success(String msg){
        return ReturnT.builder().msg(msg).code(ReturnT.SUCCESS_CODE).build();
    }

    public static  ReturnT<?> success(String msg, Object data){
        return ReturnT.builder().msg(msg).code(ReturnT.SUCCESS_CODE).content(data).build();
    }

    public static  ReturnT<?> paramMiss(String msg){
        return ReturnT.builder().msg(msg).code(ReturnT.SUCCESS_CODE).build();
    }

    public static ReturnT<?> fail(String msg){
        return ReturnT.builder().msg(msg).code(ReturnT.FAIL_CODE).build();
    }

}
