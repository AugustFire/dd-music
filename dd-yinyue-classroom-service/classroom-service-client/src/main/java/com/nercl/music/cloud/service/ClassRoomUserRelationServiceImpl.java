package com.nercl.music.cloud.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Joiner;
import com.nercl.music.cloud.dao.ClassRoomUserRelationDao;
import com.nercl.music.cloud.entity.classroom.ClassRoomUserRelation;
import com.nercl.music.constant.ApiClient;

@Service
@Transactional
public class ClassRoomUserRelationServiceImpl implements ClassRoomUserRelationService {

	@Autowired
	private ClassRoomUserRelationDao classRoomUserRelationDao;

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public boolean save(String rid, String userId) {
		ClassRoomUserRelation crur = this.get(rid, userId);
		if (null != crur) {
			return true;
		}
		crur = new ClassRoomUserRelation();
		crur.setClassRoomId(rid);
		crur.setStudentId(userId);
		crur.setJoinedAt(System.currentTimeMillis());
		this.classRoomUserRelationDao.save(crur);
		return null != crur.getId();
	}

	@Override
	public ClassRoomUserRelation get(String roomId, String uid) {
		return classRoomUserRelationDao.get(roomId, uid);
	}

	@Override
	public void removeJoinedStudent(String rid, String[] uids) {
		Arrays.stream(uids).forEach(uid -> {
			ClassRoomUserRelation tr = this.get(rid, uid);
			if (null != tr) {
				classRoomUserRelationDao.deleteById(tr.getId());
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getJoinedStudents(String rid) {
		List<ClassRoomUserRelation> crurs = this.classRoomUserRelationDao.get(rid);
		if (null == crurs || crurs.isEmpty()) {
			return null;
		}
		List<String> sids = crurs.stream().map(crur -> {
			return crur.getStudentId();
		}).collect(Collectors.toList());
		if (null == sids || sids.isEmpty()) {
			return null;
		}
		Map<String, Object> users = restTemplate.getForObject(ApiClient.GET_USERS, Map.class,
				Joiner.on(",").join(sids));

		return (List<Map<String, Object>>) users.get("users");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getUnGroupedUsers(String rid) {
		List<ClassRoomUserRelation> crurs = this.classRoomUserRelationDao.getUnGroupedUsers(rid);
		if (null == crurs || crurs.isEmpty()) {
			return null;
		}
		List<String> sids = crurs.stream().map(crur -> {
			return crur.getStudentId();
		}).collect(Collectors.toList());
		if (null == sids || sids.isEmpty()) {
			return null;
		}
		Map<String, Object> users = restTemplate.getForObject(ApiClient.GET_USERS, Map.class,
				Joiner.on(",").join(sids));

		return (List<Map<String, Object>>) users.get("users");
	}

	@Override
	public int deleteClassroomUsers(String id) {
		List<ClassRoomUserRelation> list = classRoomUserRelationDao.get(id);
		if (null == list || list.isEmpty()) {
			return 0;
		}
		list.forEach(l -> {
			classRoomUserRelationDao.deleteById(l.getId());
		});
		return list.size();
	}

}
