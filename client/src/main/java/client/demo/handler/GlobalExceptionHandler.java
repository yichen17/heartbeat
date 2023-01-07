package client.demo.handler;

import client.demo.model.dto.MailNoticeDTO;
import client.demo.utils.MailNoticeUtil;
import client.demo.utils.ReturnT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

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
        MailNoticeDTO noticeDTO = MailNoticeDTO.builder()
                .msg(String.format("%s\n%s", e.getMessage(), Arrays.toString(e.getStackTrace())))
                .subject("服务捕获异常").isHtml(false).build();
        MailNoticeUtil.sendToMail(noticeDTO);
        return new ReturnT<>("2",e.getMessage());
    }



}
