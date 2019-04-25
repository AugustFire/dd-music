package com.nercl.music.cloud.entity.alipay;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "transaction")
public class Transaction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7625559759808161540L;

	/**
	 * 主键Id
	 */
	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	/**
	 * 交易金额(单位分)
	 */
	private int amount;

	/**
	 * 交易时间戳
	 */
	private Long timestamp;

	/**
	 * 订单支付时传入的商户订单号，和支付宝交易号不能同时为空。 trade_no,out_trade_no如果同时存在优先取trade_no
	 */
	@Column(name = "out_trade_no")
	private String outTradeNo;

	/**
	 * 支付宝交易号，和商户订单号不能同时为空
	 */
	@Column(name = "trade_no")
	private String trade_no;

	/**
	 * 产品编码
	 */
	@Column(name = "product_code")
	private String productCode;

	/**
	 * 交易摘要
	 */
	private String subject;

	/**
	 * 交易详情
	 */
	private String body;

	/**
	 * 交易类型 支付-pay 退款-refund 查询-query 退款查询-refundQuery 交易关闭-close
	 */
	private TransactionType transType;

	/**
	 * 交易结果
	 */
	private Boolean transactionResult;

	public String getId() {
		return id;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getTrade_no() {
		return trade_no;
	}

	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public TransactionType getTransType() {
		return transType;
	}

	public void setTransType(TransactionType transType) {
		this.transType = transType;
	}

	public Boolean getTransactionResult() {
		return transactionResult;
	}

	public void setTransactionResult(Boolean transactionResult) {
		this.transactionResult = transactionResult;
	}

}
