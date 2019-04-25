package com.nercl.music.cloud.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PhoneCodeServiceImpl implements PhoneCodeService {

	@Override
	public boolean isSendedCodeInOneMinute(String phone) {
		// TODO Auto-generated method stub
		return false;
	}

}
