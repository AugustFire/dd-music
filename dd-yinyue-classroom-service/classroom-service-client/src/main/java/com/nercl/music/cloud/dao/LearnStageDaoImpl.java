package com.nercl.music.cloud.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.cloud.entity.base.LearnStage;

@Repository
public class LearnStageDaoImpl extends AbstractBaseDaoImpl<LearnStage, String> implements LearnStageDao {

	@Override
	public LearnStage findByCode(String learnStageCode) {
		String jpql = "from LearnStage ls where ls.code = ?1";
		List<LearnStage> learnStages = this.executeQueryWithoutPaging(jpql, learnStageCode);
		if(!learnStages.isEmpty()){
			return learnStages.get(0);
		}
		return null;
	}

}
