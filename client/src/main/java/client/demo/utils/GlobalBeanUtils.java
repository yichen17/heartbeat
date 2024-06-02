package client.demo.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Qiuxinchao
 * @version 1.0
 * @date 2024/6/2 12:43
 * @describe 全局工具类
 */
public class GlobalBeanUtils {

    static {
        // gson 初始化
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.serializeNulls();
        gson = gsonBuilder.create();


    }

    public static Gson gson;


}
