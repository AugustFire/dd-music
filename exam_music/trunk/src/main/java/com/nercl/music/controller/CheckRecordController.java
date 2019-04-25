package com.nercl.music.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nercl.music.entity.CheckRecord;
import com.nercl.music.entity.RequiredPrivilege;
import com.nercl.music.entity.user.Role;
import com.nercl.music.service.CheckRecordService;

@Controller
public class CheckRecordController {

	@Autowired
	private CheckRecordService checkRecordService;

	@GetMapping(value = "/checkRecords/{id}")
	@ResponseBody
	@RequiredPrivilege(Role.MANAGER)
	public List<Map<String, Object>> getCheckRecordsById(@PathVariable String id) {
		List<Map<String, Object>> list = Lists.newArrayList();
		List<CheckRecord> checkRecords = checkRecordService.getRecordsById(id);
		if (null == checkRecords || checkRecords.isEmpty()) {
			return list;
		}
		checkRecords.forEach(checkRecord -> {
			Map<String, Object> cr = Maps.newHashMap();
			cr.put("name", checkRecord.getCheckUser().getName());
			cr.put("email", checkRecord.getCheckUser().getEmail());
			cr.put("status", checkRecord.getStatus().getDesc());
			cr.put("time", checkRecord.getCheckAt());
			cr.put("un_pass_reason", checkRecord.getUnPassReason());
			list.add(cr);
		});
		return list;
	}

}
