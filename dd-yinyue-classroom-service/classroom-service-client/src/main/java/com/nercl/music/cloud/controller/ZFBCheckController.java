package com.nercl.music.cloud.controller;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.google.common.collect.Maps;
import com.nercl.music.cloud.entity.alipay.PayResult;
import com.nercl.music.cloud.entity.alipay.Transaction;
import com.nercl.music.cloud.service.TransactionService;

@RestController
public class ZFBCheckController {

	@Autowired
	private TransactionService transactionService;
	
	/**
	 * 应用Id
	 * */
    @Value("${APP_ID}")
	private String appId;

    /**
     * 应用公钥
     * */
    @Value("${APP_PRIVATE_KEY}")
	private String appPrivateKey;

    /**
     * 传json
     * */
    @Value("${FORMAT}")
	private String format;

    /**
     * 编码，UTF-8
     * */
    @Value("${CHARSET}")
	private String charset;
 
    /**
     * 支付宝公钥
     * */
    @Value("${ALIPAY_PUBLIC_KEY}")
	private String alipayPublicKey;

    /**
     * 签名类型，一般用RSA2
     * */
    @Value("${SIGN_TYPE}")
	private String signType;
    
    
	
	@GetMapping("/zfb_pay_redirect")
	public String payResult(PayResult payResult) {
		System.out.println("++++++++++++++++++++++++++++++++++++++++支付后返回++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		Map<String, String> map = Maps.newHashMap();
		Class<? extends PayResult> payResultClass = payResult.getClass();
		Field[] declaredFields = payResultClass.getDeclaredFields();
		for(int i=0;i<declaredFields.length;i++){
			Field field = declaredFields[i];
			String methodName = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
			try {
				Method method = payResultClass.getMethod(methodName);
				map.put(field.getName(), (String)method.invoke(payResult));
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		map.put("sign",alipayPublicKey);
		try {
			AlipaySignature.rsaCheckV1(map, alipayPublicKey, charset, "RSA2");
		} catch (AlipayApiException e1) {
			e1.printStackTrace();
		}
		return "/return";

	}
	
	
    /** 
     * 支付宝支付成功后.会回调该接口 异步通知支付结果
     *  
     * @param request 
     * @return 
     * @throws UnsupportedEncodingException 
     */  
    @PostMapping("/notify")  
    public String notify(HttpServletRequest request) throws Exception {  
    	System.out.println("------------------------------异步通知支付结果-----------------------------------------------------");
        Map<String, String> params = Maps.newHashMap();
        Map<String, String[]> requestParams = request.getParameterMap();  
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {  
            String name = iter.next();  
            String[] values = requestParams.get(name);  
            String valueStr = "";  
            for (int i = 0; i < values.length; i++) {  
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";  
            }  
            // 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化  
            // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");  
            params.put(name, valueStr);  
        }  
        boolean signVerified = false;  
        try {  
            signVerified = AlipaySignature.rsaCheckV1(params, alipayPublicKey, charset,signType);  
        } catch (AlipayApiException e) {  
            e.printStackTrace();  
            return ("fail");// 验签发生异常,则直接返回失败  
        }  
        
        // 调用SDK验证签名  
        if (signVerified) {
        	Transaction transaction = new Transaction();
            String outTradeNo = params.get("outTradeNo");
            transaction.setOutTradeNo(outTradeNo);
            // 根据商户订单号可以查到唯一的记录
            List<Transaction> list = transactionService.findByCondition(transaction);
            if(!list.isEmpty()){
            	transaction = list.get(0);
            	transaction.setTransactionResult(true);
            	transaction.setTimestamp(Instant.now().getEpochSecond());
            	transactionService.update(transaction); // 更新交易记录
            }
            
        } else {  
            System.out.println("验证失败,不去更新状态");  
            return ("fail");  
        }
		return null;  
    }  
 
}
