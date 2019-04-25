package com.nercl.music.cloud.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.cloud.entity.template.TemplateSongImpress;

@Repository
public class TemplateSongImpressDaoImpl extends AbstractBaseDaoImpl<TemplateSongImpress, String>
		implements TemplateSongImpressDao {

	@Override
	public List<TemplateSongImpress> get(String templateId, String songId) {
		String jpql = "from TemplateSongImpress sit where sit.templateId = ?1 and sit.songId = ?2";
		List<TemplateSongImpress> sits = this.executeQueryWithoutPaging(jpql, templateId, songId);
		return sits;
	}

	@Override
	public List<TemplateSongImpress> get(String templateId, String songId, String impressId) {
		String jpql = "from TemplateSongImpress sit where sit.templateId = ?1 and sit.songId = ?2 and sit.impressId = ?3";
		List<TemplateSongImpress> sits = this.executeQueryWithoutPaging(jpql, templateId, songId, impressId);
		return sits;
	}

	@Override
	public List<TemplateSongImpress> findByTemplate(String tid) {
		String jpql = "from TemplateSongImpress sit where sit.templateId = ?1";
		List<TemplateSongImpress> sits = this.executeQueryWithoutPaging(jpql, tid);
		return sits;
	}

}
