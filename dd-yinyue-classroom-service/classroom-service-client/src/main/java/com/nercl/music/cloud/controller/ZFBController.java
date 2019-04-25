package com.nercl.music.cloud.controller;

import java.time.Instant;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.nercl.music.cloud.entity.alipay.Transaction;
import com.nercl.music.cloud.entity.alipay.TransactionType;
import com.nercl.music.cloud.service.TransactionService;

@RestController
public class ZFBController {

	@Autowired
	private TransactionService transactionService;

	/**
	 * 应用Id
	 */
	@Value("${APP_ID}")
	private String appId;

	/**
	 * 应用公钥
	 */
	@Value("${APP_PRIVATE_KEY}")
	private String appPrivateKey;

	/**
	 * 传json
	 */
	@Value("${FORMAT}")
	private String format;

	/**
	 * 编码，UTF-8
	 */
	@Value("${CHARSET}")
	private String charset;

	/**
	 * 支付宝公钥
	 */
	@Value("${ALIPAY_PUBLIC_KEY}")
	private String alipayPublicKey;

	/**
	 * 签名类型，一般用RSA2
	 */
	@Value("${SIGN_TYPE}")
	private String signType;

	/**
	 * 新增一个交易
	 * 
	 * @return
	 */
	@RequestMapping("/trade")
	public ModelAndView newTrade(Transaction transaction, HttpServletResponse response) {
		long now = Instant.now().getEpochSecond();
		ModelAndView mv = new ModelAndView();
		// 获得初始化的AlipayClient
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipaydev.com/gateway.do", appId,
				appPrivateKey, format, charset, alipayPublicKey, signType);

		AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();// 创建API对应的request
		alipayRequest.setReturnUrl("http://lenovo-pc:8762/zfb_pay_redirect");
		alipayRequest.setNotifyUrl("http://lenovo-pc:8762/notify");// 在公共参数中设置回跳和通知地址
		alipayRequest.setBizContent("{" + "    \"out_trade_no\":\"2015032001010100921219211\","
				+ "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," + "    \"total_amount\":88.88,"
				+ "    \"subject\":\"Iphone6 16G\"," + "    \"body\":\"Iphone6 16G\","
				+ "    \"passback_params\":\"merchantBizType%3d3C%26merchantBizNo%3d2016010101111\","
				+ "    \"extend_params\":{" + "    \"sys_service_provider_id\":\"2088511833207846\"" + "    }" + "  }");// 填充业务参数
		String form = "";
		try {
			form = alipayClient.pageExecute(alipayRequest).getBody(); // 调用SDK生成表单
			// 保存交易信息
			transaction.setTimestamp(now);
			transaction.setTransType(TransactionType.pay);
			transaction.setTransactionResult(true);
			transactionService.save(transaction);

			// 将支付宝响应返回页面
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write(form);// 直接将完整的表单html输出到页面
			response.getWriter().flush();
			response.getWriter().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		mv.setViewName("/alipay");
		return mv;
	}

	@PostMapping("/refund")
	public String refund(Transaction transaction, HttpServletResponse response) {
		long now = Instant.now().getEpochSecond();
		// 获得初始化的AlipayClient
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipaydev.com/gateway.do", appId,
				appPrivateKey, format, charset, alipayPublicKey);
		AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();
		alipayRequest.setBizContent("{" + "    \"out_trade_no\":\"2015032001010100909\"," + "    \"trade_no\":\"\","
				+ "    \"refund_amount\":88.88," + "    \"refund_reason\":\"正常退款\","
				+ "    \"out_request_no\":\"HZ01RF001\"," + "    \"operator_id\":\"OP001\","
				+ "    \"store_id\":\"NJ_S_001\"," + "    \"terminal_id\":\"NJ_T_001\"" + "  }");
		AlipayTradeRefundResponse alipayResponse = null;
		try {
			alipayResponse = alipayClient.execute(alipayRequest);
			if (alipayResponse.isSuccess()) {
				transaction.setTransactionResult(true);
				System.out.println("调用成功");
			} else {
				transaction.setTransactionResult(false);
				System.out.println("调用失败");
			}
			transaction.setTimestamp(now);
			transaction.setTransType(TransactionType.refund);
			transactionService.save(transaction);
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}

		return "success";
	}

	@GetMapping("/trade_query")
	public String tradeQuery() {
		// 获得初始化的AlipayClient
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipaydev.com/gateway.do", appId,
				appPrivateKey, format, charset, alipayPublicKey, signType);
		AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
		request.setBizContent(
				"{" + "		\"out_trade_no\":\"2015032001010100909\"," + "		\"trade_no\":\"\"" + "  }");
		AlipayTradeQueryResponse response = null;
		try {
			Transaction transaction = new Transaction();
			response = alipayClient.execute(request);
			if (response.isSuccess()) {
				response.getOutTradeNo();
				transaction.setTransactionResult(true);
				System.out.println("调用成功");
			} else {
				transaction.setTransactionResult(false);
				System.out.println("调用失败");
			}
			long now = Instant.now().getEpochSecond();
			transaction.setTimestamp(now);
			transaction.setTransType(TransactionType.query);
			transactionService.save(transaction);
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}

		return null;
	}

}
