package client.demo.task;

import client.demo.dao.HotNewsBackMapper;
import client.demo.dao.HotNewsMapper;
import client.demo.model.HotNewsBack;
import client.demo.model.HotNewsWithBLOBs;
import cn.hutool.extra.mail.MailUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Qiuxinchao
 * @version 1.0
 * @date 2021/10/29 14:07
 * @describe 热门消息-定时抓取
 */
@Configuration
@EnableScheduling
@Slf4j
public class HotNewsTask {

    @Autowired
    private HotNewsMapper hotNewsMapper;

    @Autowired
    private HotNewsBackMapper hotNewsBackMapper;
    /**
     * 访问量 pattern， 获取访问量中的数值
     */
    private static final Pattern VISIT_PATTERN=Pattern.compile("\\d*");
    /**
     * 百度请求url
     */
    private String baidu_url="https://www.baidu.com/s?wd=1";
    /**
     * 知乎请求 url
     */
    private String zhihu_url="https://www.zhihu.com/hot";





    /**
     *  百度 热门 新闻 记录
     */
    @Scheduled(cron = "0 0 8,19 * * ?")
//    @Scheduled(cron = "0 * * * * ?")
    public void loadHotNewsBaidu() {
        try{
            Document doc = Jsoup.connect(baidu_url).timeout(2000).get();
            Element hotNews = doc.getElementsByClass("opr-toplist1-table_3K7iH").get(0);
            // 遍历 有2个 一个被隐藏了
            int totalA,errorA,totalB,errorB;
            totalA=0;errorA=0;totalB=0;errorB=0;
            for(int i=0;i<hotNews.childNodeSize();i++){
                Element child = hotNews.child(i);
                for(int j=0;j<child.childNodeSize();j++){
                    //  有效数据节点
                    Element news = child.child(j);
                    // 访问量
                    String visits=news.child(1).textNodes().get(0).text();
                    // 标题
                    String title=news.child(0).child(1).textNodes().get(0).text();
                    // 序号
                    String no=news.child(0).child(0).textNodes().get(0).text();
                    // 具体页面 uri
                    String uri=news.child(0).getElementsByTag("a").attr("href");

                    HotNewsBack hotNewsBack=constructHotNewsMapper("0",Integer.parseInt(no),
                            "https://www.baidu.com/"+uri,new Date(),title,visits);
                    int resA = hotNewsBackMapper.insert(hotNewsBack);
                    if (resA == 1) {
                        totalA++;
                    } else {
                        errorA++;
                    }

                    HotNewsWithBLOBs hotNewsWithBLOBs=constructHotNewsMapper("0",Integer.parseInt(no),
                            "https://www.baidu.com/"+uri,new Date(),title,visits,"","","");
                    int resB = hotNewsMapper.insert(hotNewsWithBLOBs);
                    if (resB == 1) {
                        totalB++;
                    } else {
                        errorB++;
                    }

                }
            }
            log.info("totalA {},errorA {}, totalB {}, errorB {}",totalA,errorA,totalB,errorB);
            MailUtil.send("q07218396@163.com", "每天百度热门信息记录",
                    "扩展表 => success "+totalA+" error "+errorA+". 一般表 => success "+totalB+" error "+errorB, false);
        } catch (IOException e) {
            log.warn("jsoup 获取数据出错");
        }
    }

    /**
     *  构造扩展数据
     */
    public HotNewsWithBLOBs constructHotNewsMapper(String channel, Integer no, String url, Date date, String title,
                                                   String visits,String imageUrl,String extra1,String extra2){
        HotNewsWithBLOBs hotNewsWithBlobs=new HotNewsWithBLOBs();
        hotNewsWithBlobs.setChannel(channel);
        hotNewsWithBlobs.setNo(no);
        hotNewsWithBlobs.setUrl(url);
        hotNewsWithBlobs.setTime(date);
        hotNewsWithBlobs.setTitle(title);
        hotNewsWithBlobs.setVisitsReal(visits);
        hotNewsWithBlobs.setImageurl(imageUrl);
        hotNewsWithBlobs.setExtra1(extra1);
        hotNewsWithBlobs.setExtra2(extra2);
        Matcher matcher = VISIT_PATTERN.matcher(visits);
        if(matcher.find()){
            hotNewsWithBlobs.setVisits(Integer.parseInt(matcher.group()));
        }
        return hotNewsWithBlobs;
    }

    /**
     * 构造一般消息
     */
    public HotNewsBack constructHotNewsMapper(String channel,Integer no,String url,Date date,String title,String visits){
        HotNewsBack hotNewsBack=new HotNewsBack();
        hotNewsBack.setChannel(channel);
        hotNewsBack.setNo(no);
        hotNewsBack.setUrl(url);
        hotNewsBack.setTime(date);
        hotNewsBack.setTitle(title);
        hotNewsBack.setVisitsReal(visits);
        Matcher matcher = VISIT_PATTERN.matcher(visits);
        if(matcher.find()){
            hotNewsBack.setVisits(Integer.parseInt(matcher.group()));
        }
        return hotNewsBack;
    }

    /**
     *  知乎 每日 热门 数据
     */
//    @Scheduled(cron = "0 0 6,17 * * ?")
//    public void loadHotNewsZhihu(){
//        try{
//            Document doc = Jsoup.connect(zhihu_url).get();
//            Elements hotList = doc.getElementsByClass("HotList-list");
//
//        } catch (IOException e) {
//            log.warn("jsoup 获取数据出错");
//        }
//
//    }

//    public static void main(String[] args) {
//        try{
//            Document doc = Jsoup.connect("https://www.zhihu.com/hot").get();
//            Elements hotList = doc.getElementsByClass("HotList-list");
//            System.out.println("===");
//
//        } catch (IOException e) {
//            log.warn("jsoup 获取数据出错");
//        }
//    }


}
