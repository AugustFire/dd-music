package com.nercl.music.cloud.service;

import java.util.List;

import com.nercl.music.cloud.entity.AnswerSource;
import com.nercl.music.cloud.entity.vo.BasicIndex;
import com.nercl.music.cloud.entity.vo.MusicAccomplishment;
import com.nercl.music.cloud.entity.vo.TestStatistic;

public interface MasterStatisicService {

	/**
	 * 校长角色---班级音乐素养综合查询
	 * 
	 * @param sid
	 *            学校id
	 * @param gid
	 *            年级id
	 * @param start
	 *            开始时间
	 * @param end
	 *            结束时间
	 * @return
	 */
	List<MusicAccomplishment> getAccomplishment4Master(String sid, String gid, Long start, Long end);

	/**
	 * 校长角色---班级基础指标综合查询
	 * 
	 * @param sid
	 *            学校id
	 * @param gid
	 *            年级id
	 * @param start
	 *            开始时间
	 * @param end
	 *            结束时间
	 * @return
	 */
	List<BasicIndex> getBasicIndex(String sid, String gid, Long start, Long end) throws Exception;

	/**
	 * 校长角色---考试综合查询
	 * 
	 * @param sid
	 *            学校id
	 * @param gid
	 *            年级id
	 * @param examType
	 *            考试类型
	 * @param start
	 *            开始时间
	 * @param end
	 *            结束时间
	 * @return
	 */
	List<TestStatistic> getTestStatistics(String sid, String gid, AnswerSource examType, Long start, Long end);

}
