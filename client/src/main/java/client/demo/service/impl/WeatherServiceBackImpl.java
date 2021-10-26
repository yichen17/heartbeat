package client.demo.service.impl;

import client.demo.dao.WeatherBackMapper;
import client.demo.model.WeatherBack;
import client.demo.service.WeatherServiceBack;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Qiuxinchao
 * @version 1.0
 * @date 2021/10/26 14:04
 * @describe  WeatherServiceBack 实现类
 */
@Slf4j
@Service
public class WeatherServiceBackImpl implements WeatherServiceBack {

    @Autowired
    private WeatherBackMapper weatherBackMapper;

    @Override
    public int insert(WeatherBack weatherBack) {
        return weatherBackMapper.insert(weatherBack);
    }
}
