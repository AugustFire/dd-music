package com.nercl.music.api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nercl.music.constant.CList;
import com.nercl.music.entity.authorize.Topic;
import com.nercl.music.service.TopicService;

@RestController
public class ApiTopicController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private TopicService topicService;

	@GetMapping(value = "/api/topics", produces = JSON_PRODUCES)
	public Map<String, Object> getTopics(Integer exerciser_type, String exerciser_id) throws IOException {
		Map<String, Object> ret = Maps.newHashMap();
		if (null == exerciser_type || exerciser_type <= 0 || Strings.isNullOrEmpty(exerciser_id)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "练习类别参数为空或者练习者id为空");
			return ret;
		}
		List<Topic> topics = topicService.list(exerciser_type);
		ret.put("code", CList.Api.Client.OK);
		if (null != topics) {
			List<Map<String, Object>> list = Lists.newArrayList();
			ret.put("topics", list);
			List<Topic> payedTopics = topicService.getPayed(exerciser_id);
			topics.forEach(topic -> {
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", topic.getId());
				map.put("subject_type", topic.getSubjectType());
				map.put("title", topic.getTitle());
				map.put("fee", topic.getFee());
				map.put("year", topic.getYear());
				map.put("area", topic.getArea());
				map.put("is_payed", false);
				if (null != payedTopics) {
					payedTopics.forEach(payedTopic -> {
				        if (payedTopic.getId().equals(topic.getId())) {
					        map.put("is_payed", true);
				        }
			        });
				}
				list.add(map);
			});
		}
		return ret;
	}

}
