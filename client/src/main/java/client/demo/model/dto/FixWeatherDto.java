package client.demo.model.dto;

import lombok.Data;


/**
 * @author Qiuxinchao
 * @version 1.0
 * @date 2022/6/25 21:26
 * @describe 恢复天气数据 dto
 */
@Data
public class FixWeatherDto {

    /**
     * 时间点   yyyy-MM-dd hh:mm:ss
     */
    private String dateTime;

    /**
     * 校验参数，判断是否是正确的人请求的
     */
    private String key;

    /**
     * 日期  yyyy-MM-dd hh:mm:ss
     */
    private String date;



}
