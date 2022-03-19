package client.demo.service;

import client.demo.model.SysConfig;

import java.util.List;

/**
 * @author Qiuxinchao
 * @version 1.0
 * @date 2022/3/19 21:19
 * @describe 系统配置表
 */
public interface SysConfigService {

    /**
     * 根据key 获取系统配置
     * @param key 查询关键字 key
     * @return 系统配置信息
     */
    SysConfig selectByKey(String key);

}
