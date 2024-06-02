package client.demo;

import client.demo.task.HotNewsTask;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;

/**
 * @author Qiuxinchao
 * @version 1.0
 * @date 2024/6/2 10:34
 * @describe
 */
@SpringBootTest
@ActiveProfiles("dev")
public class NewsTest {

    @Resource
    private HotNewsTask hotNewsTask;

    @Test
    public void fetchNews() {
        hotNewsTask.loadHotNewsBaidu();
    }

    @Test
    public void htmlAnalysis() {

    }





}
