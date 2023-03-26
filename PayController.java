package edu.canteen.order.system.controller;


import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import edu.canteen.order.system.pojo.Orders;
import edu.canteen.order.system.service.OrdersService;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 */
@Controller
@RequestMapping("/edu/pay")
public class PayController {

	// 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2016101300673774";

    // 商户私钥，您的PKCS8格式RSA2私钥    （自己生成的私钥，记得去除空格，换行符）
    public static String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCdUM+UP99FtGYJRpN83dDG9M8TG73JabvGPAK2aqd5DPswhJKy77LScTJVxz8P6sz7D/kWGuwzYAbLLaqIYSsD3xh9xXcLp/Wz9GaKvmEkTPjCfmFsE3U5l62PVSntcSAeAOhgG0vlnbiQX366vQpkYXLw2WEVmZfcs+xsOalV4BMEAnUSy19Km9CGTE7c7uEreVlZG1yKnfHPXs82m+ZK++7cHra3YF2H3lKhM0D6q71n7IsH2xu2YQx0UFmb1Q2ToQLAyTqMpuki4Oy/9TG3rZBPc/mFOGeUvQWs5oE/BEDK4ISc8JyZ9FCPJDcppzL7QXVmPa75gP/5nM8Sz+rjAgMBAAECggEAGherNlWZvL/9JJC2KcHIvRchuXOieOO2p1yQvpzWBjfm+dcjVdZ3/HuV87BGf2dcdE/j6oywnJzSw6RhYplrPwsjzsiPxDkpgZ2J3H1TM2go4ml/0yMvEExeFyXy/FUFFLTl1frxDbapRBjythpgvOGE5PnJJoIBNDQLhK/TrFBoQ/YCID6DPFRzsHCSsTNOc7rg03xH8+YCBBOY/j5Mf7MeGcVJ4TcwZibBEWAs9id+9u5ztfXHcVPwVC212dmxhuJtABdfbPfwGXUDQRJ5REKzjojepxFA6buXuptc5TuQRCMx17mVHDocjadnrRU17ZokPlLwb+OSazMaNS9eAQKBgQDzlXC6V4iDivHumJuJxXVEJko+oOVhZqwClblX0mxaue2gbIpksdxORm3/MjglzLr5AZJsuBFIIiX1Kh8CY0K6z9DOlKx3IftBZLoBQkpfA8K3YX1qmezh8QayBdmgpw/Z59fJqtjSAofkPi3Ap1dncjxsZnYdLLRBGNpVxpHWgQKBgQClVaVSZPR+Iw5x8hyneEUaSZeQUzKmZkQJFQ6excA5tAPCnajdeqI2yjI3c6lsVCEpbH0xwVEGC+J//rv2J2PZIjUBSjOqlfjstutRp0PNgRhOIzm+urAV49p3fhfFBlnrNCgZyaVQRZQQkR3nNoDmFOlLr00iGfPtZY1DK7R3YwKBgQDR+Sp5l2c4bZfr42WAt9REcPVjU3MoKUdx2vswd1WsR8ZNmgMali+g0hmT+QhpnTg32mEMkwditP0ff7XHayVkwCvxSDv7XjGmx1uIJkrR6P368nEYaaPHiOwKnfxh/mEtqCtdzoalUkUuPWNXRWmU5OatUw3STGz+CJx9gHTLAQKBgB9p2nOBQwMEXiZAPjmDnH2WJfoCFMmQ/LRND+4S3KcaEOzVT9RlDJUkbc4jZ7lxcquQNWlStbZUY8C2AbiPW2VSHogUNSTgQd+YEbX99eAB+0A9aSdYCUTfeg7iIloJDuSlkEj2CzbpQWy0Zd4cePk+V+VNdXZZQAOHHAP7NdrJAoGAUBzIKNsIzQZNbnXwSDwRzH+ClZfJpwglOorbG5KagRsGk11iig7tNtjxEWQeptgclLqjZGcbpJ048nl7uFtiXkuQbGOL/KAWpoGddpJjTaNEMrJz+m+ajGBY/HBh33FsQ5e5CPnZEEyQYGyiaZsdg7TBu5v9Ia/M390ip4wKxeo=";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlTIQ82SXNi7spPASdlJWic76eRZr4v1UinDEsJxs3JlO36KWrepT8L4ZPaFAeBqhSI31lMpGyXXp6VILc/4cpkNLs6WXh/7mLw4lIcWAAnrIrNSCkvyl4mOuO+xGFhqsj34XiSN3OVaTCMPLd1G455cZKMQVwPIZtmmT+r5dVIJreblBniKNCyh53wuE0+/o7f//3Y1SwAunrjAadVChB67/wpkAsSTC2JkE1FWMuybJ6Je17jwReiIchHa6bklvypm5gzQ5OAqdiX7naWhRZXC/lOFfK0+mtJzeBU+5gLy0KubjuA5FN9q8PDKZeYzExN9c7ehPHIF6deVsn1uWDwIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://localhost:8080/edu/pay/alipay/notify_url";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "http://localhost:8080/edu/pay/alipay/return_url";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    @Autowired
    private OrdersService ordersService;
    
   
    @RequestMapping("/toPay")
    public void toPay(HttpServletRequest request,HttpServletResponse response,Integer id) throws Exception{
    	Orders orders = ordersService.getById(id);
    	
    	//获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl,app_id,merchant_private_key, "json",charset,alipay_public_key,sign_type);
        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(return_url);
        alipayRequest.setNotifyUrl(notify_url);
        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = orders.getOrderNo();
        System.out.println("out_trade_no="+out_trade_no);
        //付款金额，必填
        String total_amount = orders.getTotalPrice().toString();
        //订单名称，必填
        String subject = out_trade_no;
        JSONObject param = new JSONObject();
        
        param.put("out_trade_no", out_trade_no);
        param.put("total_amount", total_amount);
        param.put("subject", subject);
        param.put("product_code", "FAST_INSTANT_TRADE_PAY");
        String jsonString = JSON.toJSONString(param);
        System.err.println(jsonString);
        alipayRequest.setBizContent(jsonString);
       
        //请求
        String result = "";
        try {
            result = alipayClient.pageExecute(alipayRequest).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
       
        //输出
        response.setContentType("text/html;charset=" + charset);
        response.getWriter().write(result);//
        response.getWriter().flush();
        response.getWriter().close();
    }
   
    //回调验证.验证成功后可以返回自己想要跳转的页面
    @RequestMapping("/alipay/return_url")
    public String returnUrl(HttpServletRequest request,@RequestParam("out_trade_no") String orderNo) throws UnsupportedEncodingException, AlipayApiException{
       // 回写订单状态
    	QueryWrapper<Orders> queryWrapper = new QueryWrapper<Orders>();
    	queryWrapper.eq("order_no", orderNo);
    	Orders one = ordersService.getOne(queryWrapper);
    	one.setStatus(3);
    	ordersService.updateById(one);
    	request.setAttribute("orders", one);
    	return "index/pay-success";
    }
	
}
