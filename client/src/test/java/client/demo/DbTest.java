package client.demo;

import client.demo.dao.HotNewsMapper;
import client.demo.model.HotNewsWithBLOBs;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;

/**
 * @author Qiuxinchao
 * @version 1.0
 * @date 2024/6/2 14:27
 * @describe
 */
@SpringBootTest
@ActiveProfiles("dev")
public class DbTest {

    @Autowired
    private HotNewsMapper hotNewsMapper;


    /**
     * 是否乱码看数据库连接的编码设置
     */
    @Test
    public void insertEncodeTest() {
        HotNewsWithBLOBs hotNewsWithBlobs = new HotNewsWithBLOBs();
        hotNewsWithBlobs.setChannel("哈哈哈哈");
        hotNewsWithBlobs.setNo(1);
        hotNewsWithBlobs.setUrl("http://baidu.com");
        hotNewsWithBlobs.setTime(new Date());
        hotNewsWithBlobs.setTitle("让人疯狂");
        hotNewsMapper.insert(hotNewsWithBlobs);
    }

}
