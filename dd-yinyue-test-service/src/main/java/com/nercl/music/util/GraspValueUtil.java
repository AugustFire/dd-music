package com.nercl.music.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import com.nercl.music.cloud.entity.AnswerRecord;
import com.nercl.music.cloud.entity.Question;
import com.nercl.music.cloud.entity.QuestionType;

@Component
public class GraspValueUtil {

	private static int SINGLE_SELECT_WEIGHT = 10;

	private static int MULTI_SELECT_WEIGHT = 20;

	private static int SHORT_ANSWER_WEIGHT = 30;

	public Float getGraspValue(AnswerRecord record) {
		Question question = record.getQuestion();
		if (null == question) {
			return 0F;
		}
		Boolean isTrue = record.getIsTrue();
		QuestionType questionType = question.getQuestionType();
		Float diffculty = question.getDifficulty();
		diffculty = null == diffculty || diffculty > 1 ? 1 : diffculty;
		Float fullScore = record.getFullScore();
		fullScore = null == fullScore ? 1 : fullScore;
		Float score = record.getScore();
		score = null == score ? 0 : score;
		if (QuestionType.SINGLE_SELECT.equals(questionType)) {
			return null != isTrue && isTrue ? SINGLE_SELECT_WEIGHT * (1 - diffculty) : 0;
		} else if (QuestionType.MULTI_SELECT.equals(questionType)) {
			return null != isTrue && isTrue ? MULTI_SELECT_WEIGHT * (1 - diffculty) : 0;
		} else {
			return SHORT_ANSWER_WEIGHT * (1 - diffculty) * (score / fullScore);
		}
	}

	/**
	 * 计算指定答案的总能力得分
	 */
	public Integer getGraspValue(List<AnswerRecord> records) {
		if (null == records || records.isEmpty()) {
			return 0;
		}
		Double sum = records.parallelStream().mapToDouble(record -> getGraspValue(record)).sum();
		return null == sum ? 0 : sum.intValue();
	}

	/**
	 * 计算指定答案的平均能力得分
	 */
	// public Integer getAverageGraspValue(List<AnswerRecord> records) {
	// if (null == records || records.isEmpty()) {
	// return 0;
	// }
	// return getGraspValue(records) / records.size();
	// }

	public Long getStart() {
		Long start = null;
		int year = LocalDate.now().getYear();
		int month = LocalDate.now().getMonthValue();
		try {
			if (month >= 9) {
				start = new SimpleDateFormat("yyyy-MM-dd").parse(year + "-9-1").getTime();
			} else if (month <= 2) {
				start = new SimpleDateFormat("yyyy-MM-dd").parse(year - 1 + "-9-1").getTime();
			} else {
				start = new SimpleDateFormat("yyyy-MM-dd").parse(year + "-3-1").getTime();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return start;
	}

	public Long getEnd() {
		Long end = null;
		int year = LocalDate.now().getYear();
		int month = LocalDate.now().getMonthValue();
		try {
			if (month >= 9) {
				end = new SimpleDateFormat("yyyy-MM-dd").parse(year + 1 + "-2-20").getTime();
			} else if (month <= 2) {
				end = new SimpleDateFormat("yyyy-MM-dd").parse(year + "-2-20").getTime();
			} else {
				end = new SimpleDateFormat("yyyy-MM-dd").parse(year + "-8-31").getTime();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return end;
	}

	/**
	 * 计算除法,四舍五入保留2位小数
	 * 
	 * @param divider
	 *            除数
	 * @param divided
	 *            被除数
	 */
	public Float divide(Number divider, Number divided) {
		divider = null == divider ? 0 : divider;
		divided = null == divided || divided.intValue() <= 0 ? 1 : divided;
		BigDecimal bdDivider = new BigDecimal(divider.floatValue());
		BigDecimal bdDivided = new BigDecimal(divided.floatValue());
		return bdDivider.divide(bdDivided, 2, RoundingMode.HALF_UP).floatValue();
	}

	public Float getTwoDecimal(double num) {
		return new BigDecimal(num).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
	}

}
