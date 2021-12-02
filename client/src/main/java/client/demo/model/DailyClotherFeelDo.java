package client.demo.model;

import lombok.*;

import java.util.Date;

/**
 * @author Qiuxinchao
 * @version 1.0
 * @date 2021/12/2 16:38
 * @describe 每日着装感觉记录
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class DailyClotherFeelDo {
    /**
     * 自增id
     */
    private Long id;
    /**
     * 日期
     */
    private Date time;
    /**
     * 当天最高温度
     */
    private Integer maxTemperature;
    /**
     * 当天最低温度
     */
    private Integer minTemperature;
    /**
     * 当天出门温度
     */
    private Integer outTemperature;
    /**
     * 当天出门体感温度
     */
    private Integer kinectTemperature;
    /**
     * 当天风力
     */
    private Integer windPower;
    /**
     * 当天着装  头发
     */
    private String dressedHair;
    /**
     * 当天着装  衣服
     */
    private String dressedClother;
    /**
     * 当天着装  裤子
     */
    private String dressedTrouser;
    /**
     * 当天着装  袜子
     */
    private String dressedSock;

    /**
     * 当天天气
     */
    private String weather;
    /**
     * 着装感觉
     */
    private String feel;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;

}
