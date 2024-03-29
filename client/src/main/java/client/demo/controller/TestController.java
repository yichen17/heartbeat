package client.demo.controller;

import client.demo.config.CustomConfig;
import client.demo.dao.DailyClotherFeelDao;
import client.demo.utils.MapTools;
import client.demo.utils.ReturnT;
import com.alibaba.fastjson.JSONObject;
import com.yichen.handler.NettyMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.CompletableFuture;

/**
 * @author Qiuxinchao
 * @version 1.0
 * @date 2021/8/5 15:37
 * @describe 远程请求 controller ，单纯转发数据
 */
@RestController
@Slf4j
public class TestController {

    @Resource
    private DailyClotherFeelDao dao;

    @RequestMapping("/test")
    public ReturnT test(){
        log.info("test接口访问成功");
        return new ReturnT("test");
    }
    @RequestMapping("/ers")
    public ReturnT error(){
        log.info("error访问成功");
        return new ReturnT(""+1/0);
    }

    @RequestMapping("/dressed")
    public ReturnT dressed(){
//        DailyClotherFeelDo dailyClotherFeelDo=DailyClotherFeelDo.builder().dressedClother("1").dressedHair("1").dressedSock("1")
//                .dressedTrouser("1").time(new Date()).feel("刚好").maxTemperature(13).minTemperature(-2).outTemperature(0)
//                .kinectTemperature(3).windPower(1).weather("晴天").build();
//        int res= dao.insert(dailyClotherFeelDo);
//        DailyClotherFeelDo dailyClotherFeelDo=DailyClotherFeelDo.builder().id(1L).build();

//        return new ReturnT(dao.selectByCondition(dailyClotherFeelDo));
        return new ReturnT(CustomConfig.env);
    }

    @RequestMapping(value = "/get")
    public ReturnT getData(HttpServletRequest request, @RequestParam(value = "path",required = false) String path){

        if("/video".equals(path)){
            log.warn("调用video请求错误方法");
            return new ReturnT("1","调用video不能使用该方法");
        }

        try{
            log.info("接收到请求，访问ip为{}",request.getRemoteAddr());
            log.info("get方法开始调用");
            // 判定是否有现成的netty连接，如果没有则直接返回默认值
            if(MapTools.channels.size()==0){
                return new ReturnT("1","内部出错，请求拒绝");
            }
            Channel channel=MapTools.channels.iterator().next();
            // 远程调用服务
            NettyMessage nettyMessage=new NettyMessage();
            nettyMessage.setCode(2);
            nettyMessage.setData(path);
            ChannelFuture channelFuture = channel.writeAndFlush(nettyMessage);
            log.info("向管道写入数据，管道为"+channel+"数据为"+nettyMessage);
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if(channelFuture.isSuccess()){
                        MapTools.channelFutureMap.put(channel,new CompletableFuture<String>());
                        log.info("写入成功");
                    }
                    else{
                        log.info("写入失败");
                        // 打印向 channel 管道发送消息失败的内容
                        future.cause().printStackTrace();
                    }
                }
            });
            while(true){
                try{
                    CompletableFuture<String> future=(CompletableFuture<String>)MapTools.channelFutureMap.get(channel);
                    if(future==null){
                        Thread.sleep(10000);
                        continue;
                    }
                    if(future.isDone()){
                        String res= future.get();
                        ReturnT data= JSONObject.parseObject(res,ReturnT.class);
                        return data;
//                        log.info("远程调用返回的结果{},加密数据为:{}",res,data.getData().toString());
//                        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, "yichenshanliangz".getBytes());
//                        String decryptStr = aes.decryptStr(data.getData().toString(), CharsetUtil.CHARSET_UTF_8);
//                        log.info("解密后的数据结果{}",decryptStr);
//                        return new ReturnT(decryptStr.replace("|","\n"));
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                    break;
                }
            }
            return new ReturnT("1","出现异常");
        }
        catch (Exception e){
            e.printStackTrace();
            return new ReturnT("1","get future error");
        }
    }

}
