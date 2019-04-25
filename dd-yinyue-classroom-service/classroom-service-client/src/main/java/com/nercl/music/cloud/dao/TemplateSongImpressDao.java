package com.nercl.music.cloud.dao;

import java.util.List;

import com.nercl.music.cloud.entity.template.TemplateSongImpress;

public interface TemplateSongImpressDao extends BaseDao<TemplateSongImpress, String> {

	List<TemplateSongImpress> get(String templateId, String songId);

	List<TemplateSongImpress> get(String templateId, String songId, String impressId);

	List<TemplateSongImpress> findByTemplate(String tid);

}
