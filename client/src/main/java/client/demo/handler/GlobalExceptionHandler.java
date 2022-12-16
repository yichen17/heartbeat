package client.demo.handler;

import client.demo.config.CustomConfig;
import client.demo.constants.CommonConstants;
import client.demo.utils.ReturnT;
import cn.hutool.extra.mail.MailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * @author Qiuxinchao
 * @version 1.0
 * @date 2021/8/30 10:21
 * @describe 全局异常拦截器
 */

@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ReturnT<String> exceptionHandler(HttpServletRequest request, Exception e){
        log.error("全局捕获Exception异常，异常信息为  ==> {} \n 堆栈信息  ===> ", e.getMessage(), e);
        if(CommonConstants.PROFILE_ONLINE.equals(CustomConfig.env)){
            MailUtil.send("q07218396@163.com", "服务捕获异常",
                    e.getMessage()+"\n"+Arrays.toString(e.getStackTrace()), false);
        }
        return new ReturnT<>("2",e.getMessage());
    }



}
