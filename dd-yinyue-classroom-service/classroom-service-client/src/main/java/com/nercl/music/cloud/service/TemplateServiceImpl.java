package com.nercl.music.cloud.service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.nercl.music.cloud.dao.TemplateChapterQuestionRelationDao;
import com.nercl.music.cloud.dao.TemplateDao;
import com.nercl.music.cloud.entity.classroom.Chapter;
import com.nercl.music.cloud.entity.classroom.ClassRoom;
import com.nercl.music.cloud.entity.classroom.TeachForm;
import com.nercl.music.cloud.entity.template.ClassroomTemplateChapterStatus;
import com.nercl.music.cloud.entity.template.Template;
import com.nercl.music.cloud.entity.template.TemplateChapterQuestionRelation;
import com.nercl.music.cloud.entity.template.TemplateChapterResource;
import com.nercl.music.cloud.entity.template.TemplateParentChapterBookClassRoom;
import com.nercl.music.cloud.entity.template.TemplateSongImpress;
import com.nercl.music.constant.ApiClient;
import com.nercl.music.constant.CList;

@Service
@Transactional
public class TemplateServiceImpl implements TemplateService {

	@Autowired
	private ClassRoomService classRoomService;

	@Autowired
	private TemplateDao templateDao;

	@Autowired
	private TemplateChapterResourceService templateChapterResourceService;

	@Autowired
	private TemplateSongImpressService songImpressTemplateService;

	@Autowired
	private Gson gson;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ChapterService chapterService;

	@Autowired
	private TemplateChapterQuestionRelationDao templateChapterQuestionRelationDao;

	@Autowired
	private TemplateParentChapterBookClassRoomService templateParentChapterBookClassRoomService;

	@Autowired
	private ClassroomTemplateChapterStatusService classroomTemplateChapterStatusService;

	@Override
	public List<Map<String, Object>> getDefaultTemplate(String bookId, String chapterId, String roomId) {
		TemplateParentChapterBookClassRoom tpcbc = templateParentChapterBookClassRoomService.getBookTemplate(bookId,
				chapterId);
		if (null == tpcbc) {
			return null;
		}
		List<Template> templates = Lists.newArrayList(tpcbc.getTemplate());
		if (null == templates || templates.isEmpty()) {
			return null;
		}
		List<Map<String, Object>> list = parse(templates, roomId);
		return list;
	}

	@Override
	public Template getDefaultTemplate(String bookId, String chapterId) {
		TemplateParentChapterBookClassRoom tpcbc = templateParentChapterBookClassRoomService.getBookTemplate(bookId,
				chapterId);
		if (null != tpcbc) {
			return tpcbc.getTemplate();
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> getDefaultTemplate(String bookId) {
		List<TemplateParentChapterBookClassRoom> tpcbcs = templateParentChapterBookClassRoomService
				.getBookTemplate(bookId);
		if (null == tpcbcs || tpcbcs.isEmpty()) {
			return null;
		}
		List<Template> templates = tpcbcs.stream().map(tpcbc -> tpcbc.getTemplate()).collect(Collectors.toList());
		List<Map<String, Object>> list = parse(templates, tpcbcs.get(0).getClassRoomId());
		return list;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> parse(Template template, String roomId) {
		Map<String, Object> ret = Maps.newHashMap();
		ret.put("id", template.getId());
		ret.put("title", template.getTitle());
		ret.put("is_default", template.getIsDefault());
		List<TemplateChapterResource> templateChapterResources = templateChapterResourceService
				.getByTemplate(template.getId());
		if (null == templateChapterResources || templateChapterResources.isEmpty()) {
			return ret;
		}
		List<Chapter> parents = templateChapterResources.stream().map(crt -> {
			Chapter chapter = crt.getChapter();
			if (null != chapter) {
				return chapter.getParent();
			} else {
				return null;
			}
		}).filter(crt -> null != crt).distinct().collect(Collectors.toList());

		if (null == parents || parents.isEmpty()) {
			return ret;
		}

		List<Map<String, Object>> chapterResources = Lists.newArrayList();
		ret.put("chapter_resources", chapterResources);
		parents.forEach(parent -> {
			Map<String, Object> map = Maps.newHashMap();
			chapterResources.add(map);
			map.put("pid", parent.getId());
			map.put("ptitle", parent.getTitle());
			TeachForm teachForm = parent.getTeachForm();
			if (null != teachForm) {
				map.put("teach_form", teachForm);
			}
			List<Chapter> children = parent.getChildren();
			if (null == children || children.isEmpty()) {
				return;
			}
			List<Map<String, Object>> clist = Lists.newArrayList();
			map.put("children", clist);
			children.forEach(child -> {
				Map<String, Object> m = Maps.newHashMap();
				clist.add(m);
				m.put("cid", child.getId());
				m.put("ctitle", child.getTitle());
				TeachForm cTeachForm = child.getTeachForm();
				if (null != cTeachForm) {
					m.put("teach_form", cTeachForm);
				}
				ClassroomTemplateChapterStatus status = classroomTemplateChapterStatusService.get(roomId,
						template.getId(), child.getId());
				m.put("status", null == status ? ClassroomTemplateChapterStatus.ChapterResourceStatus.UNCOMPLETION
						: status.getStatus());
				templateChapterResources.stream().filter(crt -> crt.getChapterId().equals(child.getId()))
						.forEach(crt -> {
							String csongId = crt.getSongId();
							if (!Strings.isNullOrEmpty(csongId)) {
								m.put("song_id", csongId);

								Map<String, Object> res = restTemplate.getForObject(ApiClient.GET_RESOURCE_SONG,
										Map.class, csongId);
								if (null != res && null != res.get("id")) {
									m.put("song_name", res.getOrDefault("name", ""));
								}

								List<TemplateSongImpress> sits = songImpressTemplateService.get(template.getId(),
										csongId);

								if (null != sits && !sits.isEmpty()) {
									List<Map<String, Object>> impresses = Lists.newArrayList();
									m.put("impresses", impresses);
									sits.forEach(sit -> {
										Map<String, Object> impressMap = restTemplate.getForObject(
												ApiClient.GET_RESOURCE_IMPRESS, Map.class, sit.getImpressId());
										if (null != impressMap
												&& impressMap.getOrDefault("code", 0).equals(CList.Api.Client.OK)) {
											impresses.add(
													((Map<String, Object>) impressMap.getOrDefault("impress", null)));
										}
									});
								}
							}
							String cresourceId = crt.getResourceId();
							if (!Strings.isNullOrEmpty(cresourceId)) {
								m.put("resource_id", cresourceId);

								Map<String, Object> res = restTemplate.getForObject(ApiClient.GET_RESOURCE, Map.class,
										cresourceId);
								if (null != res && res.getOrDefault("code", 0).equals(CList.Api.Client.OK)) {
									m.put("resource_name", res.getOrDefault("name", ""));
									m.put("resource_ext", res.getOrDefault("ext", ""));
								}
							}
						});

				List<TemplateChapterQuestionRelation> ctqrs = templateChapterQuestionRelationDao
						.getByTemplateChapter(template.getId(), child.getId());
				if (null == ctqrs || ctqrs.isEmpty()) {
					return;
				}
				List<String> qids = ctqrs.stream().map(ctqr -> ctqr.getQuestionId()).collect(Collectors.toList());
				if (null == qids || qids.isEmpty()) {
					return;
				}
				List<Object> questions = Lists.newArrayList();
				System.out.println("-------qids:" + qids);
				qids.forEach(qid -> {
					Map<String, Object> res = restTemplate.getForObject(ApiClient.GET_QUESTION, Map.class, qid);
					if (null != res) {
						questions.add(res);
					}
				});
				m.put("questions", questions);

				// Map<String, Object> res =
				// restTemplate.getForObject(ApiClient.GET_QUESTIONS, Map.class,
				// Joiner.on(",").join(qids));
				// if (null != res) {
				// m.put("questions", res.get("questions"));
				// }
			});
		});
		return ret;
	}

	private List<Map<String, Object>> parse(List<Template> templates, String classroomId) {
		List<Map<String, Object>> list = Lists.newArrayList();
		templates.forEach(template -> {
			list.add(parse(template, classroomId));
		});
		return list;
	}

	@Override
	public boolean hasDefaultTemplate(String bookId, String chapterId) {
		return null != templateParentChapterBookClassRoomService.getBookTemplate(bookId, chapterId);
	}

	@Override
	public String addDefaultTemplate(String bookId, String chapterId, String title, String json) {
		boolean hasDefault = hasDefaultTemplate(bookId, chapterId);
		if (hasDefault) {
			return "";
		}
		Template template = new Template();
		template.setTitle(title);
		template.setIsDefault(true);
		template.setCreateAt(System.currentTimeMillis());
		templateDao.save(template);
		if (Strings.isNullOrEmpty(template.getId())) {
			return "";
		}
		boolean success = templateParentChapterBookClassRoomService.saveBookTemplate(bookId, chapterId,
				template.getId());
		if (!success) {
			return "";
		}
		if (Strings.isNullOrEmpty(json)) {
			return template.getId();
		}
		readJson(json, template.getId());
		return template.getId();
	}

	@SuppressWarnings("unchecked")
	private void readJson(String json, String templateId) {
		Map<String, Object> map = gson.fromJson(json, Map.class);
		List<Map<String, Object>> relations = (List<Map<String, Object>>) map.getOrDefault("relations", null);
		if (null == relations || relations.isEmpty()) {
			return;
		}
		relations.forEach(relation -> {
			String parentId = (String) relation.getOrDefault("parent_chapter_id", "");
			String chapterId = (String) relation.getOrDefault("chapter_id", "");
			String title = (String) relation.getOrDefault("chapter_title", "");
			String songId = (String) relation.getOrDefault("song_id", "");
			String resourceId = (String) relation.getOrDefault("resource_id", "");
			if (Strings.isNullOrEmpty(chapterId) && Strings.isNullOrEmpty(title)) {
				return;
			}
			if (Strings.isNullOrEmpty(songId) && Strings.isNullOrEmpty(resourceId)) {
				return;
			}
			if (Strings.isNullOrEmpty(chapterId)) {
				if (Strings.isNullOrEmpty(parentId)) {
					return;
				}
				chapterId = chapterService.addSubChapter(parentId, title);
			}
			if (Strings.isNullOrEmpty(chapterId)) {
				return;
			}
			templateChapterResourceService.save(chapterId, songId, resourceId, templateId);
			List<Map<String, String>> impresses = (List<Map<String, String>>) relation.getOrDefault("impresses", null);
			if (null == impresses || impresses.isEmpty()) {
				return;
			}
			impresses.forEach(impresse -> {
				String impresseId = impresse.getOrDefault("id", "");
				if (Strings.isNullOrEmpty(impresseId) || Strings.isNullOrEmpty(songId)) {
					return;
				}
				songImpressTemplateService.save(templateId, songId, impresseId);
			});

			// List<String> qids = Lists.newArrayList();
			// List<Map<String, Object>> questions = (List<Map<String, Object>>)
			// relation.getOrDefault("questions", null);
			// if (null != questions && !questions.isEmpty()) {
			// String ids = saveQuestion(gson.toJson(questions));
			// if (!Strings.isNullOrEmpty(ids)) {
			// qids.addAll(Splitter.on(",").splitToList(ids));
			// }
			// }
			// List<Map<String, Object>> questiongroups = (List<Map<String,
			// Object>>) relation
			// .getOrDefault("questiongroups", null);
			// if (null != questiongroups && !questiongroups.isEmpty()) {
			// String ids = saveQuestion(gson.toJson(questiongroups));
			// if (!Strings.isNullOrEmpty(ids)) {
			// qids.addAll(Splitter.on(",").splitToList(ids));
			// }
			// }
			// for (String qid : qids) {
			// saveChapterTemplateQuestionRelation(chapterId, templateId, qid);
			// }

			deleteChapterTemplateQuestionRelation(chapterId, templateId);
			List<String> qids = (List<String>) relation.getOrDefault("questions", null);
			System.out.println("----qids:" + qids);
			if (null == qids) {
				return;
			}
			for (String qid : qids) {
				saveChapterTemplateQuestionRelation(chapterId, templateId, qid);
			}
		});
	}

	// @SuppressWarnings({ "rawtypes", "unchecked" })
	// private String saveQuestion(String json) {
	// HttpHeaders headers = new HttpHeaders();
	// headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
	//
	// HttpEntity<String> requestEntity = new HttpEntity<String>(json, headers);
	// ResponseEntity<Map> responseEntity =
	// restTemplate.exchange(ApiClient.SAVE_QUESTIONS2, HttpMethod.POST,
	// requestEntity, Map.class);
	//
	// Map<String, Object> ret = responseEntity.getBody();
	// String ids = (String) ret.getOrDefault("ids", "");
	// return ids;
	// }

	private void deleteChapterTemplateQuestionRelation(String chapterId, String templateId) {
		List<TemplateChapterQuestionRelation> tcqrs = templateChapterQuestionRelationDao
				.getByTemplateChapter(templateId, chapterId);
		if (null == tcqrs || tcqrs.isEmpty()) {
			return;
		}
		tcqrs.forEach(tcqr -> {
			templateChapterQuestionRelationDao.delete(tcqr);
			System.out.println("--------------tcqr id:" + tcqr.getId());
		});
	}

	@Override
	public boolean deleteTemplate(String tid) {
		templateChapterQuestionRelationDao.deleteByTemplate(tid);
		templateChapterResourceService.deleteByTemplate(tid);
		songImpressTemplateService.deleteBytemplate(tid);
		templateDao.deleteById(tid);
		return true;
	}

	@Override
	public boolean updateTemplate(String tid, String title, String json) {
		Template template = templateDao.findByID(tid);
		if (null == template) {
			return false;
		}
		template.setTitle(title);
		templateDao.update(template);
		if (Strings.isNullOrEmpty(json)) {
			return true;
		}
		templateChapterResourceService.deleteByTemplate(tid);
		songImpressTemplateService.deleteBytemplate(tid);
		readJson(json, template.getId());
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean addPersonalTemplate(String teacherId, String bookId, String classroomId, String title, String json) {
		Template template = new Template();
		template.setTeacherId(teacherId);
		template.setTitle(title);
		template.setCreateAt(Instant.now().getEpochSecond());
		template.setIsDefault(false);
		templateDao.save(template);
		if (Strings.isNullOrEmpty(template.getId())) {
			return false;
		}
		if (Strings.isNullOrEmpty(json)) {
			return !Strings.isNullOrEmpty(template.getId());
		}
		Map<String, Object> map = gson.fromJson(json, Map.class);
		List<Map<String, Object>> relations = (List<Map<String, Object>>) map.getOrDefault("relations", null);
		if (null != relations && !relations.isEmpty()) {
			String chapterId = (String) relations.get(0).getOrDefault("parent_chapter_id", "");
			TemplateParentChapterBookClassRoom tpcbc = new TemplateParentChapterBookClassRoom();
			tpcbc.setBookId(bookId);
			tpcbc.setChapterId(chapterId);
			tpcbc.setClassRoomId(classroomId);
			tpcbc.setTemplateId(template.getId());
			templateParentChapterBookClassRoomService.save(tpcbc);
		}
		readJson(json, template.getId());
		return !Strings.isNullOrEmpty(template.getId());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getClassRoomTemplate(String classroomId, String chapterId) {
		List<TemplateParentChapterBookClassRoom> tpcbcrs = templateParentChapterBookClassRoomService
				.getClassRoomTemplate(classroomId, chapterId);
		if (null == tpcbcrs || tpcbcrs.isEmpty()) {
			ClassRoom classRoom = classRoomService.get(classroomId);
			if (null == classRoom) {
				return null;
			}
			String bookId = classRoom.getBookId();
			TemplateParentChapterBookClassRoom tpcbcr = templateParentChapterBookClassRoomService
					.getBookTemplate(bookId, chapterId);
			if (null == tpcbcr) {
				return null;
			}
			setClassRoomTemplate(tpcbcr.getTemplateId(), classroomId, chapterId);
			return Lists.newArrayList(parse(tpcbcr.getTemplate(), classroomId));
		}
		TemplateParentChapterBookClassRoom tpcbcr = tpcbcrs.stream().filter(t -> t.getTemplate().getIsDefault())
				.findAny().get();
		return Lists.newArrayList(parse(tpcbcr.getTemplate(), classroomId));
	}

	@Override
	public boolean setClassRoomTemplate(String templateId, String classroomId, String chapterId) {
		if (Strings.isNullOrEmpty(templateId)) {
			return false;
		}
		if (Strings.isNullOrEmpty(classroomId)) {
			return false;
		}
		if (Strings.isNullOrEmpty(chapterId)) {
			return false;
		}
		boolean success = templateParentChapterBookClassRoomService.saveClassRoomTemplate(classroomId, chapterId,
				templateId);
		return success;
	}

	@Override
	public void saveChapterTemplateQuestionRelation(String chapterId, String templateId, String questionId) {
		TemplateChapterQuestionRelation ctqr = new TemplateChapterQuestionRelation();
		ctqr.setChapterId(chapterId);
		ctqr.setTemplateId(templateId);
		ctqr.setQuestionId(questionId);
		templateChapterQuestionRelationDao.save(ctqr);
	}

	@Override
	public void updateResource(String oldRid, String newRid) {
		templateChapterResourceService.updateResource(oldRid, newRid);
	}

	@Override
	public Template findById(String tid) {
		return templateDao.findByID(tid);
	}

	@Override
	public void update(Template template) {
		templateDao.update(template);
	}

	@Override
	public void setDefaultTemplate(Template template, List<Template> defaultTemplates) {
		// 将已经是默认的模板设置成非默认
		defaultTemplates.forEach(dft -> {
			dft.setIsDefault(false);
			templateDao.update(dft);
		});
		// 将要设置成默认的模板设置成默认
		templateDao.update(template);
	}

}
