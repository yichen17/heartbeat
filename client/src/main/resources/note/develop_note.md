## 2022-03-19
### 变更内容
百度热门信息请求方式以及格式变更
1、原先的热度(访问量)移除了，排名表示方式变化(第一个为上升的箭头原先都是数字)
2、直接通过jsoup方式请求数据被拦截了，拦截信息如下
```html
<!doctype html>
<html lang="zh-CN">
 <head> 
  <meta charset="utf-8"> 
  <title>百度安全验证</title> 
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8"> 
  <meta name="apple-mobile-web-app-capable" content="yes"> 
  <meta name="apple-mobile-web-app-status-bar-style" content="black"> 
  <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0"> 
  <meta name="format-detection" content="telephone=no, email=no"> 
  <link rel="shortcut icon" href="https://www.baidu.com/favicon.ico" type="image/x-icon"> 
  <link rel="icon" sizes="any" mask href="https://www.baidu.com/img/baidu.svg"> 
  <meta http-equiv="X-UA-Compatible" content="IE=Edge"> 
  <meta http-equiv="Content-Security-Policy" content="upgrade-insecure-requests"> 
  <link rel="stylesheet" href="https://ppui-static-wap.cdn.bcebos.com/static/touch/css/api/mkdjump_0635445.css"> 
 </head> 
 <body> 
  <div class="timeout hide"> 
   <div class="timeout-img"></div> 
   <div class="timeout-title">
    网络不给力，请稍后重试
   </div> 
   <button type="button" class="timeout-button">返回首页</button> 
  </div> 
  <div class="timeout-feedback hide"> 
   <div class="timeout-feedback-icon"></div> 
   <p class="timeout-feedback-title">问题反馈</p> 
  </div> 
  <script src="https://wappass.baidu.com/static/machine/js/api/mkd.js"></script> 
  <script src="https://ppui-static-wap.cdn.bcebos.com/static/touch/js/mkdjump_eac1ee5.js"></script>  
 </body>
</html>
```
### 解决办法
经过测试，发现是通过请求头中的cookie字段用以区分的，故手动在请求头中加入浏览器访问的token

## 百度请求地址变更记录
> https://www.baidu.com/s?wd=    // 最初版本
> https://www.baidu.com/s?ie=UTF-8&wd=%E6%91%B8%E6%91%B8%E5%A4%B4    // 这个会重定向到 http 变为下面这种
> http://www.baidu.com/s?ie=UTF-8&wd=%E6%91%B8%E6%91%B8%E5%A4%B4    // 2022-03-27 替换
