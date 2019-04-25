package com.nercl.music.service.impl;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.nercl.music.constant.CList;
import com.nercl.music.dao.ExamDao;
import com.nercl.music.entity.Exam;
import com.nercl.music.entity.ExamPaper;
import com.nercl.music.entity.ExamPoint;
import com.nercl.music.service.ExamExamPaperService;
import com.nercl.music.service.ExamPaperService;
import com.nercl.music.service.ExamPointService;
import com.nercl.music.service.ExamService;
import com.nercl.music.service.ExpertMachineWeightService;

@Service
@Transactional
public class ExamServiceImpl implements ExamService {

	@Autowired
	private ExamDao examDao;

	@Autowired
	private ExamPaperService examPaperService;

	@Autowired
	private ExamExamPaperService examExamPaperService;

	@Autowired
	private ExpertMachineWeightService expertMachineWeightService;

	@Autowired
	private ExamPointService examPointService;

	@Override
	public List<Exam> list(int page) {
		return this.examDao.list(page);
	}

	@Override
	public Exam getUsedToExam(int year) {
		return this.examDao.getUsedToExam(year);
	}

	@Override
	public Exam save(String title, String intro, Date startAt, Date endAt, String[] pids) {
		Exam exam = new Exam();
		exam.setTitle(title);
		exam.setIntro(intro);
		int year = DateUtils.toCalendar(startAt).get(Calendar.YEAR);
		int month = DateUtils.toCalendar(startAt).get(Calendar.MONTH) + 1;
		exam.setYear(year);
		exam.setMonth(month);
		exam.setStartAt(startAt.getTime());
		exam.setEndAt(endAt.getTime());
		exam.setExamStatus(Exam.ExamStatus.VALID);
		if (null != pids) {
			List<ExamPoint> points = Lists.newArrayList();
			Arrays.stream(pids).forEach(pid -> {
				ExamPoint examPoint = this.examPointService.get(pid);
				if (null != examPoint) {
					points.add(examPoint);
				}
			});
			if (!points.isEmpty()) {
				exam.setExamPoints(points);
			}
		}
		examDao.save(exam);
		return exam;
	}

	@Override
	public Exam get(String id) {
		return this.examDao.get(id);
	}

	@Override
	public boolean update(String id, String title, String intro, Date startAt, Date endAt, String[] pids) {
		Exam exam = examDao.findByID(id);
		if (null != exam) {
			exam.setIntro(intro);
			exam.setTitle(title);
			int year = DateUtils.toCalendar(startAt).get(Calendar.YEAR);
			int month = DateUtils.toCalendar(startAt).get(Calendar.MONTH) + 1;
			exam.setYear(year);
			exam.setMonth(month);
			exam.setStartAt(startAt.getTime());
			exam.setEndAt(endAt.getTime());
			if (null != pids) {
				List<ExamPoint> points = Lists.newArrayList();
				Arrays.stream(pids).forEach(pid -> {
					ExamPoint examPoint = this.examPointService.get(pid);
					if (null != examPoint) {
						points.add(examPoint);
					}
				});
				if (!points.isEmpty()) {
					exam.setExamPoints(points);
				}
			}
			examDao.update(exam);
			return true;
		}
		return false;
	}

	@Override
	public void setExamPapers(String id, String[] pids, Integer[] weights, Integer machineWeight,
	        Integer expertWeight) {
		Exam exam = examDao.findByID(id);
		if (null != exam && pids != null) {
			int count = 0;
			for (String pid : pids) {
				this.examExamPaperService.save(id, pid, weights[count]);
				ExamPaper examPaper = this.examPaperService.get(pid);
				if (null != examPaper && CList.Api.SubjectType.LOOK_SING.equals(examPaper.getSubjectType())) {
					this.expertMachineWeightService.save(id, pid, expertWeight, machineWeight);
				}
				count++;
			}
		}
	}

	@Override
	public void delete(String id) {
		Exam exam = examDao.get(id);
		if (exam != null) {
			examDao.delete(exam);
		}
	}

	@Override
	public List<Exam> getByYear(Integer year) {
		return this.examDao.getByYear(year);
	}

	@Override
	public boolean usedToExam(String id) {
		Exam exam = this.examDao.findByID(id);
		if (null != exam) {
			List<Exam> exams = this.getByYear(exam.getYear());
			if (null != exams && !exams.isEmpty()) {
				exams.forEach(e -> {
					e.setUsedToExam(false);
					this.examDao.update(e);
				});
			}
			exam.setUsedToExam(true);
			this.examDao.update(exam);
			return true;
		}
		return false;
	}

}
