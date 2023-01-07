package client.demo.task;

import client.demo.constants.CommonConstants;
import client.demo.dao.HotNewsBackMapper;
import client.demo.dao.HotNewsMapper;
import client.demo.model.HotNewsBack;
import client.demo.model.HotNewsWithBLOBs;
import client.demo.model.dto.MailNoticeDTO;
import client.demo.service.SysConfigService;
import client.demo.utils.MailNoticeUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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

    @Autowired
    private SysConfigService sysConfigService;

    /**
     * 访问量 pattern， 获取访问量中的数值
     */
    private static final Pattern VISIT_PATTERN=Pattern.compile("\\d*");
    /**
     * 百度请求url
     */
    private static final String BAIDU_URL="https://www.baidu.com/s?wd=";
    /**
     * 知乎请求 url
     */
    private static final String ZHIHU_URL="https://www.zhihu.com/hot";

    /**
     * 查询信息
     */
    private static final String[] QUERY_THINGS = {"怎么好好学习","老是想打游戏","社会心理学","小朋友你是不是有很多问号","what are you doing","you see see you one day day"};



    /**
     *  百度 热门 新闻 记录
     */
    @Scheduled(cron = "0 0 14,6 * * ?")
//    @Scheduled(cron = "* * * * * ?")
    public void loadHotNewsBaidu() {

        Map<String,String> header = new HashMap<>(16);
        header.put("cache-control","no-cache");
        header.put("pragma","no-cache");
        header.put("referer",BAIDU_URL);
        header.put("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36");
        String cookie = sysConfigService.selectByKey(CommonConstants.BAIDU_COOKIE).getOptionValue();
        String baseBaiDuUrl = sysConfigService.selectByKey(CommonConstants.BAIDU_BASE_URL).getOptionValue();
        if (StringUtils.isEmpty(cookie)){
            log.error("查询系统配置 key {} 未查到",CommonConstants.BAIDU_COOKIE);
            return ;
        }
        header.put("Cookie",cookie);

        try{
            String queryUrl = baseBaiDuUrl + URLEncoder.encode(QUERY_THINGS[(int) (System.currentTimeMillis()%QUERY_THINGS.length)],"utf-8");
            log.info("查询热门新闻，请求头 {} ,URL {}", JSON.toJSONString(header),queryUrl);
            Document doc = Jsoup.connect(queryUrl).headers(header).timeout(2000).get();
            Element hotNews = doc.getElementsByClass("opr-toplist1-table_3K7iH").get(0);
            // 遍历 有2个 一个被隐藏了
            int totalA,errorA,totalB,errorB;
            totalA=0;errorA=0;totalB=0;errorB=0;
            for(int i=0;i<hotNews.childNodeSize();i++){
                Element child = hotNews.child(i);
                for(int j=0;j<child.childNodeSize();j++){
                    //  有效数据节点
                    Element news = child.child(j);
                    // 访问量    2022-03-19  改版没有访问量了
//                    String visits=news.child(1).textNodes().get(0).text();
                    String visits = "0w";
                    // 标题
                    String title=news.child(0).child(1).textNodes().get(0).text();
                    // 序号  2022-03-19  第一个是向上非数字
                    String no ;
                    try {
                        no = news.child(0).child(0).textNodes().get(0).text();
                    }
                    catch (Exception e){
                        no = "-1";
                    }
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
            MailNoticeDTO noticeDTO = MailNoticeDTO.builder()
                    .msg(String.format("扩展表 => success:%s,error:%s\n一般表 => success:%s,error:%s", totalA, errorA, totalB, errorB))
                    .subject("每天百度热门信息记录").isHtml(false).build();
            MailNoticeUtil.sendToMail(noticeDTO);
        } catch (IOException e) {
            log.error("jsoup 获取数据出错 {}",e.getMessage(),e);
            MailNoticeDTO noticeDTO = MailNoticeDTO.builder()
                    .msg(String.format("执行出错:%s", e.getMessage()))
                    .subject("每天百度热门信息记录").isHtml(false).build();
            MailNoticeUtil.sendToMail(noticeDTO);
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
//            Document doc = Jsoup.connect(ZHIHU_URL).get();
//            Elements hotList = doc.getElementsByClass("HotList-list");
//
//        } catch (IOException e) {
//            log.warn("jsoup 获取数据出错");
//        }
//
//    }

    public static void main(String[] args)throws  Exception{
        
        try{
            Map<String,String> header = new HashMap<>();
//            header.put("cache-control","no-cache");
//            header.put("pragma","no-cache");
//            header.put("referer",BAIDU_URL);
//            header.put("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36");
//            header.put("Cookie","BIDUPSID=6295B6CB9DC0630E10BCB844423D665F; PSTM=1600236153; __yjs_duid=1_d30495de07c00cbdde196fa89da1a3381619846723067; MCITY=-%3A; BDUSS=m1LMUNsSHlLaVdiRmhPZTVQMzV0Zkg4VEZyTWN4OHc4djlQazlTWH4xT3dwSk5oRVFBQUFBJCQAAAAAAAAAAAEAAABoTb6geMnBwcF4tcezoXgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAALAXbGGwF2xhZ; BDUSS_BFESS=m1LMUNsSHlLaVdiRmhPZTVQMzV0Zkg4VEZyTWN4OHc4djlQazlTWH4xT3dwSk5oRVFBQUFBJCQAAAAAAAAAAAEAAABoTb6geMnBwcF4tcezoXgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAALAXbGGwF2xhZ; BAIDUID=1F6B36221D29C43B72FE70A549AD3B55:FG=1; BDORZ=B490B5EBF6F3CD402E515D22BCDA1598; BDSFRCVID_BFESS=jlCOJeC62uJgHl6Dbfqvb_-jhHIMdMOTH6aomZEOJbDFrz9q0T_UEG0P5M8g0KuMWSJQogKK0mOTHvtF_2uxOjjg8UtVJeC6EG0Ptf8g0f5; H_BDCLCKID_SF_BFESS=tb-j_D--JI_3DR7mbDTBhnLO-fPX5-RLfKQQ5h7F5l8-h45_bU7Sjx0VblQq5q4L2Db7al6eBl7xOKQphPcAQf4p-4OlbJI8-RRnMM5N3KJmMqC9bT3v5fui0P6I2-biWbRL2MbdbtbP_IoG2Mn8M4bb3qOpBtQmJeTxoUJ25DnJhbLGe4bK-Tr3jGKOtfK; BAIDUID_BFESS=81DD48729AEDF1E47857A32C4EFFCE13:FG=1; H_PS_PSSID=35836_35106_31253_35979_36087_34584_36140_36122_36075_35994_35320_26350_36115_36102; delPer=0; PSINO=1; BA_HECTOR=802l01a5a4818ga4u31h3gsdn0r");

            header.put("cache-control","no-cache");
            header.put("pragma","no-cache");
            header.put("referer",BAIDU_URL);
            header.put("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36");
//            header.put("Cookie",cookie);

            Document doc = Jsoup.connect(BAIDU_URL).headers(header).timeout(5000).get();
            Element hotNews = doc.getElementsByClass("opr-toplist1-table_3K7iH").get(0);
            System.out.println(hotNews);

//            Document doc = Jsoup.connect("https://www.zhihu.com/hot").get();
//            Elements hotList = doc.getElementsByClass("HotList-list");
//            System.out.println("===");

        } catch (IOException e) {
            System.out.println(e.getMessage());
            log.error("jsoup 获取数据出错 {} ",e.getMessage(),e);
        }
    }


}
