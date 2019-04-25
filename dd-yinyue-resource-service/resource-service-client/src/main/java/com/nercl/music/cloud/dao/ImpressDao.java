package com.nercl.music.cloud.dao;

import java.util.List;

import com.nercl.music.cloud.entity.impress.Impress;
import com.nercl.music.cloud.entity.resource.Dimension;

public interface ImpressDao extends BaseDao<Impress, String> {

	List<Impress> get(String summary, Dimension dimension);

	List<Impress> getBySong(String sid);

}
