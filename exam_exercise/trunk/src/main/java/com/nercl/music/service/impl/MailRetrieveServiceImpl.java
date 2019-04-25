package com.nercl.music.service.impl;

import com.nercl.music.dao.MailRetrieveDao;
import com.nercl.music.entity.user.MailRetrieve;
import com.nercl.music.service.MailRetrieveService;
import com.nercl.music.util.Encryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class MailRetrieveServiceImpl implements MailRetrieveService {

	@Autowired
	private MailRetrieveDao mailRetrieveDao;

	@Autowired
	private JavaMailSender mailSender;

	@Value("${spring.mail.username}")
	private String username;

	@Override
	public void sendAuthCode(String email) {
		MailRetrieve mailRetrieve = this.mailRetrieveDao.getByEmail(email);
		if (null != mailRetrieve) {
			this.mailRetrieveDao.delete(mailRetrieve);
		}
		mailRetrieve = new MailRetrieve();
		mailRetrieve.setEmail(email);
		long outTime = System.currentTimeMillis() + 1000 * 60 * 30;
		mailRetrieve.setOutTime(outTime);
		String sid = Encryptor.generateRandomArray(6);
		mailRetrieve.setSid(sid);
		this.mailRetrieveDao.save(mailRetrieve);

		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(username);
		message.setTo(email);
		message.setSubject("主题：音乐练习");
		message.setText("30分钟内有效：" + sid);
		mailSender.send(message);
	}

	@Override
	public boolean authentication(String email, String sid) {
		MailRetrieve mailRetrieve = this.mailRetrieveDao.getByEmailAndSid(email, sid);
		return null != mailRetrieve && mailRetrieve.getOutTime() > System.currentTimeMillis();
	}
}
