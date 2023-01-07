package client.demo.controller;

import client.demo.constants.CommonConstants;
import client.demo.model.dto.FixWeatherDto;
import client.demo.task.WeatherTask;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
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

        if (StringUtils.isEmpty(fixWeatherDto.getDateTime()) &&
                (StringUtils.isEmpty(fixWeatherDto.getStartTime()) || Objects.isNull(fixWeatherDto.getDuration()))){
            return ReturnUtils.paramMiss("缺少入参时间");
        }

        int result = 0;
        try {
            if (!StringUtils.isEmpty(fixWeatherDto.getDateTime())){
                DateTime day = DateUtil.parse(fixWeatherDto.getDateTime(), DatePattern.NORM_DATETIME_PATTERN);
                result += fillDayWeatherDate(day);
            }
            else {
                DateTime startTime = null;
                for (int i=0; i<fixWeatherDto.getDuration(); i++){
                    if (i == 0){
                        startTime = DateUtil.parse(fixWeatherDto.getStartTime(), DatePattern.NORM_DATETIME_PATTERN);
                    }
                    else {
                        startTime = DateUtil.offsetDay(startTime, 1);
                    }
                    result += fillDayWeatherDate(startTime);
                }
            }
        }
        catch (Exception e){
            log.error("手动填充天气数据异常 {}", e.getMessage(), e);
            return ReturnUtils.fail("系统异常");
        }
        return ReturnUtils.success(String.format("记录补偿数据成功，执行异常天数%d", result));
    }

    private int fillDayWeatherDate(Date day){
        if (Objects.isNull(day)){
            log.warn("填充天气数据入参为空");
            return 0;
        }
        String dayStr = null;
        try {
            long timestamp=day.getTime();
            dayStr = DateUtil.format(day, DatePattern.NORM_DATE_PATTERN);
            String url = "https://weather.cma.cn/api/map/weather/1?t=" + timestamp;
            weatherTask.requestDataAndSave(dayStr, url);
        }
        catch (Exception e){
            log.error("填充{}天气数据执行异常{}", dayStr, e.getMessage(), e);
            return 1;
        }
        return 0;
    }

}
