package com.nercl.music.cloud.service;

import java.util.List;

import com.nercl.music.cloud.entity.base.Grade;

public interface GradeService {
	/**
	 * 获取所有年级
	 * */
	List<Grade> getAllGrades();
	
	/**
	 * 根据条件查询对应的年级
	 * @param condition 查询条件
	 * */
	List<Grade> findByConditions(Grade condition) throws Exception;
	
	/**
	 * 根据Id查询对应的年级
	 * @param gradeId 年级id
	 * */
	Grade findById(String gradeId);
	
	/**
	 * 根据code查询对应的年级
	 * @param gradeCode 年级code
	 * */
	Grade findByCode(String gradeCode);
	/**
	 * 更新年级
	 * @param grade 更新实体
	 * */
	void update(Grade grade);
	
	/**
	 * 根据Id删除年级
	 * @param gradeId
	 * */
	void deleteById(String gradeId) throws Exception ;
	
	/**
	 * 删除
	 * @param grade 删除实体
	 * */
	void delete(Grade grade);
	
	/**
	 * 保存
	 * @param grade 新增实体
	 * */
	void save(Grade grade);

}
