package com.nercl.music.cloud.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nercl.music.cloud.entity.user.Person;
import com.nercl.music.cloud.service.PersonService;
import com.nercl.music.constant.CList;
import com.nercl.music.util.page.PaginateSupportArray;

@RestController
public class UserManagerController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private PersonService personService;

	@GetMapping(value = "/users", params = { "key" }, produces = JSON_PRODUCES)
	public Map<String, Object> query(@RequestParam(value = "key") String key,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page, Model model) {
		List<Person> persons = personService.query(key, page);
		Map<String, Object> ret = Maps.newHashMap();
		ret.put("code", CList.Api.Client.OK);
		if (null != persons) {
			List<Map<String, Object>> list = Lists.newArrayList();
			ret.put("users", list);
			persons.forEach(person -> {
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", person.getId());
				map.put("name", person.getName());
				map.put("email", person.getEmail());
				map.put("phone", person.getPhone());
				list.add(map);
			});
			if (persons instanceof PaginateSupportArray) {
				ret.put("count", ((PaginateSupportArray<Person>) persons).getTotal());
				ret.put("pages", ((PaginateSupportArray<Person>) persons).getPage());
				ret.put("page_size", ((PaginateSupportArray<Person>) persons).getPageSize());
			}
		}
		return ret;
	}

}
