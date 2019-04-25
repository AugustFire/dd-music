package com.nercl.music.cloud.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.nercl.music.cloud.dao.BasicFacilityDao;
import com.nercl.music.cloud.entity.base.BasicFacility;

@Service
@Transactional
public class BasicFacilityServiceImpl implements BasicFacilityService {

	@Autowired
	private BasicFacilityDao basicFacilityDao;

	@Override
	public boolean save(String title, Integer num, Boolean isInstrument, String schoolId) {
		List<BasicFacility> bfs = get(title, schoolId);
		if (null != bfs && !bfs.isEmpty()) {
			return false;
		}
		BasicFacility bf = new BasicFacility();
		bf.setTitle(title);
		bf.setNum(num);
		bf.setIsInstrument(isInstrument);
		bf.setSchoolId(schoolId);
		basicFacilityDao.save(bf);
		return !Strings.isNullOrEmpty(bf.getId());
	}

	@Override
	public List<BasicFacility> get(String title, String schoolId) {
		return basicFacilityDao.get(title, schoolId);
	}

	@Override
	public List<BasicFacility> get(String schoolId) {
		return basicFacilityDao.get(schoolId);
	}

	@Override
	public boolean edit(String id, String title, Integer num, Boolean isInstrument) {
		BasicFacility bf = basicFacilityDao.findByID(id);
		if (null == bf) {
			return false;
		}
		List<BasicFacility> bfs = get(title, bf.getSchoolId());
		if (null != bfs && !bfs.isEmpty() && !id.equals(bfs.get(0).getId())) {
			return false;
		}
		bf.setTitle(title);
		bf.setNum(num);
		bf.setIsInstrument(isInstrument);
		basicFacilityDao.update(bf);
		return true;
	}

	@Override
	public boolean delete(String id) {
		basicFacilityDao.deleteById(id);
		return true;
	}

}
