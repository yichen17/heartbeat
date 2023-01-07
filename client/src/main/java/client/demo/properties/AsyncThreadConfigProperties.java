package client.demo.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Qiuxinchao
 * @version 1.0
 * @date 2021/12/29 10:57
 * @describe 异步线程池配置参数
 */
@ConfigurationProperties(prefix = "async.thread")
@Component
@Data
public class AsyncThreadConfigProperties {
    /**
     * 核心线程数
     */
    private Integer corePoolSize2;
    /**
     * 最大核心线程数
     */
    private Integer maxPoolSize2;
    /**
     * 队列长度
     */
    private Integer queueCapacity2;
    /**
     * 线程池维护线程所允许的空闲时间，以秒为单位
     */
    private Integer keepAliveSeconds2;
    /**
     * 线程名字
     */
    private String threadNamePrefix2;
    /**
     * 等待销毁时长
     */
    private Integer awaitTerminationSeconds2;

    /**
     * 核心线程数
     */
    private Integer corePoolSize1;
    /**
     * 最大核心线程数
     */
    private Integer maxPoolSize1;
    /**
     * 队列长度
     */
    private Integer queueCapacity1;
    /**
     * 线程池维护线程所允许的空闲时间，以秒为单位
     */
    private Integer keepAliveSeconds1;
    /**
     * 线程名字
     */
    private String threadNamePrefix1;
    /**
     * 等待销毁时长
     */
    private Integer awaitTerminationSeconds1;


}
