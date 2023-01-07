package client.demo.config;

import client.demo.properties.AsyncThreadConfigProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Qiuxinchao
 * @version 1.0
 * @date 2021/12/29 10:30
 * @describe 异步线程池配置
 */
@EnableAsync
@Configuration
public class AsyncThreadConfig {

    @Resource
    private AsyncThreadConfigProperties properties;

    @Bean("async")
    @Lazy
    public ThreadPoolTaskExecutor threadPoolAsync1(){

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(properties.getCorePoolSize1());
        executor.setMaxPoolSize(properties.getMaxPoolSize1());
        executor.setKeepAliveSeconds(properties.getKeepAliveSeconds1());
        executor.setQueueCapacity(properties.getQueueCapacity1());
        executor.setThreadNamePrefix(properties.getThreadNamePrefix1());
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(properties.getAwaitTerminationSeconds1());
        executor.initialize();

        return executor;
    }


}
