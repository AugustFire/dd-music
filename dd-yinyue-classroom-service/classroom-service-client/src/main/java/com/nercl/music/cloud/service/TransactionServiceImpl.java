package com.nercl.music.cloud.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nercl.music.cloud.dao.TransactionDao;
import com.nercl.music.cloud.entity.alipay.Transaction;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	private TransactionDao transactionDao;

	@Override
	public void save(Transaction transaction) {
		transactionDao.save(transaction);
	}

	@Override
	public List<Transaction> findByCondition(Transaction transaction) throws Exception {
		return transactionDao.findByConditions(transaction);
	}

	@Override
	public void update(Transaction transaction) {
		transactionDao.update(transaction);
		
	}

}
