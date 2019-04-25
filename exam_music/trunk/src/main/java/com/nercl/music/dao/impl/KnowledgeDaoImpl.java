package com.nercl.music.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.dao.KnowledgeMapDao;
import com.nercl.music.entity.KnowledgeMap;

@Repository
public class KnowledgeDaoImpl extends AbstractBaseDaoImpl<KnowledgeMap, String> implements KnowledgeMapDao {

	@Override
	public KnowledgeMap getByNo(String no) {
		String jpql = "from KnowledgeMap ex where ex.no = ?1";
		List<KnowledgeMap> KnowledgeMaps = executeQueryWithoutPaging(jpql, no);
		return null != KnowledgeMaps && !KnowledgeMaps.isEmpty() ? KnowledgeMaps.get(0) : null;
	}

	@Override
	public List<KnowledgeMap> getAll() {
		String jpql = "from KnowledgeMap km order by km.id asc";
		List<KnowledgeMap> KnowledgeMaps = executeQueryWithoutPaging(jpql);
		return KnowledgeMaps;
	}

	@Override
	public KnowledgeMap getTop() {
		String jpql = "from KnowledgeMap km where km.no = 'YL'";
		List<KnowledgeMap> KnowledgeMaps = executeQueryWithoutPaging(jpql);
		return null != KnowledgeMaps && !KnowledgeMaps.isEmpty() ? KnowledgeMaps.get(0) : null;
	}

}
