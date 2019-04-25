package com.nercl.music.cloud.entity.alipay;

/**
 * * 交易类型 支付-pay 退款-refund 查询-query 退款查询-refundQuery 交易关闭-close
 */
public enum TransactionType {

	/**
	 * 支付
	 */
	pay("支付"),

	/**
	 * 退款
	 */
	refund("退款"),

	/**
	 * 交易查询
	 */
	query("交易查询"),

	/**
	 * 退款查询
	 */
	refundQuery("退款查询"),

	/**
	 * 交易关闭
	 */
	close("交易关闭");

	private String desc;

	private TransactionType(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
