package com.nercl.music.cloud.service;

import java.util.List;

import com.nercl.music.cloud.entity.alipay.Transaction;

public interface TransactionService {

	/**
	 * 保存一条交易记录
	 * */
	void save(Transaction transaction);

	/**
	 * 根据条件查询交易记录
	 * */
	List<Transaction> findByCondition(Transaction transaction) throws Exception;

	/**
	 * 更新Transaction
	 * */
	void update(Transaction transaction);

}
