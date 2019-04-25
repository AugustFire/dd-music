package com.nercl.music.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nercl.music.dao.CheckRecordDao;
import com.nercl.music.entity.CheckRecord;
import com.nercl.music.entity.ExamPaper;
import com.nercl.music.entity.ExamQuestion;
import com.nercl.music.entity.user.Login;
import com.nercl.music.service.CheckRecordService;

@Service
@Transactional
public class CheckRecordServiceImpl implements CheckRecordService {

	@Autowired
	private CheckRecordDao checkRecordDao;

	@Override
	public void addRecord(CheckRecord checkRecord) {
		checkRecordDao.save(checkRecord);

	}

	@Override
	public List<CheckRecord> getRecordsById(String id) {
		List<CheckRecord> checkRecords = checkRecordDao.getRecordsById(id);
		return checkRecords;
	}

	@Override
	public CheckRecord getCheckRecord(Login login, Object object, CheckRecord.Status status, String type,
	        String unPassReason) {
		CheckRecord checkRecord = new CheckRecord();
		checkRecord.setCheckAt(System.currentTimeMillis());
		checkRecord.setCheckUser(login.getPerson());
		checkRecord.setCheckUserId(login.getPersonId());
		if (type.equals("question")) {
			checkRecord.setExamQuestion((ExamQuestion) object);
			checkRecord.setExamQuestionId(((ExamQuestion) object).getId());
		} else if (type.equals("paper")) {
			checkRecord.setExamPaper((ExamPaper) object);
			checkRecord.setExamPaperId(((ExamPaper) object).getId());
		}
		checkRecord.setStatus(status);
		checkRecord.setUnPassReason(unPassReason);
		return checkRecord;
	}

	@Override
	public String getUnpassReason(String id, String type) {
		// TODO Auto-generated method stub
		return null;
	}

}
