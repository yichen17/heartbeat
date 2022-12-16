package client.demo.test;

import cn.hutool.extra.mail.MailUtil;

/**
 * @author Qiuxinchao
 * @version 1.0
 * @date 2022/12/16 18:57
 * @describe
 */
public class MailTest {

    public static void main(String[] args) {
        MailUtil.send("q07218396@163.com", "测试", "测试", false);
    }

}
