package com.nercl.music.controller;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nercl.music.constant.CList;
import com.nercl.music.entity.authorize.Topic;
import com.nercl.music.entity.authorize.TopicSubject;
import com.nercl.music.exception.CommonLogicException;
import com.nercl.music.service.TopicService;
import com.nercl.music.service.TopicSubjectService;
import com.nercl.music.util.AJaxReturn;

@Controller
public class TopicController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private TopicService topicService;

	@Autowired
	private TopicSubjectService topicSubjectService;

	@GetMapping("/topics")
	public String getTopics(@RequestParam(value = "page", required = false, defaultValue = "1") int page, Model model) {
		List<Topic> topics = topicService.list(page);
		model.addAttribute("topics", topics);
		if (null != topics) {
			Map<String, String> ret = Maps.newHashMap();
			topics.forEach(topic -> {
				List<TopicSubject> topicSubjects = topicSubjectService.getByTopic(topic.getId());
				if (null != topicSubjects) {
					List<String> subjects = topicSubjects.stream().map(topicSubject -> {
				        return topicSubject.getSubjectTitle();
			        }).collect(Collectors.toList());
					ret.put(topic.getId(), Joiner.on(",").join(subjects));
				}
			});
			model.addAttribute("subjects", ret);
		}
		return "topic/topics";
	}

	@GetMapping("/topic")
	public String toSave(Model model) {
		int currentYear = LocalDate.now().getYear();
		model.addAttribute("currentYear", currentYear);
		return "topic/topic";
	}

	@PostMapping("/topic")
	public String save(Integer subjectType, String title, Integer year, String area, Integer fee, String[] sids,
	        Date startAt, Date endAt, HttpSession session, RedirectAttributes redirectAttr) {
		long start = 0L;
		long end = 0L;
		topicService.save(subjectType, title, year, area, fee, sids, start, end);
		return "redirect:/topics";
	}

	@GetMapping("/topic/{tid}")
	public String toUpdate(@PathVariable String tid, Model model) {
		Topic topic = topicService.get(tid);
		if (null != topic) {
			model.addAttribute("tid", tid);
			model.addAttribute("topic", topic);
			List<TopicSubject> topicSubjects = topicSubjectService.getByTopic(tid);
			if (null != topicSubjects) {
				List<String> subjects = topicSubjects.stream().map(topicSubject -> {
					return topicSubject.getSubjectTitle();
				}).collect(Collectors.toList());
				model.addAttribute("subjects", Joiner.on(",").join(subjects));

				List<String> subjectIds = topicSubjects.stream().map(topicSubject -> {
					return topicSubject.getSubjectId();
				}).collect(Collectors.toList());
				model.addAttribute("subjectIds", Joiner.on(",").join(subjectIds));
			}
		}
		int currentYear = LocalDate.now().getYear();
		model.addAttribute("currentYear", currentYear);
		return "topic/topic";
	}

	@PutMapping("/topic/{tid}")
	public String update(@PathVariable String tid, Integer subjectType, String title, Integer year, String area,
	        String[] sids, HttpSession session, RedirectAttributes redirectAttr) {
		long start = 0L;
		long end = 0L;
		topicService.update(tid, subjectType, title, year, area, sids, start, end);
		return "redirect:/topics";
	}

	@PostMapping("/topic/{tid}/set_questions")
	public String setQuestions(@PathVariable String tid, String[] qids) {
		if (Strings.isNullOrEmpty(tid)) {
			throw CommonLogicException.create(500, "tid is null or qids is null");
		}
		topicService.setQuestions(tid, qids);
		return "redirect:/topics";
	}

	@GetMapping(value = "/topics", params = { "title" })
	public String getByTitle(@RequestParam(value = "title", required = true) String title, Integer subjectType,
	        @RequestParam(value = "page", required = false, defaultValue = "1") int page, Model model) {
		List<Topic> topics = topicService.getByTitle(subjectType, title, page);
		model.addAttribute("topics", topics);
		if (null != topics) {
			Map<String, String> ret = Maps.newHashMap();
			topics.forEach(topic -> {
				List<TopicSubject> topicSubjects = topicSubjectService.getByTopic(topic.getId());
				if (null != topicSubjects) {
					List<String> subjects = topicSubjects.stream().map(topicSubject -> {
				        return topicSubject.getSubjectTitle();
			        }).collect(Collectors.toList());
					ret.put(topic.getId(), Joiner.on(",").join(subjects));
				}
			});
			model.addAttribute("subjects", ret);
		}
		model.addAttribute("title", title);
		return "topic/topics";
	}

	@GetMapping(value = "/topics", params = { "key" }, produces = JSON_PRODUCES)
	@ResponseBody
	public Map<String, Object> query(@RequestParam(value = "key") String key) {
		List<Topic> topics = topicService.query(key);
		Map<String, Object> ret = Maps.newHashMap();
		ret.put("code", CList.Api.Client.OK);
		if (null != topics) {
			List<Map<String, Object>> list = Lists.newArrayList();
			ret.put("topics", list);
			topics.forEach(topic -> {
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", topic.getId());
				map.put("title", topic.getTitle());
				map.put("fee", topic.getFee());
				list.add(map);
			});
		}
		return ret;
	}

	@GetMapping(value = "/topics/json", produces = JSON_PRODUCES)
	@ResponseBody
	public Map<String, Object> getTopics() {
		List<Topic> topics = topicService.list();
		Map<String, Object> ret = Maps.newHashMap();
		ret.put("code", CList.Api.Client.OK);
		if (null != topics) {
			List<Map<String, Object>> list = Lists.newArrayList();
			ret.put("topics", list);
			topics.forEach(topic -> {
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", topic.getId());
				map.put("title", topic.getTitle());
				map.put("fee", topic.getFee());
				list.add(map);
			});
		}
		return ret;
	}

	@GetMapping(value = "/topic/{tid}/questions", produces = JSON_PRODUCES)
	@ResponseBody
	public Map<String, Object> getQuestions(@PathVariable String tid) {
		Map<String, Object> ret = Maps.newHashMap();
		ret.put("code", CList.Api.Client.OK);
		List<String> ids = topicService.getQuestions(tid);
		ret.put("ids", ids);
		return ret;
	}

	@GetMapping(value = "/topics", params = { "year" })
	@ResponseBody
	public List<Map<String, String>> json(@RequestParam(value = "year") Integer year) {
		List<Topic> topics = this.topicService.getByYear(year);
		List<Map<String, String>> ret = Lists.newArrayList();
		topics.forEach(topic -> {
			Map<String, String> map = Maps.newHashMap();
			map.put("id", topic.getId());
			map.put("title", topic.getTitle());
			ret.add(map);
		});
		return ret;
	}
    /**
     * 删除一条topic
     */
    @DeleteMapping(value = "/topic/remove_topic")
    @ResponseBody
    public AJaxReturn<Integer> remove(@RequestParam(value = "topicId") String topicId){
        AJaxReturn<Integer> aJaxReturn = new AJaxReturn<Integer>();
        try{
            topicService.deleteTopicById(topicId);
            aJaxReturn.setReturnCode(CList.ReturnConstant.RETURN_SUCCESS);
            aJaxReturn.setReturnMsg("删除成功");
        }catch (Exception e){
            aJaxReturn.setReturnCode(CList.ReturnConstant.RETURN_FAIL_SERVICE);
            aJaxReturn.setReturnMsg("删除失败");
        }
        return aJaxReturn;
    }
}
