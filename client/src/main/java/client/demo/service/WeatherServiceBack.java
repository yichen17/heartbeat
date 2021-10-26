package client.demo.service;

import client.demo.model.Weather;
import client.demo.model.WeatherBack;

/**
 * @author Qiuxinchao
 * @version 1.0
 * @date 2021/10/26 14:03
 * @describe WeatherService
 */
public interface WeatherServiceBack {

    /**
     * 插入一条天气记录
     * @param weather  具体的天气信息
     * @return  影响行数
     */
    public int insert(WeatherBack weatherBack);

}
