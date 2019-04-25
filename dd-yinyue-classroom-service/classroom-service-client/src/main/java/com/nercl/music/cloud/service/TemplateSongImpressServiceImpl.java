package com.nercl.music.cloud.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.nercl.music.cloud.dao.TemplateSongImpressDao;
import com.nercl.music.cloud.entity.template.TemplateSongImpress;

@Service
@Transactional
public class TemplateSongImpressServiceImpl implements TemplateSongImpressService {

	@Autowired
	private TemplateSongImpressDao songImpressTemplateDao;

	@Override
	public List<TemplateSongImpress> get(String templateId, String songId) {
		return songImpressTemplateDao.get(templateId, songId);
	}

	@Override
	public void deleteBytemplate(String templateId) {
		List<TemplateSongImpress> tsis = songImpressTemplateDao.findByTemplate(templateId);
		if (null != tsis) {
			tsis.forEach(tsi -> {
				songImpressTemplateDao.deleteById(tsi.getId());
			});
		}
	}

	@Override
	public boolean save(String templateId, String songId, String impressId) {
//		boolean exsit = exsit(templateId, songId, impressId);
//		if (exsit) {
//			return false;
//		}
		List<TemplateSongImpress> tsis = songImpressTemplateDao.get(templateId, songId, impressId);
		if (null != tsis) {
			tsis.forEach(tsi -> {
				songImpressTemplateDao.deleteById(tsi.getId());
			});
		}
		TemplateSongImpress sit = new TemplateSongImpress();
		sit.setTemplateId(templateId);
		sit.setSongId(songId);
		sit.setImpressId(impressId);
		songImpressTemplateDao.save(sit);
		return !Strings.isNullOrEmpty(sit.getId());
	}

	@Override
	public boolean exsit(String templateId, String songId, String impressId) {
		List<TemplateSongImpress> sits = songImpressTemplateDao.get(templateId, songId, impressId);
		return null != sits && !sits.isEmpty();
	}

}
