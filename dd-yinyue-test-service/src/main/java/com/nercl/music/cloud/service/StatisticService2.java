package com.nercl.music.cloud.service;

import java.util.List;
import java.util.Map;

public interface StatisticService2 {

	Map<String, Object> getGradeMusicTendency(String sid, String gid, long start, long end);

	Map<String, Object> getGradeAbilityAverageScore(String sid, String gid, long start, long end);

	Map<String, Object> getGradeKnowledgeAverageScore(String sid, String gid, long start, long end);

	List<Map<String, Object>> getGradeMusicExamStatData(String sid, String gid, long start, long end);

	List<Map<String, Object>> getTeacherMusicMiddleExamStatData(String sid, String gid, long start, long end);

	List<Map<String, Object>> getTeacherMusicFinalExamStatData(String sid, String gid, long start, long end);

	List<Map<String, Object>> getTeacherClassBasicData(String sid, String gid, long start, long end);

	List<Map<String, Object>> getTeacherClassMusicStatData(String sid, String gcode, long start, long end);

}
