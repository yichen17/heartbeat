package client.demo.dao;

import client.demo.model.DailyClotherFeelDo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * @author Qiuxinchao
 * @version 1.0
 * @date 2021/12/2 16:57
 * @describe
 */
@Mapper
public interface DailyClotherFeelDao {

    /**
     * 插入记录
     * @param dailyClotherFeelDo 数据集
     * @return 影响行数
     */
    int insert(DailyClotherFeelDo dailyClotherFeelDo);

    /**
     * 条件查询， 有效查询   id、天气、风力、着装内容(头发，衣服，裤子，袜子)、时间
     * @param dailyClotherFeelDo 数据集
     * @return  匹配记录
     */
    List<DailyClotherFeelDo> selectByCondition(DailyClotherFeelDo dailyClotherFeelDo);

}
