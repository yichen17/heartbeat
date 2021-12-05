package client.demo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Qiuxinchao
 * @version 1.0
 * @date 2021/12/3 8:50
 * @describe 自定义配置，用于定义一些 自定义项，从配置文件中获取
 */
@Component
public class CustomConfig {
    /**
     * 启动配置文件类型， 有dev、online 等，
     * 这里用于邮件发送 只有 online 才发送，dev不发送，因为开发出错会一直发送邮件。
     */
    public static String env;

    /**
     * 这里通过 @Value 注入，注意 方法不能是静态方法。
     */
    @Value("${yichen.conf.env}")
    public void setEnv(String env) {
        CustomConfig.env = env;
    }
}
