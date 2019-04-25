package com.nercl.music.cloud.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.google.common.base.Strings;
import com.nercl.music.cloud.entity.Knowledge;

@Repository
public class KnowledgeDaoImpl extends AbstractBaseDaoImpl<Knowledge, String> implements KnowledgeDao {

	@Override
	public Knowledge getByNo(String no) {
		String jpql = "from Knowledge km where km.no = ?1";
		List<Knowledge> knowledges = executeQueryWithoutPaging(jpql, no);
		return null != knowledges && !knowledges.isEmpty() ? knowledges.get(0) : null;
	}

	@Override
	public List<Knowledge> getAll() {
		String jpql = "from Knowledge km order by km.id asc";
		List<Knowledge> knowledges = executeQueryWithoutPaging(jpql);
		return knowledges;
	}

	@Override
	public List<Knowledge> getTop() {
		String jpql = "from Knowledge km where km.parentId is NULL";
		List<Knowledge> knowledges = executeQueryWithoutPaging(jpql);
		return knowledges;
	}

	@Override
	public List<Knowledge> getChildren(String knowledgeNo) {
		Knowledge knowledge = getByNo(knowledgeNo);
		if (null == knowledge) {
			return null;
		}
		String jpql = "from Knowledge km where km.parentId = ?1";
		return executeQueryWithoutPaging(jpql, knowledge.getId());
	}

	@Override
	public List<Knowledge> getKnowledgesByParentId(String parentId) {
		StringBuffer jpqlBuffer = new StringBuffer();
		if(Strings.isNullOrEmpty(parentId)){
			jpqlBuffer.append("from Knowledge km where km.parentId is null");
			return executeQueryWithoutPaging(jpqlBuffer.toString());
		}else{
			jpqlBuffer.append("from Knowledge km where km.parentId =?1");
			return executeQueryWithoutPaging(jpqlBuffer.toString(),parentId);
		}
	}
}
