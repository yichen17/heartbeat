package client.demo.service.impl;

import client.demo.dao.SysConfigMapper;
import client.demo.model.SysConfig;
import client.demo.model.SysConfigExample;
import client.demo.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Qiuxinchao
 * @version 1.0
 * @date 2022/3/19 21:20
 * @describe 系统配置
 */
@Service
public class SysConfigServiceImpl implements SysConfigService {

    @Autowired
    private SysConfigMapper sysConfigMapper;

    @Override
    public SysConfig selectByKey(String key) {
        SysConfigExample sysConfigExample = new SysConfigExample();
        sysConfigExample.createCriteria().andOptionKeyEqualTo(key).andEnableEqualTo(0);
        List<SysConfig> sysConfigs = sysConfigMapper.selectByExample(sysConfigExample);
        if (sysConfigs == null || sysConfigs.size() == 0){
            return null;
        }
        return sysConfigs.get(0);
    }
}
