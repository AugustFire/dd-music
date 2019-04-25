package com.nercl.music.cloud.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.Lists;
import com.nercl.music.cloud.dao.ActivityClassDao;
import com.nercl.music.cloud.dao.ActivityMemberDao;
import com.nercl.music.cloud.dao.AnswerRecordDao;
import com.nercl.music.cloud.entity.ActivityClass;
import com.nercl.music.cloud.entity.ActivityMember;
import com.nercl.music.cloud.entity.ActivityType;
import com.nercl.music.cloud.entity.AnswerRecord;
import com.nercl.music.cloud.entity.AnswerSource;
import com.nercl.music.cloud.entity.ExamPaperType;
import com.nercl.music.cloud.entity.UserRole;
import com.nercl.music.cloud.entity.vo.BasicIndex;
import com.nercl.music.cloud.entity.vo.MusicAccomplishment;
import com.nercl.music.cloud.entity.vo.TestStatistic;
import com.nercl.music.constant.ApiClient;
import com.nercl.music.util.GraspValueUtil;

@Service
@Transactional
public class MasterStatisicServiceImpl implements MasterStatisicService {

	@Autowired
	private AnswerRecordDao answerRecordDao;

	@Autowired
	private GraspValueUtil graspValueUtil;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ActivityClassDao activityClassDao;

	@Autowired
	private ActivityMemberDao activityMemberDao;

	@SuppressWarnings("unchecked")
	@Override
	public List<MusicAccomplishment> getAccomplishment4Master(String sid, String gid, Long start, Long end) {
		List<MusicAccomplishment> result = Lists.newArrayList();
		List<AnswerRecord> gradeRecord = answerRecordDao.getGradeRecord(gid, sid, start, end);
		Map<String, List<AnswerRecord>> groupByClassId = gradeRecord.parallelStream()
				.collect(Collectors.groupingBy(record -> {
					return record.getClassId();
				}));
		groupByClassId.forEach((classId, records) -> {
			MusicAccomplishment ma = new MusicAccomplishment();
			Map<String, Object> map = restTemplate.getForObject(ApiClient.GET_CLASS_TEACHER, Map.class, classId);
			List<Map<String, Object>> listTeacher = (List<Map<String, Object>>) map.get("class_teacher");
			if (listTeacher != null && !listTeacher.isEmpty()) {
				ma.setTeacherId((String) listTeacher.get(0).get("userId")); // 教师id
				ma.setTeacherName((String) listTeacher.get(0).get("userName")); // 教师姓名
			}
			ma.setClassId(classId); // 班级id
			if (records != null && !records.isEmpty()) {
				ma.setClassName(records.get(0).getClassName()); // 班级名称
			}
			long total = records.parallelStream().map(AnswerRecord::getUserId).distinct().count();
			ma.setTotalAmount(total); // 班级总人数
			Integer graspValue = graspValueUtil.getGraspValue(records);
			Float average = graspValueUtil.divide(graspValue, records.size());
			ma.setAverage(average); // 平均分
			Long s60 = records.parallelStream().filter(record -> graspValueUtil.getGraspValue(record) < 60F).count();
			Long s60_70 = records.parallelStream().filter(
					record -> graspValueUtil.getGraspValue(record) >= 60F && graspValueUtil.getGraspValue(record) < 70F)
					.count();
			Long s70_80 = records.parallelStream().filter(
					record -> graspValueUtil.getGraspValue(record) >= 70F && graspValueUtil.getGraspValue(record) < 80F)
					.count();
			Long s80_90 = records.parallelStream().filter(
					record -> graspValueUtil.getGraspValue(record) >= 80F && graspValueUtil.getGraspValue(record) < 90F)
					.count();
			Long s90 = records.parallelStream().filter(record -> graspValueUtil.getGraspValue(record) >= 90F).count();
			ma.setPassRate(graspValueUtil.divide(s60_70 + s70_80 + s80_90 + s90, records.size())); // 及格率
			ma.setExcellentRate(graspValueUtil.divide(s90, records.size())); // 优秀率
			ma.setSpread60(s60.intValue()); // 60以下
			ma.setSpread60_70(s60_70.intValue()); // [60,70)
			ma.setSpread70_80(s70_80.intValue()); // [70,80)
			ma.setSpread80_90(s80_90.intValue()); // [80,90)
			ma.setSpread90(s90.intValue()); // 90以上
			result.add(ma);
		});
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BasicIndex> getBasicIndex(String sid, String gid, Long start, Long end) throws Exception {
		List<BasicIndex> biList = Lists.newCopyOnWriteArrayList();
		List<ActivityClass> activityClassList = activityClassDao.getActivityClassesByConditions(sid, gid, null, null, 1,
				999999);

		Map<String, List<ActivityClass>> teacherActivityClassGroup = activityClassList.parallelStream()
				.filter(ac -> ac.getActivity().getUserRole().compareTo(UserRole.TEACHER) == 0)
				.collect(Collectors.groupingBy(ac -> ac.getClassId()));

		teacherActivityClassGroup.forEach((cid, actList) -> {
			BasicIndex bi = new BasicIndex();
			Map<String, Object> map = restTemplate.getForObject(ApiClient.GET_CLASS_STUDNETS, Map.class, cid);
			List<Map<String, Object>> students = (List<Map<String, Object>>) map.get("students");
			if (students != null) {
				bi.setClassNumber(students.size()); // 班级人数
				List<ActivityMember> members = activityMemberDao.getActivityMembers(cid,
						actList.get(0).getActivityId());
				bi.setAverageNo((float) ((members == null || members.isEmpty()) ? 0 : members.size())); // 参与人数
				bi.setParticipation(100*(graspValueUtil.divide((members == null || members.isEmpty()) ? 0 : members.size(),
						students.size()))); // 参与度 乘以100
			}
			bi.setClassId(cid); // 班级id
			bi.setClassName(actList.get(0).getClassName()); // 班级名称
			bi.setActivityNo(actList.size()); // 活动数量
			bi.setTeacherId(actList.get(0).getActivity().getUserId()); // 教师id
			bi.setTeacherName(actList.get(0).getActivity().getUserName()); // 教师名
			biList.add(bi);
		});

		Map<String, List<ActivityClass>> studentActivityClassGroup = activityClassList.parallelStream()
				.filter(ac -> ac.getActivity().getUserRole().compareTo(UserRole.STUDENT) == 0)
				.collect(Collectors.groupingBy(ac -> ac.getClassId()));
		studentActivityClassGroup.forEach((classId, aList) -> {
			// 比赛获奖人数
			Long awardNo = aList.parallelStream().filter(act -> act.getActivity().getActivityType()
					.compareTo(ActivityType.STUDENT_COMPETITION_AWARD) == 0).count();
			// 考级人数=总人数-比赛获奖人数
			int gradingTestNo = aList.size() - awardNo.intValue();
			if (!biList.isEmpty()) {
				biList.forEach(bi -> {
					if (classId.equals(bi.getClassId())) {
						bi.setAwardNo(awardNo.intValue()); // 获奖人数
						bi.setGradingTestNo(gradingTestNo); // 考级人数
					}else{
						BasicIndex bix = new BasicIndex();
						bix.setAwardNo(awardNo.intValue()); // 获奖人数
						bix.setGradingTestNo(gradingTestNo); // 考级人数
						Map<String, Object> map = restTemplate.getForObject(ApiClient.GET_CLASS_STUDNETS, Map.class, classId);
						List<Map<String, Object>> students = (List<Map<String, Object>>) map.get("students");
						if (students != null) {
							bix.setClassNumber(students.size()); // 班级人数
						}
						bix.setClassId(classId); // 班级id
						bix.setClassName(aList.get(0).getClassName()); // 班级名称
						bix.setTeacherId(aList.get(0).getActivity().getUserId()); // 教师id
						bix.setTeacherName(aList.get(0).getActivity().getUserName()); // 教师名
						biList.add(bix);
					}
				});
			}else{
				BasicIndex bix = new BasicIndex();
				bix.setAwardNo(awardNo.intValue()); // 获奖人数
				bix.setGradingTestNo(gradingTestNo); // 考级人数
				Map<String, Object> map = restTemplate.getForObject(ApiClient.GET_CLASS_STUDNETS, Map.class, classId);
				List<Map<String, Object>> students = (List<Map<String, Object>>) map.get("students");
				if (students != null) {
					bix.setClassNumber(students.size()); // 班级人数
				}
				bix.setClassId(classId); // 班级id
				bix.setClassName(aList.get(0).getClassName()); // 班级名称
				bix.setTeacherId(aList.get(0).getActivity().getUserId()); // 教师id
				bix.setTeacherName(aList.get(0).getActivity().getUserName()); // 教师名
				biList.add(bix);
			}
		});
		// TODO 出勤率要调第三方接口
		return biList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TestStatistic> getTestStatistics(String sid, String gid, AnswerSource examType, Long start, Long end) {
		List<TestStatistic> result = Lists.newArrayList();
		List<AnswerRecord> gradeRecord = answerRecordDao.getRecordInAnswerSource(gid, sid, examType, start, end);
		Map<String, List<AnswerRecord>> groupByClassId = gradeRecord.parallelStream()
				.collect(Collectors.groupingBy(record -> {
					return record.getClassId();
				}));
		long examTimes = gradeRecord.parallelStream().map(AnswerRecord::getExamId).distinct().count(); // 进行的考试场数
		long actExamTimes = gradeRecord.parallelStream() // 表演考试场数
				.filter(record -> record.getExamPaper().getExamPaperType().compareTo(ExamPaperType.ACT) == 0)
				.map(AnswerRecord::getExamId).distinct().count();
		long abilityExamTimes = gradeRecord.parallelStream() // 素养考试场数
				.filter(record -> record.getExamPaper().getExamPaperType().compareTo(ExamPaperType.MUSIC_ABILITY) == 0)
				.map(AnswerRecord::getExamId).distinct().count();
		long students = gradeRecord.parallelStream().map(AnswerRecord::getUserId).distinct().count(); // 考试人次
		groupByClassId.forEach((classId, records) -> {
			TestStatistic tstics = new TestStatistic();
			tstics.setClassId(classId);
			tstics.setClassName(records.get(0).getClassName());
			Map<String, Object> map = restTemplate.getForObject(ApiClient.GET_CLASS_TEACHER, Map.class, classId);
			List<Map<String, Object>> listTeacher = (List<Map<String, Object>>) map.get("class_teacher");
			if (listTeacher != null && !listTeacher.isEmpty()) {
				tstics.setTeacherId((String) listTeacher.get(0).get("userId")); // 教师id
				tstics.setTeacherName((String) listTeacher.get(0).get("userName")); // 教师姓名
			}
			tstics.setTotalAverageScore(
					graspValueUtil.divide(graspValueUtil.getGraspValue(records), (examTimes * students))); // 总分平均分
			List<AnswerRecord> actTest = records.parallelStream()
					.filter(record -> record.getExamPaper().getExamPaperType().compareTo(ExamPaperType.ACT) == 0)
					.collect(Collectors.toList());
			List<AnswerRecord> abilityTest = records.parallelStream().filter(
					record -> record.getExamPaper().getExamPaperType().compareTo(ExamPaperType.MUSIC_ABILITY) == 0)
					.collect(Collectors.toList());
			tstics.setKnowledgeAverageScore(
					graspValueUtil.divide(graspValueUtil.getGraspValue(abilityTest), (abilityExamTimes * students))); // 知识考试平均分
			tstics.setActAverageScore(
					graspValueUtil.divide(graspValueUtil.getGraspValue(actTest), (actExamTimes * students))); // 表演考试平均分

			Map<String, List<AnswerRecord>> groupByExam = records.parallelStream()
					.collect(Collectors.groupingBy(AnswerRecord::getExamId));
			List<String> pass = Lists.newArrayList(); // 考试及格的人
			List<String> excellent = Lists.newArrayList(); // 考试优秀的人
			List<String> users = Lists.newArrayList(); // 考试总的人

			List<String> act_pass = Lists.newArrayList(); // 表演考试及格的人
			List<String> act_excellent = Lists.newArrayList(); // 表演考试优秀的人
			List<String> act_users = Lists.newArrayList(); // 表演考试总的人

			List<String> ability_pass = Lists.newArrayList(); // 知识考试及格的人
			List<String> ability_excellent = Lists.newArrayList(); // 知识考试优秀的人
			List<String> ability_users = Lists.newArrayList(); // 知识考试总的人
			groupByExam.forEach((examId, res) -> {
				Map<String, Double> actScores = res.parallelStream()
						.filter(r -> r.getExamPaper().getExamPaperType().compareTo(ExamPaperType.ACT) == 0)
						.collect(Collectors.groupingBy(AnswerRecord::getUserId,
								Collectors.summingDouble(AnswerRecord::getScore))); // 表演--每场考试中每个学生的得分
				Map<String, Double> abilityScores = res.parallelStream()
						.filter(r -> r.getExamPaper().getExamPaperType().compareTo(ExamPaperType.MUSIC_ABILITY) == 0)
						.collect(Collectors.groupingBy(AnswerRecord::getUserId,
								Collectors.summingDouble(AnswerRecord::getScore))); // 知识--每场考试中每个学生的得分
				Map<String, Double> scores = res.parallelStream().collect(Collectors.groupingBy(AnswerRecord::getUserId,
						Collectors.summingDouble(AnswerRecord::getScore))); // 每场考试中每个学生的得分
				Map<String, Double> fullScores = res.parallelStream().collect(Collectors
						.groupingBy(AnswerRecord::getUserId, Collectors.summingDouble(AnswerRecord::getFullScore)));// 每场考试的总分

				actScores.forEach((userId, score) -> {
					if (graspValueUtil.divide(score, fullScores.get(userId)) >= 0.6F) {
						act_pass.add(userId);
					}
					if (graspValueUtil.divide(score, fullScores.get(userId)) >= 0.9F) {
						act_excellent.add(userId);
					}
					act_users.add(userId);
				});
				abilityScores.forEach((userId, score) -> {
					if (graspValueUtil.divide(score, fullScores.get(userId)) >= 0.6F) {
						ability_pass.add(userId);
					}
					if (graspValueUtil.divide(score, fullScores.get(userId)) >= 0.9F) {
						ability_excellent.add(userId);
					}
					ability_users.add(userId);
				});
				scores.forEach((userId, score) -> {
					if (graspValueUtil.divide(score, fullScores.get(userId)) >= 0.6F) {
						pass.add(userId);
					}
					if (graspValueUtil.divide(score, fullScores.get(userId)) >= 0.9F) {
						excellent.add(userId);
					}
					users.add(userId);
				});
			});
			tstics.setTotalPassRate(graspValueUtil.divide(pass.size(), users.size()));
			tstics.setTotalExcellentRate(graspValueUtil.divide(excellent.size(), users.size()));

			tstics.setActPassRate(graspValueUtil.divide(act_pass.size(), act_users.size()));
			tstics.setActExcellentRate(graspValueUtil.divide(act_excellent.size(), act_users.size()));

			tstics.setKnowledgePassRate(graspValueUtil.divide(ability_pass.size(), ability_users.size()));
			tstics.setKnowledgeExcellentRate(graspValueUtil.divide(ability_excellent.size(), ability_users.size()));
			result.add(tstics);
		});
		return result;
	}
}
