package client.demo.model.dto;

import lombok.Data;

import java.util.List;

/**
 * @author Qiuxinchao
 * @version 1.0
 * @date 2021/10/26 9:30
 * @describe 气象局结果集
 */
@Data
public class WeatherDate {
    /**
     * 最近更新时间
     */
    private String lastUpdate;
    /**
     * 日期
     */
    private String date;
    /**
     * 各城市的天气情况
     */
    private List<List<String>> city;
}
