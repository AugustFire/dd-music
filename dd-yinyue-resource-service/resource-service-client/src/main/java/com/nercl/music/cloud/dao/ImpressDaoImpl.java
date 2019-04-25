package com.nercl.music.cloud.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.cloud.entity.impress.Impress;
import com.nercl.music.cloud.entity.resource.Dimension;

@Repository
public class ImpressDaoImpl extends AbstractBaseDaoImpl<Impress, String> implements ImpressDao {

	@Override
	public List<Impress> get(String summary, Dimension dimension) {
		String jpql = "from Impress im where im.dimension = ?1 and im.summary like ?2";
		return this.executeQueryWithoutPaging(jpql, dimension, "%" + summary + "%");
	}

	@Override
	public List<Impress> getBySong(String sid) {
		String jpql = "from Impress im where im.songId = ?1";
		return this.executeQueryWithoutPaging(jpql, sid);
	}

}
