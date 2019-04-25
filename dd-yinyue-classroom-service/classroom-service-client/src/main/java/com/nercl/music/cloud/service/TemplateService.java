package com.nercl.music.cloud.service;

import java.util.List;
import java.util.Map;

import com.nercl.music.cloud.entity.template.Template;

public interface TemplateService {

	List<Map<String, Object>> getDefaultTemplate(String bookId, String chapterId, String roomId);

	Template getDefaultTemplate(String bookId, String chapterId);

	List<Map<String, Object>> getDefaultTemplate(String bookId);

	boolean hasDefaultTemplate(String bookId, String chapterId);

	String addDefaultTemplate(String bookId, String chapterId, String title, String json);

	boolean deleteTemplate(String tid);

	boolean updateTemplate(String tid, String title, String json);

	boolean addPersonalTemplate(String teacherId, String bookId, String classroomId, String title, String json);

	List<Map<String, Object>> getClassRoomTemplate(String classroomId, String chapterId);

	boolean setClassRoomTemplate(String templateId, String classroomId, String chapterId);

	void saveChapterTemplateQuestionRelation(String chapterId, String templateId, String questionId);

	void updateResource(String oldRid, String newRid);

	Template findById(String tid);

	void update(Template template);

	/**
	 * 将此模板设置成默认模板
	 * 
	 * @param template
	 *            将要被设置成默认模板的模板
	 * @param defaultTemplates
	 *            将要取消默认的模板列表
	 */
	void setDefaultTemplate(Template template, List<Template> defaultTemplates);

}
