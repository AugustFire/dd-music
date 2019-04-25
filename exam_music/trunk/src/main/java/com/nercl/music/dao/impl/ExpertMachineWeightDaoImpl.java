package com.nercl.music.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.dao.ExpertMachineWeightDao;
import com.nercl.music.entity.ExpertMachineWeight;

@Repository
public class ExpertMachineWeightDaoImpl extends AbstractBaseDaoImpl<ExpertMachineWeight, String>
        implements ExpertMachineWeightDao {

	@Override
	public ExpertMachineWeight get(String examId) {
		String jpql = "from ExpertMachineWeight epw where epw.examId = ?1";
		List<ExpertMachineWeight> weights = executeQueryWithoutPaging(jpql, examId);
		return null == weights || weights.isEmpty() ? null : weights.get(0);
	}

}
