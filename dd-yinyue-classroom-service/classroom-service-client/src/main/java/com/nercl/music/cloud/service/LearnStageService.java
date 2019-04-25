package com.nercl.music.cloud.service;

import java.util.List;

import com.nercl.music.cloud.entity.base.LearnStage;

public interface LearnStageService {
	/**
	 * 获取所有学段
	 * */
	List<LearnStage> getAllLearnStages();
	
	/**
	 * 根据条件查询对应的学段
	 * @param condition 查询条件
	 * */
	List<LearnStage> findByConditions(LearnStage condition) throws Exception;
	
	/**
	 * 根据Id查询对应的学段
	 * @param learnStageId 学段id
	 * */
	LearnStage findById(String learnStageId);
	
	/**
	 * 根据code查询对应的学段
	 * @param learnStageCode 学段code
	 * */
	LearnStage findByCode(String learnStageCode);
	
	/**
	 * 更新学段
	 * @param learnStage 更新实体
	 * */
	void update(LearnStage learnStage);
	
	/**
	 * 根据Id删除学段
	 * @param 
	 * */
	void deleteById(String learnStageId);
	
	/**
	 * 删除
	 * @param learnStage 删除实体
	 * */
	void delete(LearnStage learnStage);
	
	/**
	 * 保存
	 * @param learnStage 新增实体
	 * */
	void save(LearnStage learnStage);
}
