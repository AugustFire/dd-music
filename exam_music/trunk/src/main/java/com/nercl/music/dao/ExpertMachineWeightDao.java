package com.nercl.music.dao;

import com.nercl.music.entity.ExpertMachineWeight;

public interface ExpertMachineWeightDao extends BaseDao<ExpertMachineWeight, String> {

	ExpertMachineWeight get(String examId);

}
