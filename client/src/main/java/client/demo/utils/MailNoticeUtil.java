package client.demo.utils;

import client.demo.config.CustomConfig;
import client.demo.constants.CommonConstants;
import client.demo.model.dto.MailNoticeDTO;
import cn.hutool.extra.mail.MailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author Qiuxinchao
 * @version 1.0
 * @date 2023/1/7 20:27
 * @describe 发邮件通知
 */
@Slf4j
public class MailNoticeUtil {

    private static final String MAIL_ADDRESS = "q07218396@163.com";


    public static void sendToMail(MailNoticeDTO mailNoticeDTO){
        if (!CommonConstants.PROFILE_ONLINE.equals(CustomConfig.env)){
            log.warn("非生产环境，不发邮件");
            return;
        }
        String subject = String.format("[%s] %s", CustomConfig.env, mailNoticeDTO.getSubject());
        String msg = String.format("[%s] => %s", CustomConfig.env, mailNoticeDTO.getMsg());
        MailUtil.send(MAIL_ADDRESS, subject, msg, mailNoticeDTO.getIsHtml());
    }

}
