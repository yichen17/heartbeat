package client.demo.controller;

import client.demo.model.Weather;
import client.demo.service.WeatherService;
import cn.hutool.extra.mail.Mail;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.sun.mail.util.MailSSLSocketFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.GeneralSecurityException;

/**
 * @author Qiuxinchao
 * @version 1.0
 * @date 2021/10/26 11:21
 * @describe  测试场景
 */
//@RestController
//@Slf4j
//@RequestMapping("/case")
public class CaseController {

    @Autowired
    private WeatherService weatherService;


    @RequestMapping("/mail/send")
    public String sendMailMessage() throws GeneralSecurityException {
        return  MailUtil.send("q07218396@163.com", "每日天气记录出错", "请求气象局接口出错", false);
    }


    @RequestMapping("/weather/add")
    public String weatherAdd(){
        Weather weather=new Weather();
        weather.setCode("0");
        weather.setCounty("顺义区");
        weather.setCountry("0");
        weather.setLatitude("23.11");
        weather.setLongitude("37");
        weather.setMaxTemperature("20");
        weather.setMaxWeather("多云");
        weather.setMaxWindDirection("东北风");
        weather.setMaxWindPower("威风");
        weather.setTime("2021-10-26");
        weather.setMinTemperature("5");
        weather.setMinWeather("多云");
        weather.setMinWindDirection("西北风");
        weather.setMinWindPower("狂风暴雨");
        return weatherService.insert(weather)+"";
    }

}
