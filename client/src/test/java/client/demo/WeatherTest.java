package client.demo;

import client.demo.task.WeatherTask;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;

/**
 * @author Qiuxinchao
 * @version 1.0
 * @date 2024/6/2 13:56
 * @describe 天气相关测试
 */
@SpringBootTest
@ActiveProfiles("dev")
public class WeatherTest {

    @Resource
    private WeatherTask weatherTask;

    @Test
    public void weatherTest() throws Exception{
        weatherTask.loanWeatherEveryday();
    }



}
