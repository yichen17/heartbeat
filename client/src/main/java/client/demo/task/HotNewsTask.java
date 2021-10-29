package client.demo.task;

import client.demo.dao.HotNewsMapper;
import client.demo.model.HotNews;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
    @Scheduled(cron = "0 0 5,16 * * ?")
    public void loadHotNewsBaidu() {
        try{
            Document doc = Jsoup.connect(baidu_url).get();
            Element hotNews = doc.getElementsByClass("opr-toplist1-table_3K7iH").get(0);
            // 遍历 有2个 一个被隐藏了
            int total,error;
            for(int i=0;i<hotNews.childNodeSize();i++){
                total=0;error=0;
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
                    HotNews recode=new HotNews();
                    recode.setChannel("0");
                    recode.setNo(Integer.parseInt(no));
                    recode.setUrl("https://www.baidu.com/"+uri);
                    recode.setTime(new Date());
                    recode.setTitle(title);
                    recode.setVisitsReal(visits);
                    Matcher matcher = VISIT_PATTERN.matcher(visits);
                    if(matcher.find()){
                        recode.setVisits(Integer.parseInt(matcher.group()));
                    }
                    int res = hotNewsMapper.insert(recode);
                    if (res == 1) {
                        total++;
                    } else {
                        error++;
                    }
                }
                System.out.println("total "+total+"  error"+error);
            }
        } catch (IOException e) {
            log.warn("jsoup 获取数据出错");
        }
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
