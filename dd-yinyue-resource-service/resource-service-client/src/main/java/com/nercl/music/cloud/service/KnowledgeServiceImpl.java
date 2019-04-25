package com.nercl.music.cloud.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.nercl.music.cloud.dao.KnowledgeDao;
import com.nercl.music.cloud.entity.knowledge.Knowledge;

@Service
@Transactional
public class KnowledgeServiceImpl implements KnowledgeService {

	@Autowired
	private KnowledgeDao knowledgeDao;

	@Override
	public List<Knowledge> get(String pid) {
		if(Strings.isNullOrEmpty(pid)){
			
		}else{
			
		}
		return null;
	}

	@Override
	public boolean save(String no, String title, Float difficulty, String parentId) {
		Knowledge knowledge = new Knowledge();
		knowledge.setNo(no);
		knowledge.setTitle(title);
		knowledge.setDifficulty(difficulty);
		knowledge.setParentId(parentId);
		knowledgeDao.save(knowledge);
		return !Strings.isNullOrEmpty(knowledge.getId());
	}

	@Override
	public boolean update(String kid, String no, String title, Float difficulty, String parentId) {
		Knowledge knowledge = knowledgeDao.findByID(kid);
		if (null == knowledge) {
			return false;
		}
		knowledge.setNo(no);
		knowledge.setTitle(title);
		knowledge.setDifficulty(difficulty);
		knowledge.setParentId(parentId);
		knowledgeDao.update(knowledge);
		return !Strings.isNullOrEmpty(knowledge.getId());
	}

	@Override
	public boolean delete(String kid) {
		knowledgeDao.deleteById(kid);
		return null == knowledgeDao.findByID(kid);
	}

}