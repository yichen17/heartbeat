package client.demo.service;

import client.demo.model.Weather;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Qiuxinchao
 * @version 1.0
 * @date 2021/10/26 11:15
 * @describe  天气 service ，用来存储天气， 有自增主键
 */
public interface WeatherService {


    /**
     * 插入一条天气记录
     * @param weather  具体的天气信息
     * @return  影响行数
     */
    public int insert(Weather weather);

}
