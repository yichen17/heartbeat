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

    /**
     * 根据日期查询记录条数
     * @param date 指定的日期 格式为 yyyy-MM-dd
     * @return 该日期插入的天气条数
     */
    public int findByDate(String date);


}
