package com.nercl.music.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nercl.music.dao.MailRetrieveDao;
import com.nercl.music.entity.MailRetrieve;
import com.nercl.music.service.MailRetrieveService;
import com.nercl.music.util.Encryptor;

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
	public MailRetrieve getByEmail(String email) {
		return this.mailRetrieveDao.getByEmail(email);
	}

	@Override
	public void findPassword(String login, String email, String url) {

		MailRetrieve mailRetrieve = this.mailRetrieveDao.getByEmail(email);
		if (null != mailRetrieve) {
			this.mailRetrieveDao.delete(mailRetrieve);
		}
		mailRetrieve = new MailRetrieve();
		mailRetrieve.setEmail(email);
		long outTime = System.currentTimeMillis() + 1000 * 60 * 30;
		mailRetrieve.setOutTime(outTime);
		String sid = email + "&" + UUID.randomUUID().toString() + "&" + outTime;
		sid = Encryptor.encrypte(sid);
		mailRetrieve.setSid(sid);
		mailRetrieve.setUserId(login);
		this.mailRetrieveDao.save(mailRetrieve);

		String toEmail = email;
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(username);
		message.setTo(toEmail);
		message.setSubject("主题：音乐考试系统密码找回");
		String emailConten = "请点击以下链接完成密码找回：" + "\n" + url + "?email=" + email + "&sid=" + sid;
		message.setText(emailConten);
		mailSender.send(message);
	}

	@Override
	public boolean authentication(String login, String sid) {
		MailRetrieve mailRetrieve = this.mailRetrieveDao.getByLoginAndSid(login, sid);
		return null != mailRetrieve && mailRetrieve.getOutTime() > System.currentTimeMillis();
	}

	@Override
	public void deleteByEmail(String email) {
		MailRetrieve mailRetrieve = this.getByEmail(email);
		if (null != mailRetrieve) {
			this.mailRetrieveDao.delete(mailRetrieve);
		}
	}

	@Override
	public void checkMail(String userId, String url, String email) {
		MailRetrieve mailRetrieve = this.mailRetrieveDao.getByEmail(email);
		if (null != mailRetrieve) {
			this.mailRetrieveDao.delete(mailRetrieve);
		}

		mailRetrieve = new MailRetrieve();
		mailRetrieve.setUserId(userId);
		mailRetrieve.setEmail(email);
		long outTime = System.currentTimeMillis() + 1000 * 60 * 30;
		mailRetrieve.setOutTime(outTime);
		String sid = email + "&" + UUID.randomUUID().toString() + "&" + outTime;
		sid = Encryptor.encrypte(sid);
		mailRetrieve.setSid(sid);
		this.mailRetrieveDao.save(mailRetrieve);

		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(username);
		message.setTo(email);
		message.setSubject("主题：数字音乐平台邮箱验证");
		String herf = url + "?email" + email + "&sid=" + sid;
		String text = "<a herf='" + herf + "'>请点击此链接以便激活您在音乐数字平台绑定的邮箱！</a>";
		message.setText(text);
		mailSender.send(message);
	}

}
