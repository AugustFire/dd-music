package com.nercl.music.service.impl;

import com.google.common.base.Strings;
import com.nercl.music.dao.DownloadQuestionRecordDao;
import com.nercl.music.dao.LoginRecordDao;
import com.nercl.music.dao.LogoutRecordDao;
import com.nercl.music.entity.behavior.DownloadQuestionRecord;
import com.nercl.music.entity.behavior.LoginRecord;
import com.nercl.music.entity.behavior.LogoutRecord;
import com.nercl.music.entity.question.ExamQuestion;
import com.nercl.music.service.BehaviorService;
import com.nercl.music.service.ExamQuestionService;
import com.nercl.music.util.DESCryption;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Transactional
public class BehaviorServiceImpl implements BehaviorService {

	@Autowired
	private LoginRecordDao loginRecordDao;

	@Autowired
	private LogoutRecordDao logoutRecordDao;

	@Autowired
	private DownloadQuestionRecordDao downloadQuestionRecordDao;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ExamQuestionService examQuestionService;

	@Autowired
	private DESCryption desCryption;

	private static final String TAOBAO_IP_SERVICE = "http://ip.taobao.com/service/getIpInfo.php?ip={ip}";

	@Override
	public void saveLogin(String personId, String ip) {
		LoginRecord loginRecord = new LoginRecord();
		loginRecord.setCreateAt(System.currentTimeMillis());
		loginRecord.setIp(ip);
		loginRecord.setPersonId(personId);
//		if (!Strings.isNullOrEmpty(ip)) {
//			String output = restTemplate.getForObject(TAOBAO_IP_SERVICE, String.class, ip);
//			if (!Strings.isNullOrEmpty(output)) {
//				JSONObject jsonObject = JSONObject.fromObject(output);
//				JSONObject data = jsonObject.getJSONObject("data");
//				String province = data.getString("region");
//				String city = data.getString("city");
//				loginRecord.setProvince(province);
//				loginRecord.setCity(city);
//			}
//		}
		this.loginRecordDao.save(loginRecord);
	}

	@Override
	public void saveLogout(String personId) {
		LoginRecord loginRecord = this.loginRecordDao.getNewest(personId);
		if (null == loginRecord) {
			return;
		}
		Long now = System.currentTimeMillis();
		Long d = (now - loginRecord.getCreateAt()) / 1000;
		Integer duration = d.intValue();
		LogoutRecord logoutRecord = new LogoutRecord();
		logoutRecord.setCreateAt(now);
		logoutRecord.setDuration(duration);
		logoutRecord.setPersonId(personId);

		this.logoutRecordDao.save(logoutRecord);
	}

	@Override
	public void saveDownload(String personId, String examQuestionId) {
		DownloadQuestionRecord record = new DownloadQuestionRecord();
		record.setCreateAt(System.currentTimeMillis());
		record.setExamQuestionId(examQuestionId);
		record.setPersonId(personId);
		ExamQuestion question = examQuestionService.get(examQuestionId);
		if (null != question) {
			try {
				record.setTitle(this.desCryption.decode(question.getTitle()));
			} catch (Exception e) {
				record.setTitle(question.getTitle());
			}
		}
		this.downloadQuestionRecordDao.save(record);
	}

	@Override
	public List<LoginRecord> getLoginRecords(String name, String email, int page) {
		return this.loginRecordDao.get(name, email, page);
	}

	@Override
	public List<LogoutRecord> getLogoutRecords(String name, String email, int page) {
		List<LogoutRecord> records = this.logoutRecordDao.get(name, email, page);
		records.forEach(r -> r.setDurationStr(this.convertTime(r.getDuration())));
		return records;
	}

	@Override
	public List<DownloadQuestionRecord> getDownloadRecords(String name, String email, int page) {
		return this.downloadQuestionRecordDao.get(name, email, page);
	}

	private String convertTime(Integer duration) {
		if (null == duration || duration <= 0) {
			return "";
		}
		Integer hour = duration / 3600;
		Integer minute = duration % 3600 / 60;
		Integer second = duration % 60;
		String ret = "";
		if (hour > 0) {
			ret = hour + "时";
		}
		if (minute > 0) {
			ret += minute + "分";
		}
		if (second > 0) {
			ret += second + "秒";
		}

		return ret;
	}
}
