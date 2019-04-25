package com.nercl.music.cloud.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.nercl.music.cloud.entity.base.BasicFacility;
import com.nercl.music.cloud.entity.base.ClassUser;
import com.nercl.music.cloud.entity.base.Classes;
import com.nercl.music.cloud.entity.base.Grade;
import com.nercl.music.cloud.service.BasicFacilityService;
import com.nercl.music.cloud.service.ClassUserService;
import com.nercl.music.constant.CList;

@RestController
public class ClassUserController2 {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private ClassUserService classUserService;

	@Autowired
	private BasicFacilityService basicFacilityService;

	@PostMapping(value = "/v2/class_user/new", produces = JSON_PRODUCES)
	public Map<String, Object> newClassUser() {
		Map<String, Object> ret = Maps.newHashMap();
		
		classUserService.save("2c92e85767a5492c0167ca5ad6a20000", "东莞老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", true);
		
		classUserService.save("2c92e85767a5492c0167ca5ad7220002", "1老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad72b0004", "2老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad7350006", "3老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad73f0008", "4老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad747000a", "5老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad74e000c", "6老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad754000e", "7老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad75d0010", "8老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad7650012", "9老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad76a0014", "10老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad76f0016", "11老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad7740018", "12老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad778001a", "13老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad77e001c", "14老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad784001e", "15老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad78b0020", "16老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad7920022", "17老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad7970024", "18老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad79d0026", "19老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad7a30028", "20老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad7a9002a", "21老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad7ae002c", "22老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad7b4002e", "23老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad7bc0030", "24老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad7c10032", "25老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad7c50034", "26老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad7cd0036", "27老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad7d30038", "28老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad7ea003a", "29老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad7ef003c", "30老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad7f7003e", "31老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad7fe0040", "32老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad80b0042", "33老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad8130044", "34老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad81a0046", "35老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad81e0048", "36老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad823004a", "37老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad829004c", "38老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad82f004e", "39老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad8370050", "40老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad83c0052", "41老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad8410054", "42老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad8450056", "43老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad84a0058", "44老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad850005a", "45老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad855005c", "46老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad85b005e", "47老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad8620060", "48老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad8670062", "49老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad86e0064", "50老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad8740066", "51老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad87a0068", "52老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad880006a", "53老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad885006c", "54老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad88a006e", "55老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad8900070", "56老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad8950072", "57老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad89b0074", "58老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad8a00076", "59老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad8a50078", "60老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad8aa007a", "61老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad8b4007c", "62老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad8ba007e", "63老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad8c00080", "64老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad8cb0082", "65老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad8d10084", "66老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad8d70086", "67老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad8de0088", "68老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad8e4008a", "69老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad917008c", "70老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad925008e", "71老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad92d0090", "72老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad93a0092", "73老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad93f0094", "74老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad9460096", "75老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad94d0098", "76老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad953009a", "77老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad95a009c", "78老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);

		classUserService.save("2c92e85767a5492c0167ca5ad962009e", "79老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);
		
		classUserService.save("2c92e85767a5492c0167ca5ad96800a0", "80老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);
		
		classUserService.save("2c92e85767a5492c0167ca5ad96f00a2", "81老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);
		
		classUserService.save("2c92e85767a5492c0167ca5ad97700a4", "82老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);
		
		classUserService.save("2c92e85767a5492c0167ca5ad98100a6", "83老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);
		
		classUserService.save("2c92e85767a5492c0167ca5ad98700a8", "84老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);
		
		classUserService.save("2c92e85767a5492c0167ca5ad99100aa", "85老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);
		
		classUserService.save("2c92e85767a5492c0167ca5ad99600ac", "86老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);
		
		classUserService.save("2c92e85767a5492c0167ca5ad99b00ae", "87老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);
		
		classUserService.save("2c92e85767a5492c0167ca5ad9a100b0", "88老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);
		
		classUserService.save("2c92e85767a5492c0167ca5ad9ad00b2", "89老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);
		
		classUserService.save("2c92e85767a5492c0167ca5ad9b200b4", "90老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);
		
		classUserService.save("2c92e85767a5492c0167ca5ad9bc00b6", "91老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);
		
		classUserService.save("2c92e85767a5492c0167ca5ad9c200b8", "92老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);
		
		classUserService.save("2c92e85767a5492c0167ca5ad9cd00ba", "93老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);
		
		classUserService.save("2c92e85767a5492c0167ca5ad9d700bc", "94老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);
		
		classUserService.save("2c92e85767a5492c0167ca5ad9e200be", "95老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);
		
		classUserService.save("2c92e85767a5492c0167ca5ad9f100c0", "96老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);
		
		classUserService.save("2c92e85767a5492c0167ca5ada0200c2", "97老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);
		
		classUserService.save("2c92e85767a5492c0167ca5ada0900c4", "98老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);
		
		classUserService.save("2c92e85767a5492c0167ca5ada0f00c6", "99老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);
		
		classUserService.save("2c92e85767a5492c0167ca5ada1800c8", "100老师", "bbbbbbbbbb", "4028810a62b2b2b20162b2b3dcc70012", "ddddddddddd", false);
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = "/v2/class_user/{uid}", produces = JSON_PRODUCES)
	public Map<String, Object> getClassUser(@PathVariable String uid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(uid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "uid is null");
			return ret;
		}
		int year = LocalDate.now().getYear();
		ClassUser cs = new ClassUser();
		cs.setUserId(uid);
		List<ClassUser> cusers;
		try {
			cusers = classUserService.findByCondionts(cs);
		} catch (Exception e) {
			e.printStackTrace();
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "uid is null");
			return ret;
		}
		ret.put("code", CList.Api.Client.OK);
		if (null == cusers) {
			return ret;
		}

		// 教育局,没有学校、年级、班级信息
		List<ClassUser> bureauofEducation = cusers.stream()
				.filter(cst -> null == cst.getSchoolId() && null == cst.getGradeId() && null == cst.getClassId())
				.collect(Collectors.toList());
		if (null != bureauofEducation && !bureauofEducation.isEmpty()) {
			ret.put("school_id", null);
			ret.put("school_name", null);
			ret.put("classes", null);
			ret.put("grades", null);
			return ret;
		}

		// 校长,没有年级、班级信息
		List<ClassUser> schoolMaster = cusers.stream()
				.filter(cst -> null == cst.getGradeId() && null == cst.getClassId()).collect(Collectors.toList());
		if (null != schoolMaster && !schoolMaster.isEmpty()) {
			ret.put("school_id", schoolMaster.get(0).getSchoolId());
			ret.put("school_name", schoolMaster.get(0).getSchool().getName());
			ret.put("classes", null);
			ret.put("grades", null);
			return ret;
		}

		// 年级主任 ,没有班级信息
		List<ClassUser> gradeMaster = cusers.stream().filter(cst -> null == cst.getClassId())
				.collect(Collectors.toList());
		if (null != gradeMaster && !gradeMaster.isEmpty()) {
			ret.put("school_id", gradeMaster.get(0).getSchoolId());
			ret.put("school_name", gradeMaster.get(0).getSchool().getName());
			List<Map<String, String>> grades = Lists.newArrayList();
			Set<String> gids = Sets.newHashSet();
			gradeMaster.forEach(gm -> {
				if (!Strings.isNullOrEmpty(gm.getGradeId()) && !gids.contains(gm.getGradeId())) {
					gids.add(gm.getGradeId());
					Map<String, String> map = Maps.newHashMap();
					map.put("grade_id", gm.getGradeId());
					map.put("grade_code", gm.getGrade().getCode());
					map.put("grade_name", gm.getGrade().getName());
					grades.add(map);
				}
			});
			ret.put("classes", null);
			ret.put("grades", grades);
			return ret;
		}

		List<ClassUser> cus = cusers.stream()
				.filter(cst -> null != cst.getSchoolId() && null != cst.getGradeId() && null != cst.getClassId())
				.filter(cst -> year == cst.getClasses().getStartYear().intValue()
						|| year == cst.getClasses().getEndYear().intValue())
				.collect(Collectors.toList());
		if (null == cus || cus.isEmpty()) {
			return ret;
		}
		boolean isTeahcher = cusers.get(0).getIsTeacher();
		ret.put("is_teahcher", isTeahcher);
		if (isTeahcher) {
			ret.put("school_id", cusers.get(0).getSchoolId());
			ret.put("school_name", cusers.get(0).getSchool().getName());

			Set<String> cids = Sets.newHashSet();
			Set<String> gids = Sets.newHashSet();

			List<Map<String, String>> classes = Lists.newArrayList();
			List<Map<String, String>> grades = Lists.newArrayList();
			cus.forEach(cu -> {
				if (!Strings.isNullOrEmpty(cu.getClassId()) && !cids.contains(cu.getClassId())) {
					cids.add(cu.getClassId());
					Map<String, String> map = Maps.newHashMap();
					map.put("class_id", cu.getClassId());
					map.put("class_name", cu.getClasses().getName());
					classes.add(map);
				}
				if (!Strings.isNullOrEmpty(cu.getGradeId()) && !gids.contains(cu.getGradeId())) {
					gids.add(cu.getGradeId());
					Map<String, String> map = Maps.newHashMap();
					map.put("grade_id", cu.getGradeId());
					map.put("grade_code", cu.getGrade().getCode());
					map.put("grade_name", cu.getGrade().getName());
					grades.add(map);
				}
			});
			ret.put("classes", classes);
			ret.put("grades", grades);
			return ret;
		} else {
			ClassUser cu = cus.get(0);
			ret.put("school_id", cu.getSchoolId());
			ret.put("school_name", cu.getSchool().getName());

			Map<String, String> classes = Maps.newHashMap();
			classes.put("class_id", cu.getClassId());
			classes.put("class_name", cu.getClasses().getName());
			ret.put("classes", Lists.newArrayList(classes));

			Map<String, String> grade = Maps.newHashMap();
			grade.put("grade_id", cu.getGrade().getId());
			grade.put("grade_code", cu.getGrade().getCode());
			grade.put("grade_name", cu.getGrade().getName());
			ret.put("grades", Lists.newArrayList(grade));
			return ret;
		}
	}

	@GetMapping(value = "/v2/class_user/num", params = { "cid" }, produces = JSON_PRODUCES)
	public Map<String, Object> getClassStudentNum(String cid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(cid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "cid is null");
			return ret;
		}
		int num = classUserService.getStudentNum(cid);
		ret.put("code", CList.Api.Client.OK);
		ret.put("num", num);
		return ret;
	}

	@GetMapping(value = "/v2/class_user/num", params = { "sid" }, produces = JSON_PRODUCES)
	public Map<String, Object> getSchoolStudentNum(String sid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(sid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "sid is null");
			return ret;
		}
		int num = classUserService.getSchoolStudentNum(sid);
		ret.put("code", CList.Api.Client.OK);
		ret.put("num", num);
		return ret;
	}

	@GetMapping(value = "/v2/class_user/grade_student_num", produces = JSON_PRODUCES)
	public Map<String, Object> getGradeNum(String sid, String gid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(sid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "sid is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(gid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "gid is null");
			return ret;
		}
		List<Classes> classes = classUserService.getClassBySchoolGrade(sid, gid);
		ret.put("code", CList.Api.Client.OK);
		if (null == classes) {
			return ret;
		}
		int count = classes.stream()
				.filter(cl -> LocalDate.now().getYear() == cl.getStartYear().intValue()
						|| LocalDate.now().getYear() == cl.getEndYear().intValue())
				.mapToInt(c -> classUserService.getStudentNum(c.getId())).sum();
		ret.put("num", count);
		return ret;
	}

	@GetMapping(value = "/v2/class_user/grades", params = { "tid" }, produces = JSON_PRODUCES)
	public Map<String, Object> getGrades(String tid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(tid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "tid is null");
			return ret;
		}
		List<Grade> grades = classUserService.getGradesByTeacher(tid);
		ret.put("code", CList.Api.Client.OK);
		if (null == grades) {
			return ret;
		}
		List<Map<String, String>> gs = grades.stream().sorted((g1, g2) -> g1.getCode().compareTo(g2.getCode()))
				.map(grade -> {
					Map<String, String> g = Maps.newHashMap();
					g.put("id", grade.getId());
					g.put("name", grade.getName());
					return g;
				}).collect(Collectors.toList());
		ret.put("grades", gs);
		return ret;
	}

	@GetMapping(value = "/v2/class_user/grades", params = { "sid" }, produces = JSON_PRODUCES)
	public Map<String, Object> getSchoolGrades(String sid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(sid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "sid is null");
			return ret;
		}
		List<Grade> grades = classUserService.getGradesBySchool(sid);
		ret.put("code", CList.Api.Client.OK);
		if (null == grades) {
			return ret;
		}
		List<Map<String, String>> gs = grades.stream().sorted((g1, g2) -> g1.getCode().compareTo(g2.getCode()))
				.map(grade -> {
					Map<String, String> g = Maps.newHashMap();
					g.put("id", grade.getId());
					g.put("name", grade.getName());
					return g;
				}).collect(Collectors.toList());
		ret.put("grades", gs);
		return ret;
	}

	@GetMapping(value = "/v2/class_user/teacher/{tid}/classes", produces = JSON_PRODUCES)
	public Map<String, Object> getClassByTeacherGrade(@PathVariable String tid, String gid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(tid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "tid is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(gid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "gid is null");
			return ret;
		}
		List<Classes> classes = classUserService.getClassByTeacherGrade(tid, gid);
		ret.put("code", CList.Api.Client.OK);
		if (null == classes) {
			return ret;
		}
		List<Map<String, String>> cls = classes.stream().sorted(
				(cl1, cl2) -> Strings.nullToEmpty(cl1.getOrderBy()).compareTo(Strings.nullToEmpty(cl2.getOrderBy())))
				.map(cl -> {
					Map<String, String> map = Maps.newHashMap();
					map.put("id", cl.getId());
					map.put("name", cl.getName());
					return map;
				}).collect(Collectors.toList());

		ret.put("classes", cls);
		return ret;
	}

	@GetMapping(value = "/v2/class_user/students", produces = JSON_PRODUCES)
	public Map<String, Object> getStudentsByClass(String cid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(cid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "cid is null");
			return ret;
		}
		List<Map<String, Object>> students = classUserService.getStudentsByClass(cid);
		ret.put("code", CList.Api.Client.OK);
		ret.put("students", students);
		System.out.println("----------students:" + students);
		return ret;
	}

	@GetMapping(value = "/v2/class_user/teachers_classes", produces = JSON_PRODUCES)
	public Map<String, Object> getTeachersClasses(String sid, String gid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(sid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "sid is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(gid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "gid is null");
			return ret;
		}
		List<Classes> classes = classUserService.getClassBySchoolGrade(sid, gid);
		ret.put("code", CList.Api.Client.OK);
		if (null == classes) {
			return ret;
		}
		Map<String, List<Classes>> group = classes.stream()
				.filter(cl -> LocalDate.now().getYear() == cl.getStartYear().intValue()
						|| LocalDate.now().getYear() == cl.getEndYear().intValue())
				.collect(Collectors.groupingBy(cl -> cl.getTeacherId()));
		Map<String, List<Map<String, String>>> result = Maps.newHashMap();
		group.forEach((tid, cls) -> {
			if (null == cls) {
				return;
			}
			List<Map<String, String>> ms = cls.stream().map(cl -> {
				Map<String, String> m = Maps.newHashMap();
				m.put("id", cl.getId());
				m.put("name", cl.getName());
				return m;
			}).collect(Collectors.toList());
			result.put(tid, ms);
		});
		ret.put("teachers_classes", result);
		return ret;
	}

	@GetMapping(value = "/v2/class_user/classes", params = { "sid", "gid" }, produces = JSON_PRODUCES)
	public Map<String, Object> getClasses(String sid, String gid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(sid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "sid is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(gid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "gid is null");
			return ret;
		}
		List<Classes> classes = classUserService.getClassBySchoolGrade(sid, gid);
		ret.put("code", CList.Api.Client.OK);
		if (null == classes || classes.isEmpty()) {
			return ret;
		}

		List<Map<String, String>> clss = classes.stream().sorted((c1,c2) ->c1.getOrderBy().compareTo(c2.getOrderBy()))
				.map(cls -> {
		Map<String, String> c = Maps.newHashMap();
		c.put("id", cls.getId());
		c.put("name", cls.getName());
		c.put("teacher_name", cls.getTeacherName());
		return c;
	}).collect(Collectors.toList());	
		ret.put("classes", clss);
		return ret;
	}

	/**
	 * 
	 * @param sid
	 * @return
	 */
	@GetMapping(value = "/v2/class_user/classes", params = { "sid" }, produces = JSON_PRODUCES)
	public Map<String, Object> getSchoolClasses(String sid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(sid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "sid is null");
			return ret;
		}
		List<Classes> classes = classUserService.getClassBySchool(sid);
		ret.put("code", CList.Api.Client.OK);
		if (null == classes || classes.isEmpty()) {
			return ret;
		}
		List<Map<String, String>> clss = classes.stream().map(cls -> {
			Map<String, String> c = Maps.newHashMap();
			c.put("id", cls.getId());
			c.put("name", cls.getName());
			c.put("teacher_name", cls.getTeacherName());
			return c;
		}).collect(Collectors.toList());
		ret.put("classes", clss);
		return ret;
	}

	@GetMapping(value = "/v2/class_user/teachers", params = { "sid", "gid" }, produces = JSON_PRODUCES)
	public Map<String, Object> getClassTeachers(String sid, String gid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(sid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "sid is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(gid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "gid is null");
			return ret;
		}
		List<Map<String, String>> teachers = classUserService.getClassTeachers(sid, gid);
		ret.put("code", CList.Api.Client.OK);
		ret.put("teachers", teachers);
		return ret;
	}

	@GetMapping(value = "/v2/class_user/teachers", params = { "sid" }, produces = JSON_PRODUCES)
	public Map<String, Object> getSchoolTeachers(String sid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(sid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "sid is null");
			return ret;
		}
		List<Map<String, Object>> teachers = classUserService.getSchoolTeachers(sid);
		ret.put("code", CList.Api.Client.OK);
		ret.put("teachers", teachers);
		return ret;
	}

	@GetMapping(value = "/v2/basic_data", produces = JSON_PRODUCES)
	public Map<String, Object> get(String sid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(sid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "sid is null");
		}
		List<Map<String, Object>> teachers = classUserService.getSchoolTeachers(sid);
		ret.put("teacher_num", null == teachers ? 0 : teachers.size());

		int num = classUserService.getSchoolStudentNum(sid);
		ret.put("student_num", num);

		List<Grade> grades = classUserService.getGradesBySchool(sid);
		ret.put("grade_num", null == grades ? 0 : grades.size());
		if (null != grades) {
			List<Map<String, Object>> gs = grades.parallelStream().map(grade -> {
				Map<String, Object> m = Maps.newHashMap();
				m.put("id", grade.getId());
				m.put("name", grade.getName());
				m.put("code", grade.getCode());
				return m;
			}).collect(Collectors.toList());
			ret.put("grades", gs);
		}

		List<Classes> classes = classUserService.getClassBySchool(sid);
		ret.put("class_num", null == classes ? 0 : classes.size());
		if (null != classes) {
			List<Map<String, Object>> clss = classes.parallelStream().map(cls -> {
				Map<String, Object> m = Maps.newHashMap();
				m.put("id", cls.getId());
				m.put("name", cls.getName());
				m.put("grade", cls.getGrade().getName());
				return m;
			}).collect(Collectors.toList());
			ret.put("classes", clss);
		}

		if (null != teachers) {
			List<Map<String, Object>> teacherClassNums = teachers.parallelStream().map(teacher -> {
				Map<String, Object> m = Maps.newHashMap();
				m.put("id", teacher.getOrDefault("id", ""));
				m.put("name", teacher.getOrDefault("name", ""));
				List<Classes> cls = classUserService.getClassByTeacher((String) teacher.getOrDefault("id", ""));
				m.put("class_num", null == cls ? 0 : cls.size());
				if (null != cls) {
					List<Integer> nums = Lists.newArrayList();
					cls.forEach(cl -> {
						int n = classUserService.getStudentNum(cl.getId());
						nums.add(n);
					});
					m.put("student_num", nums.parallelStream().mapToInt(nu -> nu).sum());
				}
				return m;
			}).collect(Collectors.toList());
			ret.put("teacher_class_num", teacherClassNums);
		}

		List<BasicFacility> bfs = basicFacilityService.get(sid);
		if (null != bfs) {
			List<Integer> instruments = Lists.newArrayList();
			List<Map<String, Object>> maps = bfs.parallelStream().map(bf -> {
				Map<String, Object> m = Maps.newHashMap();
				m.put("title", bf.getTitle());
				m.put("num", bf.getNum());
				boolean isInstrument = bf.getIsInstrument();
				m.put("is_instrument", isInstrument);
				if (isInstrument) {
					instruments.add(bf.getNum());
				}
				return m;
			}).collect(Collectors.toList());
			ret.put("bfs", maps);
			ret.put("instrument_num", instruments.parallelStream().mapToInt(in -> in).sum());
		}
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	@GetMapping(value = "/v2/teacher_info", produces = JSON_PRODUCES)
	public Map<String, Object> getTeacherInfo(String sid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(sid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "sid is null");
		}
		List<Map<String, Object>> teachers = classUserService.getSchoolTeachers(sid);
		System.out.println("----------------teachers:" + teachers);
		ret.put("code", CList.Api.Client.OK);
		if (null == teachers) {
			return ret;
		}
		List<Map<String, Object>> teacherClassNums = teachers.parallelStream().map(teacher -> {
			Map<String, Object> m = Maps.newHashMap();
			m.putAll(teacher);
			List<Classes> cls = classUserService.getClassByTeacher((String) teacher.getOrDefault("id", ""));
			m.put("class_num", null == cls ? 0 : cls.size());
			if (null != cls) {
				List<Integer> nums = Lists.newArrayList();
				cls.forEach(cl -> {
					int n = classUserService.getStudentNum(cl.getId());
					nums.add(n);
				});
				m.put("student_num", nums.parallelStream().mapToInt(nu -> nu).sum());
				m.put("class_ids", cls.parallelStream().map(cl -> cl.getId()).collect(Collectors.toList()));
			}
			return m;
		}).collect(Collectors.toList());
		ret.put("teacher_class_num", teacherClassNums);

		Map<String, List<Map<String, Object>>> group = teachers.parallelStream()
				.collect(Collectors.groupingBy(teacher -> (String) teacher.get("gender")));
		Map<String, Object> genders = Maps.newHashMap();
		group.forEach((gender, value) -> {
			genders.put(gender, value.size());
		});
		ret.put("genders", genders);

		group = teachers.parallelStream().collect(Collectors.groupingBy(teacher -> (String) teacher.get("degree")));
		Map<String, Object> degrees = Maps.newHashMap();
		group.forEach((degree, value) -> {
			degrees.put(degree, value.size());
		});
		ret.put("degrees", degrees);

		Map<String, Integer> ages = Maps.newHashMap();
		teachers.forEach(teacher -> {
			Integer age = (Integer) teacher.getOrDefault("age", 0);
			if (age <= 30) {
				Integer count = ages.getOrDefault("20-30", 0);
				ages.put("20-30", ++count);
				return;
			} else if (age <= 40) {
				Integer count = ages.getOrDefault("30-40", 0);
				ages.put("30-40", ++count);
				return;
			} else if (age <= 50) {
				Integer count = ages.getOrDefault("40-50", 0);
				ages.put("40-50", ++count);
				return;
			} else {
				Integer count = ages.getOrDefault("50+", 0);
				ages.put("50+", ++count);
			}
		});
		ret.put("ages", ages);
		return ret;
	}

	@GetMapping(value = "/v2/class_info", produces = JSON_PRODUCES)
	public Map<String, Object> getClassInfo(String sid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(sid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "sid is null");
		}
		List<Classes> classes = classUserService.getClassBySchool(sid);
		ret.put("code", CList.Api.Client.OK);
		if (null == classes) {
			return ret;
		}
		Map<String, Object> man = Maps.newHashMap();
		Map<String, Object> woman = Maps.newHashMap();
		List<Map<String, Object>> clss = classes.parallelStream().map(cls -> {
			Map<String, Object> m = Maps.newHashMap();
			m.put("id", cls.getId());
			m.put("name", cls.getName());
			m.put("grade", cls.getGrade().getName());
			m.put("teacher", cls.getTeacherName());
			m.put("student_num", classUserService.getStudentNum(cls.getId()));
			Map<String, Integer> genders = classUserService.getManAndWomanStudentNum(cls.getId());
			if (null == genders) {
				m.put("MAN", 0);
				m.put("WOMAN", 0);
			} else {
				m.putAll(genders);
			}
			String grade = cls.getGrade().getName();
			Integer count = (Integer) man.getOrDefault(grade, 0);
			count = null == count ? 0 : count;
			if (null == genders) {
				man.put(grade, count);
			} else {
				Integer w = genders.get("MAN");
				w = null == w ? 0 : w;
				man.put(grade, count + w);
			}
			count = (Integer) woman.getOrDefault(grade, 0);
			count = null == count ? 0 : count;
			if (null == genders) {
				woman.put(grade, count);
			} else {
				Integer w = genders.get("WOMAN");
				w = null == w ? 0 : w;
				woman.put(grade, count + w);
			}
			return m;
		}).collect(Collectors.toList());
		ret.put("classes", clss);
		ret.put("grade_man", man);
		ret.put("grade_woman", woman);
		List<Map<String, Object>> teachers = classUserService.getSchoolTeachers(sid);
		if (null != teachers) {
			List<Map<String, Object>> teacherClassNums = teachers.parallelStream().map(teacher -> {
				Map<String, Object> m = Maps.newHashMap();
				m.put("id", teacher.getOrDefault("id", ""));
				m.put("name", teacher.getOrDefault("name", ""));
				List<Classes> cls = classUserService.getClassByTeacher((String) teacher.getOrDefault("id", ""));
				m.put("class_num", null == cls ? 0 : cls.size());
				if (null != cls) {
					List<Integer> nums = Lists.newArrayList();
					cls.forEach(cl -> {
						int n = classUserService.getStudentNum(cl.getId());
						nums.add(n);
					});
					m.put("student_num", nums.parallelStream().mapToInt(nu -> nu).sum());
				}
				return m;
			}).collect(Collectors.toList());
			ret.put("teacher_class_num", teacherClassNums);
		}
		return ret;
	}

}
