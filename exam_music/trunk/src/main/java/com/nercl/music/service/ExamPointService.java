package com.nercl.music.service;

import java.util.List;

import com.nercl.music.entity.ExamPoint;

public interface ExamPointService {

	List<ExamPoint> list(int page);

	List<ExamPoint> list();

	List<ExamPoint> listByAttributes(int page, String name, String address);

	ExamPoint get(String examPointId);

	ExamPoint getByName(String name);

	boolean save(String name, String address);

	void update(String id, String name, String address);

	void delete(String id);

}
