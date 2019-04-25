package com.nercl.music.service.impl;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nercl.music.constant.CList;
import com.nercl.music.entity.AnswerRecord;
import com.nercl.music.entity.ExamPaper;
import com.nercl.music.entity.ExamPaperQuestion;
import com.nercl.music.entity.ExamQuestion;
import com.nercl.music.service.AnswerRecordService;
import com.nercl.music.service.ExamPaperQuestionService;
import com.nercl.music.service.ExamPaperService;
import com.nercl.music.service.ExamQuestionService;
import com.nercl.music.service.StatisticService;
import com.nercl.music.util.DESCryption;

@Service
@Transactional
public class StatisticServiceImpl implements StatisticService {

	@Autowired
	private AnswerRecordService answerRecordService;

	@Autowired
	private ExamPaperService examPaperService;

	@Autowired
	private ExamQuestionService examQuestionService;

	@Autowired
	private DESCryption desCryption;

	@Autowired
	private ExamPaperQuestionService examPaperQuestionService;

	private DecimalFormat df = new DecimalFormat("0.00#");

	@Override
	public Map<String, Object> statExam(String examId) {

		Map<String, Object> data = Maps.newHashMap();

		List<AnswerRecord> records = this.answerRecordService.getByExam(examId);
		if (null == records || records.isEmpty()) {
			return data;
		}

		Map<String, List<AnswerRecord>> examPaperRecordMap = records.parallelStream()
		        .collect(Collectors.groupingBy(AnswerRecord::getExamPaperId));

		for (Map.Entry<String, List<AnswerRecord>> entry : examPaperRecordMap.entrySet()) {
			Map<String, Object> examPaperStatData = Maps.newHashMap();
			String examPaperId = entry.getKey();
			ExamPaper examPaper = this.examPaperService.get(examPaperId);
			data.put(examPaper.getTitle(), examPaperStatData);
			this.getFullScore(examPaperStatData, examPaper);
			List<AnswerRecord> rs = entry.getValue();
			this.getAverageScore(examPaperStatData, rs);
			Integer fullScore = (Integer) examPaperStatData.get("full_score");
			this.getPercentNo(examPaperStatData, rs, fullScore);
			this.getRealityDifficulty(examPaperStatData);
			this.getExamPaperDifficulty(examPaperStatData, examId, examPaperId);
		}
		return data;
	}

	private void getFullScore(Map<String, Object> examPaperStatData, ExamPaper examPaper) {
		if (null != examPaper) {
			Integer score = null == examPaper.getScore() || 0 >= examPaper.getScore() ? 100 : examPaper.getScore();
			examPaperStatData.put("full_score", score);
		}
	}

	private void getAverageScore(Map<String, Object> examPaperStatData, List<AnswerRecord> rs) {
		int sum = rs.parallelStream().mapToInt(r -> {
			return null == r.getScore() ? 0 : r.getScore();
		}).sum();
		Map<String, List<AnswerRecord>> examineeRecordMap = rs.parallelStream()
		        .collect(Collectors.groupingBy(AnswerRecord::getExamineeId));
		int averageScore = sum / examineeRecordMap.size();
		examPaperStatData.put("average_score", averageScore);
	}

	private void getPercentNo(Map<String, Object> examPaperStatData, List<AnswerRecord> rs, Integer fullScore) {
		int lessPercentSixtyNo = 0;
		int percentSixtyNo = 0;
		int percentSeventyNo = 0;
		int percentEightyNo = 0;
		int percentNinetyNo = 0;
		Map<String, List<AnswerRecord>> examineeRecordMap = rs.parallelStream()
		        .collect(Collectors.groupingBy(AnswerRecord::getExamineeId));
		for (Map.Entry<String, List<AnswerRecord>> entry : examineeRecordMap.entrySet()) {
			int sum = entry.getValue().parallelStream().mapToInt(r -> {
				return null == r.getScore() ? 0 : r.getScore();
			}).sum();
			double percent = (double) sum / fullScore;
			if (percent < 0.6) {
				lessPercentSixtyNo++;
			} else if (percent >= 0.6 && percent < 0.7) {
				percentSixtyNo++;
			} else if (percent >= 0.7 && percent < 0.8) {
				percentSeventyNo++;
			} else if (percent >= 0.8 && percent < 0.9) {
				percentEightyNo++;
			} else if (percent >= 0.9) {
				percentNinetyNo++;
			}
		}
		examPaperStatData.put("less_percent_sixty", lessPercentSixtyNo);
		examPaperStatData.put("percent_sixty", percentSixtyNo);
		examPaperStatData.put("percent_seventyty", percentSeventyNo);
		examPaperStatData.put("percent_eighty", percentEightyNo);
		examPaperStatData.put("percent_ninety", percentNinetyNo);
	}

	private void getRealityDifficulty(Map<String, Object> examPaperStatData) {
		int averageScore = (int) examPaperStatData.get("average_score");
		Integer fullScore = (Integer) examPaperStatData.get("full_score");
		examPaperStatData.put("reality_diff", df.format((double) averageScore / fullScore));
	}

	private void getExamPaperDifficulty(Map<String, Object> examPaperStatData, String examId, String examPaperId) {
		List<ExamPaperQuestion> questions = this.examQuestionService.getExamPaperQuestion(examId, examPaperId);
		if (null == questions || questions.isEmpty()) {
			return;
		}
		double sum = questions.parallelStream().mapToDouble(question -> {
			return question.getExamQuestion().getDifficulty() * question.getScore();
		}).sum();
		ExamPaper examPaper = questions.get(0).getExamPaper();
		if (CList.Api.SubjectType.LOOK_SING.equals(examPaper.getSubjectType())) {
			sum = sum / questions.size();
		}
		examPaperStatData.put("epaper_diff", df.format(sum / (Integer) examPaperStatData.get("full_score")));
	}

	@Override
	public Map<String, Object> statExaminee(String examId, String examPaperId) {

		Map<String, Object> data = Maps.newHashMap();

		List<AnswerRecord> records = this.answerRecordService.getByExamPaper(examId, examPaperId);
		if (null == records || records.isEmpty()) {
			return data;
		}
		Map<String, List<AnswerRecord>> examQuestionRecordMap = records.parallelStream()
		        .collect(Collectors.groupingBy(AnswerRecord::getExamineeId));
		for (Map.Entry<String, List<AnswerRecord>> entry : examQuestionRecordMap.entrySet()) {
			List<Map<String, Object>> examineeData = Lists.newArrayList();
			List<AnswerRecord> rs = entry.getValue();
			String name = rs.get(0).getExaminee().getPerson().getName();
			while (data.containsKey(name)) {
				name = name + " ";
			}
			data.put(name, examineeData);
			Map<String, Integer> scoreMap = this.examPaperQuestionService.getScore(examPaperId);
			rs.forEach(r -> {
				if (null != scoreMap.get(r.getExamQuestion().getId())
			            && scoreMap.get(r.getExamQuestion().getId()) > 0) {

					Map<String, Object> examQuestionStatData = Maps.newHashMap();
					examineeData.add(examQuestionStatData);
					examQuestionStatData.put("id", r.getExamQuestion().getId());
					examQuestionStatData.put("title", this.desCryption.decode(r.getExamQuestion().getTitle()));
					examQuestionStatData.put("diff", r.getExamQuestion().getDifficulty());

					examQuestionStatData.put("socre", scoreMap.get(r.getExamQuestion().getId()));
					examQuestionStatData.put("person_socre", r.getScore());
					List<AnswerRecord> ars = this.answerRecordService.getByQuestion(examId,
			                r.getExamQuestion().getId());
					List<Integer> scores = ars.stream().map(ar -> {
				        return null == ar.getScore() ? 0 : ar.getScore();
			        }).distinct().collect(Collectors.toList());
					int ranking = 1;
					for (Integer score : scores) {
						if (score > r.getScore()) {
							ranking++;
						}
					}
					examQuestionStatData.put("ranking", ranking);
				}
			});
		}
		return data;
	}

	@Override
	public Map<String, Object> statExamPaper(String examId, String examPaperId) {

		Map<String, Object> data = Maps.newHashMap();

		List<AnswerRecord> records = this.answerRecordService.getByExamPaper(examId, examPaperId);
		if (null == records || records.isEmpty()) {
			return data;
		}

		Map<String, List<AnswerRecord>> examQuestionRecordMap = records.parallelStream()
		        .collect(Collectors.groupingBy(AnswerRecord::getExamQuestionId));

		for (Map.Entry<String, List<AnswerRecord>> entry : examQuestionRecordMap.entrySet()) {
			List<AnswerRecord> rs = entry.getValue();
			Map<String, Object> examQuestionStatData = Maps.newHashMap();
			String title = this.desCryption.decode(rs.get(0).getExamQuestion().getTitle());
			while (data.containsKey(title)) {
				title = title + " ";
			}
			data.put(title, examQuestionStatData);
			ExamQuestion question = rs.get(0).getExamQuestion();
			examQuestionStatData.put("diff", question.getDifficulty());
			Integer score = this.examQuestionService.getQuestionScore(examPaperId, question.getId());
			examQuestionStatData.put("score", score);
			int sum = rs.parallelStream().mapToInt(r -> {
				int s = null == r.getScore() ? 0 : r.getScore();
				return s;
			}).sum();
			int averageScore = sum / rs.size();
			examQuestionStatData.put("average_score", averageScore);
			examQuestionStatData.put("reality_diff", df.format((double) averageScore / score));
			List<String> scorePersonNoList = Lists.newArrayList();
			examQuestionStatData.put("person_score_no", scorePersonNoList);
			Map<Integer, List<AnswerRecord>> scoreRecordMap = rs.parallelStream()
			        .collect(Collectors.groupingBy(AnswerRecord::getScore));
			for (Map.Entry<Integer, List<AnswerRecord>> en : scoreRecordMap.entrySet()) {
				scorePersonNoList.add(String.valueOf(en.getKey()) + "分:" + en.getValue().size() + "个");
			}
		}
		return data;
	}

}
