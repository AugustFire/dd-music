package com.nercl.music.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.nercl.music.dao.ExpertResultDao;
import com.nercl.music.entity.ExpertResult;
import com.nercl.music.entity.MachineResult;
import com.nercl.music.service.ExpertResultService;
import com.nercl.music.service.MachineResultService;
import com.nercl.music.util.DESCryption;

@Service
@Transactional
public class ExpertResultServiceImpl implements ExpertResultService {

	@Autowired
	private ExpertResultDao expertResultDao;

	@Autowired
	private DESCryption desCryption;

	@Autowired
	private MachineResultService machineResultService;

	@Override
	public ExpertResult get(String examId, String examPaperId, String examineeId, String examQuestionId,
	        String expertId) {
		return this.expertResultDao.get(examId, examPaperId, examineeId, examQuestionId, expertId);
	}

	@Override
	public boolean save(String examId, String examPaperId, String examineeId, String examQuestionId, String expertId,
	        int score, String comment) {
		ExpertResult expertResult = this.get(examId, examPaperId, examineeId, examQuestionId, expertId);
		if (null != expertResult) {
			return false;
		}
		expertResult = new ExpertResult();
		expertResult.setExamId(examId);
		expertResult.setExamPaperId(examPaperId);
		expertResult.setExamineeId(examineeId);
		expertResult.setExamQuestionId(examQuestionId);
		expertResult.setExpertId(expertId);
		expertResult.setScore(score);
		expertResult.setComment(comment);
		this.expertResultDao.save(expertResult);
		return true;
	}

	@Override
	public List<ExpertResult> list(String examId, String name, String examNo, String expertName, int page) {
		return this.expertResultDao.list(examId, name, examNo, expertName, page);
	}

	@Override
	public Integer getScore(String examId, String examPaperId, String examineeId) {
		List<ExpertResult> results = this.expertResultDao.get(examId, examPaperId, examineeId);
		if (null == results || results.isEmpty()) {
			return 0;
		}
		return results.stream().mapToInt(result -> {
			return null == result.getScore() ? 0 : (int) result.getScore();
		}).sum() / results.size();
	}

	@Override
	public List<ExpertResult> get(String examId, String examPaperId, String examQuestionId, String examineeId) {
		return this.expertResultDao.get(examId, examPaperId, examQuestionId, examineeId);
	}

	@Override
	public List<Object[]> getAvgScore() {
		return this.expertResultDao.getAvgScore();
	}

	@Override
	public List<ExpertResult> getBigDeviationResults() {
		List<Object[]> avgScores = this.getAvgScore();
		if (null == avgScores || avgScores.isEmpty()) {
			return null;
		}
		List<ExpertResult> results = this.expertResultDao.getBigDeviationResults(avgScores);
		if (null == results || results.isEmpty()) {
			return null;
		}
		results.parallelStream().forEach(result -> {
			String key = result.getExamId() + result.getExamPaperId() + result.getExamQuestionId()
		            + result.getExamineeId();
			avgScores.forEach(avgScore -> {
			    String s = avgScore[0].toString() + avgScore[1].toString() + avgScore[2].toString()
		                + avgScore[3].toString();
			    if (key.equals(s)) {
				    result.setAvgScore((Double) avgScore[4]);
			    }
		    });
			result.setQtitle(this.desCryption.decode(result.getExamQuestion().getTitle()));
			MachineResult mresult = this.machineResultService.get(result.getExamId(), result.getExamPaperId(),
		            result.getExamineeId(), result.getExamQuestionId());
			if (null != mresult) {
				Integer mscore = mresult.getScore();
				result.setMscore(mscore);
			}
		});
		return results;
	}

	@Override
	public List<ExpertResult> getDiffResults() {
		List<ExpertResult> results = Lists.newArrayList();
		List<Object[]> avgScores = this.getAvgScore();
		if (null == avgScores || avgScores.isEmpty()) {
			return null;
		}
		avgScores.forEach(avgScore -> {
			String examId = avgScore[0].toString();
			String examPaperId = avgScore[1].toString();
			String examQuestionId = avgScore[2].toString();
			String examineeId = avgScore[3].toString();
			MachineResult mresult = this.machineResultService.get(avgScore[0].toString(), avgScore[1].toString(),
		            avgScore[4].toString(), avgScore[2].toString());
			if (null != mresult && null != mresult.getScore()) {
				Integer score = mresult.getScore();
				if (score - (Double) avgScore[5] >= 15 || score - (Double) avgScore[5] < 15) {
					List<ExpertResult> ers = this.get(examId, examPaperId, examQuestionId, examineeId);
					if (null != ers && !ers.isEmpty()) {
						ers.forEach(er -> {
			                er.setQtitle(this.desCryption.decode(er.getExamQuestion().getTitle()));
			                er.setAvgScore((Double) avgScore[4]);
		                });
					}
					results.addAll(ers);
				}
			}
		});
		return results;
	}

}
