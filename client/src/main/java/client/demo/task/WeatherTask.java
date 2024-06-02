package client.demo.task;

import client.demo.constants.CommonConstants;
import client.demo.constants.CountryConstants;
import client.demo.model.Weather;
import client.demo.model.WeatherBack;
import client.demo.model.dto.MailNoticeDTO;
import client.demo.model.dto.WeatherDate;
import client.demo.model.dto.WeatherResponse;
import client.demo.service.WeatherService;
import client.demo.service.WeatherServiceBack;
import client.demo.utils.MailNoticeUtil;
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
 * @describe  地区天气-定时任务调度
 *    参考链接  https://www.cnblogs.com/mmzs/p/10161936.html
 */
@Configuration
@EnableScheduling
@Slf4j
public class WeatherTask {
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
    public void loanWeatherEveryday(){
        SimpleDateFormat sdf=new SimpleDateFormat(CommonConstants.DAY_DATE_FORMAT);
        String day=sdf.format(new Date());
        if(weatherService.findByDate(day)>0){
            log.info("当天的天气情况已记录");
            return ;
        }
        String fetchTime = day+" 06:00:00";
        try{
            sdf=new SimpleDateFormat(CommonConstants.NORMAL_DATE_FORMAT);
            Date date=sdf.parse(fetchTime);
            long timestamp=date.getTime();
            String url = "https://weather.cma.cn/api/map/weather/1?t=" + timestamp;
            requestDataAndSave(day, url);
        } catch (ParseException e) {
            log.warn("转换日期出错 {}",fetchTime);
            MailNoticeDTO noticeDTO = MailNoticeDTO.builder()
                    .msg(String.format("执行异常:%s", e.getMessage()))
                    .subject("每日天气记录出错").isHtml(false).build();
            MailNoticeUtil.sendToMail(noticeDTO);
        }
    }

    /**
     * 请求接口，将结果保存到数据库中
     * @param day 今天日期
     * @param url 请求三方接口地址
     */
    public void requestDataAndSave(String day, String url){
        if(weatherService.findByDate(day) > 0){
            log.info("{}当天的天气情况已记录", day);
            return ;
        }
        try{
            String body = HttpRequest.get(url).timeout(2000).execute().body();
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
                    Weather weather=getWeather(p.get(17),p.get(1), CountryConstants.CHINA,p.get(4),p.get(5),p.get(6),p.get(7),
                            p.get(9),p.get(10),day,p.get(11),p.get(12),p.get(14),p.get(15));
                    WeatherBack weatherBack=getWeatherBack(p.get(17),p.get(1),CountryConstants.CHINA,p.get(4),p.get(5),p.get(6),
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
                MailNoticeDTO noticeDTO = MailNoticeDTO.builder()
                        .msg(String.format("索引表记录:%s\n非索引表记录:%s", totalA, totalB))
                        .subject("每日天气记录完毕").isHtml(false).build();
                MailNoticeUtil.sendToMail(noticeDTO);
            }
            else{
                log.warn("未请求到数据");
                MailNoticeDTO noticeDTO = MailNoticeDTO.builder()
                        .msg("未请求到数据")
                        .subject("每日天气记录出错").isHtml(false).build();
                MailNoticeUtil.sendToMail(noticeDTO);
            }
        } catch (Exception e) {
            log.error("转换日期出错 {}",day);
            MailNoticeDTO noticeDTO = MailNoticeDTO.builder()
                    .msg(String.format("执行异常:%s", e.getMessage()))
                    .subject("每日天气记录出错").isHtml(false).build();
            MailNoticeUtil.sendToMail(noticeDTO);
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
