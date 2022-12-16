package client.demo.controller;

import client.demo.constants.CommonConstants;
import client.demo.model.dto.FixWeatherDto;
import client.demo.task.WeatherTask;
import com.yichen.model.ReturnT;
import com.yichen.utils.ReturnUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * @author Qiuxinchao
 * @version 1.0
 * @date 2022/6/23 22:54
 * @describe 修补错误数据controller
 */
@RestController
@Slf4j
@RequestMapping("/fix")
public class FixController {

    @Autowired
    private WeatherTask weatherTask;

    @RequestMapping("/test")
    public String test(){
        return "ok";
    }

    @PostMapping(value = "/fixWeatherData", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ReturnT<?> fixWeatherData(@RequestBody FixWeatherDto fixWeatherDto){
        if (!CommonConstants.REQUEST_KEY.equals(fixWeatherDto.getKey())){
            return ReturnUtils.fail("密钥验证失败，请勿随意请求");
        }

        if (StringUtils.isEmpty(fixWeatherDto.getDateTime()) || StringUtils.isEmpty(fixWeatherDto.getDate())){
            return ReturnUtils.paramMiss("缺少入参时间");
        }

        try {
            SimpleDateFormat sdf=new SimpleDateFormat(CommonConstants.NORMAL_DATE_FORMAT);
            Date date=sdf.parse(fixWeatherDto.getDateTime());
            long timestamp=date.getTime();
            String url = "https://weather.cma.cn/api/map/weather/1?t=" + timestamp;
            weatherTask.requestDataAndSave(fixWeatherDto.getDate(), url);
        }
        catch (ParseException e){
            log.error("转换时间出错，请求入参格式不符合要求 {}", e.getMessage(), e);
            return ReturnUtils.fail("入参格式不符合要求");
        }
        catch (Exception e){
            log.error("系统出现异常 {}", e.getMessage(), e);
            return ReturnUtils.fail("系统异常");
        }
        return ReturnUtils.success("记录补偿数据成功");
    }
}
