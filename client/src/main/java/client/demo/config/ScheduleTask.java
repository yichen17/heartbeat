package client.demo.config;

import client.demo.constants.CommonConstants;
import client.demo.constants.CountryConstants;
import client.demo.model.Weather;
import client.demo.model.WeatherBack;
import client.demo.model.dto.WeatherDate;
import client.demo.model.dto.WeatherResponse;
import client.demo.service.WeatherService;
import client.demo.service.WeatherServiceBack;
import cn.hutool.extra.mail.MailUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Qiuxinchao
 * @version 1.0
 * @date 2021/10/26 8:58
 * @describe  定时任务调度
 */
@Configuration
@EnableScheduling
@Slf4j
public class ScheduleTask {


    @Autowired
    private WeatherService weatherService;

    @Autowired
    private WeatherServiceBack weatherServiceBack;

    /**
     * 每天将各地的天气存入数据库，通过查询 气象局天气
     *   可用查询链接  https://weather.cma.cn/web/weather/map.html
     *      调用接口  https://weather.cma.cn/api/map/weather/1?t=1635210265612   尾部为时间戳
     */
    @Scheduled(cron = "0 0 6,18 * * ?")
    private void loanWeatherEveryday(){
        SimpleDateFormat sdf=new SimpleDateFormat(CommonConstants.DAY_DATE_FORMAT);
        String day=sdf.format(new Date());
        String fetchTime = day+" 06:00:00";
        try{
            sdf=new SimpleDateFormat(CommonConstants.NORMAL_DATE_FORMAT);
            Date date=sdf.parse(fetchTime);
            long timestamp=date.getTime();
            String body = HttpRequest.get("https://weather.cma.cn/api/map/weather/1?t=" + timestamp)
                    .timeout(2000).execute().body();
//            log.info("{} 调用气象局接口获取返回值 {}",fetchTime,body);
            WeatherResponse weatherResponse = JSONObject.parseObject(body, WeatherResponse.class);
            if("0".equals(weatherResponse.getCode())){
                WeatherDate weatherDate = weatherResponse.getData();
                // 获取各城市的当天天气情况
                List<List<String>> city = weatherDate.getCity();
                AtomicInteger totalA= new AtomicInteger();
                AtomicInteger totalB= new AtomicInteger();
                //  数组坐标  1-县(具体地方)  2-国家  4-纬度  5-经度  6-最高温度  7-天气  9-风向  10-风力
                //   11-最低温度   12-天气  14-风向   15-风力   17-码值(前缀可以区分所属城市)
                city.forEach(p->{
                    Weather weather=getWeather(p.get(17),p.get(1), CountryConstants.CHIAN,p.get(4),p.get(5),p.get(6),p.get(7),
                            p.get(9),p.get(10),day,p.get(11),p.get(12),p.get(14),p.get(15));
                    WeatherBack weatherBack=getWeatherBack(p.get(17),p.get(1),CountryConstants.CHIAN,p.get(4),p.get(5),p.get(6),
                            p.get(7),p.get(9),p.get(10),day,p.get(11),p.get(12),p.get(14),p.get(15));
                    int resA=weatherService.insert(weather);
                    int resB=weatherServiceBack.insert(weatherBack);
                    totalA.addAndGet(resA);
                    totalB.addAndGet(resB);
                    if(resA!=1||resB!=1){
                        log.warn("插入单条记录失败,normal {},back {}",resA,resB);
                    }
                });
                log.info("执行结果,normal total {}, back total {}",totalA,totalB);
                MailUtil.send("q07218396@163.com", "每日天气记录完毕", "索引表记录"+totalA+"\n非索引表记录"+totalB, false);
            }
            else{
                log.warn("未请求到数据");
                MailUtil.send("q07218396@163.com", "每日天气记录出错", "请求气象局接口出错", false);
            }
        } catch (ParseException e) {
            log.warn("转换日期出错 {}",fetchTime);
        }
    }

    /**
     * 入参生成天气对象
     */
    public static  Weather getWeather(String code,String county,String country,String latitude,String longitude,
                                       String maxTemperature,String maxWeather,String maxWindDirection,String maxWindPower,
                                       String time,String minTemperature,String minWeather,String minWindDirection,
                                       String minWindPower){
        Weather weather=new Weather();
        weather.setCode(code);
        weather.setCounty(county);
        // 国家码值
        weather.setCountry(country);
        weather.setLatitude(latitude);
        weather.setLongitude(longitude);
        weather.setMaxTemperature(maxTemperature);
        weather.setMaxWeather(maxWeather);
        weather.setMaxWindDirection(maxWindDirection);
        weather.setMaxWindPower(maxWindPower);
        // 天气日期，格式为 "2021-10-26"
        weather.setTime(time);
        weather.setMinTemperature(minTemperature);
        weather.setMinWeather(minWeather);
        weather.setMinWindDirection(minWindDirection);
        weather.setMinWindPower(minWindPower);
        return weather;
    }

    /**
     * 入参生成天气对象
     */
    public static  WeatherBack getWeatherBack(String code,String county,String country,String latitude,String longitude,
                                      String maxTemperature,String maxWeather,String maxWindDirection,String maxWindPower,
                                      String time,String minTemperature,String minWeather,String minWindDirection,
                                      String minWindPower){
        WeatherBack weather=new WeatherBack();
        weather.setCode(code);
        weather.setCounty(county);
        // 国家码值
        weather.setCountry(country);
        weather.setLatitude(latitude);
        weather.setLongitude(longitude);
        weather.setMaxTemperature(maxTemperature);
        weather.setMaxWeather(maxWeather);
        weather.setMaxWindDirection(maxWindDirection);
        weather.setMaxWindPower(maxWindPower);
        // 天气日期，格式为 "2021-10-26"
        weather.setTime(time);
        weather.setMinTemperature(minTemperature);
        weather.setMinWeather(minWeather);
        weather.setMinWindDirection(minWindDirection);
        weather.setMinWindPower(minWindPower);
        return weather;
    }


}
