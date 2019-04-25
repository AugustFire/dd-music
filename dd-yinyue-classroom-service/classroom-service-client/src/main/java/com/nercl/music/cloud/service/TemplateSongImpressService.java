package com.nercl.music.cloud.service;

import java.util.List;

import com.nercl.music.cloud.entity.template.TemplateSongImpress;

public interface TemplateSongImpressService {

	List<TemplateSongImpress> get(String templateId, String songId);

	void deleteBytemplate(String templateId);

	boolean save(String templateId, String songId, String impressId);

	boolean exsit(String templateId, String songId, String impressId);

}
