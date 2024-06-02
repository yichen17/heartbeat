package client.demo;

import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Qiuxinchao
 * @version 1.0
 * @date 2024/6/2 13:18
 * @describe
 */
public class SimpleTest {

    @Test
    public void infoExtract() {
        String input = "Here is some text <!--s-data This is the data we want --> and some more text.";
        String regex = "<!--s-data(.*?)-->";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            String extractedData = matcher.group(1).trim(); // 使用 group(1) 获取括号内的内容，并去掉首尾空格
            System.out.println("Extracted Data: " + extractedData);
        }
    }

}
