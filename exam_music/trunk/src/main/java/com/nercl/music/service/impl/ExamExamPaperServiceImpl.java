package com.nercl.music.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.nercl.music.dao.ExamPaperWeightDao;
import com.nercl.music.dao.ExpertMachineWeightDao;
import com.nercl.music.dao.impl.AbstractBaseDaoImpl;
import com.nercl.music.entity.ExamExamPaper;
import com.nercl.music.entity.ExpertMachineWeight;
import com.nercl.music.service.ExamExamPaperService;

@Service
@Transactional
public class ExamExamPaperServiceImpl extends AbstractBaseDaoImpl<ExamExamPaper, String>
        implements ExamExamPaperService {

	@Autowired
	private ExamPaperWeightDao examPaperWeightDao;

	@Autowired
	private ExpertMachineWeightDao expertMachineWeightDao;

	@Override
	public boolean save(String examId, String examPaperId, Integer weight) {
		ExamExamPaper examExamPaper = this.get(examId, examPaperId);
		if (null != examExamPaper) {
			examExamPaper.setWeight(weight);
			this.examPaperWeightDao.update(examExamPaper);
			return true;
		}
		examExamPaper = new ExamExamPaper();
		examExamPaper.setExamId(examId);
		examExamPaper.setExamPaperId(examPaperId);
		examExamPaper.setWeight(weight);
		this.examPaperWeightDao.save(examExamPaper);
		return true;
	}

	@Override
	public ExamExamPaper get(String examId, String examPaperId) {
		return this.examPaperWeightDao.get(examId, examPaperId);
	}

	@Override
	public List<ExamExamPaper> getByExam(String examId) {
		return this.examPaperWeightDao.getByExam(examId);
	}

	@Override
	public Map<String, Integer> getWeight(String examId) {
		List<ExamExamPaper> examExamPapers = this.getByExam(examId);
		Map<String, Integer> weightMap = Maps.newHashMap();
		if (null != examExamPapers && !examExamPapers.isEmpty()) {
			examExamPapers.forEach(examExamPaper -> {
				weightMap.put(String.valueOf(examExamPaper.getExamPaper().getSubjectType()), examExamPaper.getWeight());
			});
		}
		return weightMap;
	}

	@Override
	public Map<String, Integer> getExpertMachineWeight(String examId) {
		ExpertMachineWeight expertMachineWeight = this.expertMachineWeightDao.get(examId);
		Map<String, Integer> weightMap = Maps.newHashMap();
		if (null != expertMachineWeight) {
			Integer eWeight = expertMachineWeight.getExpertWeight();
			Integer mWeight = expertMachineWeight.getMachineWeight();
			weightMap.put("e_weight", null == eWeight ? 0 : eWeight);
			weightMap.put("m_weight", null == mWeight ? 0 : mWeight);
		}
		return weightMap;
	}

}
