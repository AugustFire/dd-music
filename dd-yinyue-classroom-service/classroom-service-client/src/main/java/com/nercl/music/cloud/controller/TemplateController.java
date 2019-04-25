package com.nercl.music.cloud.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nercl.music.cloud.entity.classroom.Chapter;
import com.nercl.music.cloud.entity.template.ClassroomTemplateChapterStatus;
import com.nercl.music.cloud.entity.template.Template;
import com.nercl.music.cloud.entity.template.TemplateChapterResource;
import com.nercl.music.cloud.entity.template.TemplateParentChapterBookClassRoom;
import com.nercl.music.cloud.service.ChapterService;
import com.nercl.music.cloud.service.ClassroomTemplateChapterStatusService;
import com.nercl.music.cloud.service.TemplateChapterResourceService;
import com.nercl.music.cloud.service.TemplateParentChapterBookClassRoomService;
import com.nercl.music.cloud.service.TemplateService;
import com.nercl.music.constant.ApiClient;
import com.nercl.music.constant.CList;

@RestController
public class TemplateController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private TemplateService templateService;

	@Autowired
	private TemplateParentChapterBookClassRoomService templateParentChapterBookClassRoomService;

	@Autowired
	private TemplateChapterResourceService templateChapterResourceService;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ClassroomTemplateChapterStatusService classroomTemplateChapterStatusService;

	@Autowired
	private ChapterService chapterService;

	@Value("${dd-yinyue.zip}")
	private String zipFilePath;

	@GetMapping(value = "/has_default_template", produces = JSON_PRODUCES)
	public Map<String, Object> hasDefaultTemplate(String bookId, String chapterId) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(bookId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "bookId is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(chapterId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "chapterId is null");
			return ret;
		}
		boolean hasDefaultTemplate = templateService.hasDefaultTemplate(bookId, chapterId);
		ret.put("code", CList.Api.Client.OK);
		ret.put("has_default_template", hasDefaultTemplate);
		return ret;
	}

	@GetMapping(value = "/default_template", produces = JSON_PRODUCES)
	public Map<String, Object> getDefaultTemplate(String bookId, String chapterId, String roomId) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(bookId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "bookId is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(chapterId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "chapterId is null");
			return ret;
		}
		List<Map<String, Object>> templates = templateService.getDefaultTemplate(bookId, chapterId, roomId);
		ret.put("code", CList.Api.Client.OK);
		ret.put("templates", templates);
		return ret;
	}

	/**
	 * 根据课堂Id和章节Id获取templates
	 * 
	 * @param classroomId
	 *            课堂Id
	 * @param chapterId
	 *            章节Id
	 */
	@GetMapping(value = "/templates/{cid}", produces = JSON_PRODUCES)
	public Map<String, Object> getTemplatesByCondition(@PathVariable String cid, @RequestParam String classroomId) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(classroomId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "classroomId is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(cid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "chapterId is null");
			return ret;
		}
		List<TemplateParentChapterBookClassRoom> classRoomTemplate = templateParentChapterBookClassRoomService
				.getClassRoomTemplate(classroomId, cid);
		List<Template> listTemplate = Lists.newArrayList();
		if (null != classRoomTemplate) {
			listTemplate = classRoomTemplate.stream().map(TemplateParentChapterBookClassRoom::getTemplate)
					.collect(Collectors.toList());
		}
		ret.put("listTemplate", listTemplate);
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	@PostMapping(value = "/default_template", produces = JSON_PRODUCES)
	public Map<String, Object> addDefaultTemplate(String bookId, String chapterId, String title, String json,
			HttpServletRequest request) throws IOException {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(bookId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "bookId is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(chapterId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "chapterId is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(title)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "title is null");
			return ret;
		}
		String tid = templateService.addDefaultTemplate(bookId, chapterId, title, json);
		if (Strings.isNullOrEmpty(tid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "add default template failed");
		} else {
			ret.put("code", CList.Api.Client.OK);
			ret.put("tid", tid);
		}
		return ret;
	}

	@PutMapping(value = "/template/{tid}", produces = JSON_PRODUCES)
	public Map<String, Object> update(@PathVariable String tid, String title, String json) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(tid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "tid is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(title)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "title is null");
			return ret;
		}
		boolean success = templateService.updateTemplate(tid, title, json);
		if (success) {
			ret.put("code", CList.Api.Client.OK);
		} else {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "update template failed");
		}
		return ret;
	}

	@DeleteMapping(value = "/template/{tid}", produces = JSON_PRODUCES)
	public Map<String, Object> delete(@PathVariable String tid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(tid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "tid is null");
			return ret;
		}
		boolean success = templateService.deleteTemplate(tid);
		if (success) {
			ret.put("code", CList.Api.Client.OK);
		} else {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "delete template failed");
		}
		return ret;
	}

	@GetMapping(value = "/template", produces = JSON_PRODUCES)
	public Map<String, Object> getTemplate(String classRoomId, String chapterId) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(classRoomId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "classRoomId is null");
			return ret;
		}
		List<Map<String, Object>> templates = templateService.getClassRoomTemplate(classRoomId, chapterId);
		// List<TemplateParentChapterBookClassRoom> classRoomTemplate =
		// templateParentChapterBookClassRoomService
		// .getClassRoomTemplate(classRoomId, chapterId);
		// List<Template> templates = Lists.newArrayList();
		// if (null != classRoomTemplate) {
		// templates =
		// classRoomTemplate.stream().map(TemplateParentChapterBookClassRoom::getTemplate)
		// .collect(Collectors.toList());
		// }
		ret.put("code", CList.Api.Client.OK);
		if (null != templates && !templates.isEmpty()) {
			ret.put("templates", templates);
		}
		return ret;
	}

	@PostMapping(value = "/personal_template", produces = JSON_PRODUCES)
	public Map<String, Object> addPersonalTemplate(String teacherId, String bookId, String classroomId, String title,
			String json) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(teacherId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "teacherId is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(title)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "title is null");
			return ret;
		}
		boolean success = templateService.addPersonalTemplate(teacherId, bookId, classroomId, title, json);
		if (success) {
			ret.put("code", CList.Api.Client.OK);
		} else {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "add default template failed");
		}
		return ret;
	}

	@PostMapping(value = "/template_classroom", produces = JSON_PRODUCES)
	public Map<String, Object> setTemplateClassRoom(String templateId, String classroomId, String chapterId) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(templateId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "templateId is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(classroomId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "classroomId is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(chapterId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "chapterId is null");
			return ret;
		}
		boolean success = templateService.setClassRoomTemplate(templateId, classroomId, chapterId);
		if (success) {
			ret.put("code", CList.Api.Client.OK);
		} else {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "set templateClassRoom failed");
		}
		return ret;
	}

	@PutMapping(value = "/template/resource", produces = JSON_PRODUCES)
	public Map<String, Object> updateTemplateResource(String oldRid, String newRid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(oldRid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "oldRid is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(newRid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "newRid is null");
			return ret;
		}
		templateService.updateResource(oldRid, newRid);
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = "/template_chapter_resource/{tid}", produces = JSON_PRODUCES)
	public Map<String, Object> getTemplateChapterResources(@PathVariable String tid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(tid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "tid is null");
			return ret;
		}
		TemplateChapterResource tcr = new TemplateChapterResource();
		tcr.setTemplateId(tid);
		try {
			List<TemplateChapterResource> list = templateChapterResourceService.findByConditions(tcr);
			List<Map<String, Object>> tcrList = Lists.newArrayList();
			if (null != list && !list.isEmpty()) {
				list.forEach(l -> {
					Map<String, Object> resource = restTemplate.getForObject(ApiClient.GET_RESOURCE, Map.class,
							l.getResourceId());
					Map<String, Object> tcrMap = Maps.newHashMap();
					Chapter chapter = l.getChapter();
					chapter.setChildren(null);
					chapter.setParent(null);
					tcrMap.put("chapter", chapter);
					tcrMap.put("resource", resource);
					tcrList.add(tcrMap);
				});
				ret.put("code", CList.Api.Client.OK);
				ret.put("listTemplateChapterResource", tcrList);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "internal exception has occured");
		}
		return ret;
	}

	@PutMapping(value = "/template/default/{tid}", produces = JSON_PRODUCES)
	public Map<String, Object> setDefaultTemplate(@PathVariable String tid, String classroomId, String chapterId) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(tid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "tid is null");
			return ret;
		}

		if (Strings.isNullOrEmpty(classroomId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "classroomId is null");
			return ret;
		}

		if (Strings.isNullOrEmpty(chapterId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "chapterId is null");
			return ret;
		}
		// 校验课堂中是否有模板
		List<TemplateParentChapterBookClassRoom> classRoomTemplate = templateParentChapterBookClassRoomService
				.getClassRoomTemplate(classroomId, chapterId);
		if (null == classRoomTemplate || classRoomTemplate.isEmpty()) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "no template in chapter");
			return ret;
		}

		// 校验要设置的模板是否存在
		Template template = templateService.findById(tid);
		if (null == template) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "template not exist");
			return ret;
		}

		// 将章节中的所有id与tid不同的默认模板取出来
		List<Template> defaultTemplate = classRoomTemplate.stream().map(TemplateParentChapterBookClassRoom::getTemplate)
				.filter((t) -> {
					return t.getIsDefault() && !t.getId().equals(tid);
				}).collect(Collectors.toList());
		if (null != defaultTemplate && !defaultTemplate.isEmpty()) {
			templateService.setDefaultTemplate(template, defaultTemplate);
		} else {
			template.setIsDefault(true);
			templateService.update(template);
		}
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	@PutMapping(value = "/template/set_status", produces = JSON_PRODUCES)
	public Map<String, Object> setChapterResourceStatus(String rid, @RequestParam("template_id") String templateId,
			@RequestParam("chapter_id") String chapterId, String status) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(rid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "rid is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(templateId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "templateId is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(chapterId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "chapterId is null");
			return ret;
		}
		if (!ClassroomTemplateChapterStatus.ChapterResourceStatus.isDefined(status)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "no enum constent:" + status);
			return ret;
		}
		classroomTemplateChapterStatusService.setStatus(rid, templateId, chapterId,
				ClassroomTemplateChapterStatus.ChapterResourceStatus.valueOf(status));
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	/**
	 * 根据章节id查询章节完成百分比
	 * 
	 * @param cid
	 *            章节id
	 */
	@GetMapping(value = "/template/completion_rate", produces = JSON_PRODUCES)
	public Map<String, Object> getChapterCompletionRate(String rid, String tid, String cid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(rid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "rid is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(tid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "tid is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(cid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "cid is null");
			return ret;
		}
		List<Chapter> children = chapterService.getChildrenChapters(cid);
		if (null == children || children.isEmpty()) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "no chapter exsit");
			return ret;
		}
		List<ClassroomTemplateChapterStatus> ctcss = classroomTemplateChapterStatusService.getByParentChapter(rid, tid,
				cid);
		if (null == ctcss || ctcss.isEmpty()) {
			ret.put("code", CList.Api.Client.OK);
			ret.put("completion_rate", 0);
			return ret;
		}
		long count = ctcss.stream()
				.filter(ctcs -> ClassroomTemplateChapterStatus.ChapterResourceStatus.COMPLETION == ctcs.getStatus())
				.count();
		ret.put("code", CList.Api.Client.OK);
		ret.put("completion_rate", (Integer) (Long.valueOf(count).intValue() * 100 / children.size()));
		return ret;
	}
}
