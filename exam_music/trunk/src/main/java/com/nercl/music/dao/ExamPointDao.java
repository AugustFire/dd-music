package com.nercl.music.dao;

import java.util.List;

import com.nercl.music.entity.ExamPoint;

public interface ExamPointDao extends BaseDao<ExamPoint, String> {

	List<ExamPoint> list(int page);

	List<ExamPoint> list();

	List<ExamPoint> listByAttributes(int page, String name, String address);

	ExamPoint get(String examPoint);

	ExamPoint getByName(String name);
}
