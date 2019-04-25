package com.nercl.music.cloud.dao;

import com.nercl.music.cloud.entity.base.LearnStage;

public interface LearnStageDao extends BaseDao<LearnStage, String>{

	/**
	 * 根据code查询对应的学段
	 * @param learnStageCode 学段code
	 * */
	LearnStage findByCode(String learnStageCode);

}
