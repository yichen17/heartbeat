package client.demo.model.dto;

import lombok.Data;

/**
 * @author Qiuxinchao
 * @version 1.0
 * @date 2021/10/26 9:30
 * @describe 气象局 查询返回结果
 */
@Data
public class WeatherResponse {
    /**
     * 描述信息
     */
    private String msg;
    /**
     * 状态码  0表示查询成功
     */
    private String code;
    /**
     * 具体数据
     */
    private WeatherDate data;
}
