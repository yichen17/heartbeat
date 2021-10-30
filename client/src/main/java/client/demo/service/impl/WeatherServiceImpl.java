package client.demo.service.impl;

import client.demo.dao.WeatherMapper;
import client.demo.model.Weather;
import client.demo.model.WeatherExample;
import client.demo.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Qiuxinchao
 * @version 1.0
 * @date 2021/10/26 11:18
 * @describe WeatherService 实现类
 */
@Service
public class WeatherServiceImpl implements WeatherService {

    @Autowired
    private WeatherMapper weatherMapper;

    @Override
    public int insert(Weather weather) {
        return weatherMapper.insert(weather);
    }

    @Override
    public int findByDate(String date) {
        WeatherExample example=new WeatherExample();
        example.createCriteria().andTimeEqualTo(date);
        List<Weather> weathers = weatherMapper.selectByExample(example);
        return weathers.size();
    }
}
