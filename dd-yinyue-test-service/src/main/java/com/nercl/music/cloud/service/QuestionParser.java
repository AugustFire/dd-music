package com.nercl.music.cloud.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.nercl.music.cloud.entity.Answer;
import com.nercl.music.cloud.entity.Dimension;
import com.nercl.music.cloud.entity.Group;
import com.nercl.music.cloud.entity.Option;
import com.nercl.music.cloud.entity.PresentType;
import com.nercl.music.cloud.entity.Question;
import com.nercl.music.cloud.entity.QuestionType;
import com.nercl.music.cloud.entity.SubjectType;
import com.nercl.music.cloud.entity.VersionDesc;

@Component
public class QuestionParser {

	private List<String> imgs = Lists.newArrayList("jpg", "jpeg", "bmp", "png", "gif");

	private List<String> audios = Lists.newArrayList("mp3", "wma", "rm", "wav", "midi", "mid", "ape", "flac");

	private List<String> staffs = Lists.newArrayList("staff", "xml");

	@Autowired
	private Gson gson;

	@Autowired
	private QuestionService questionService;

	@Autowired
	private OptionService optionService;

	@Autowired
	private AnswerService answerService;

	@SuppressWarnings({ "unchecked" })
	public List<String> parse(String json) {
		List<String> ids = Lists.newArrayList();
		List<Map<String, Object>> questions = gson.fromJson(json, List.class);

		if (null != questions && !questions.isEmpty()) {
			questions.forEach(question -> {
				Question q = new Question();

				setQuestion(q, question);

				questionService.save(q);
				ids.add(q.getId());

				setKnowledges(q, question);

				if (QuestionType.SINGLE_SELECT == q.getQuestionType()
						|| QuestionType.MULTI_SELECT == q.getQuestionType()) {
					List<Map<String, Object>> choices = (List<Map<String, Object>>) question.getOrDefault("choices",
							null);
					if (null != choices && !choices.isEmpty()) {
						choices.forEach(choice -> {
							String content = (String) choice.getOrDefault("content", "");
							String file = (String) choice.getOrDefault("file_id", "");
							String ext = (String) choice.getOrDefault("ext", "");
							Boolean istrue = (Boolean) choice.getOrDefault("istrue", false);
							Option option = new Option();
							option.setContent(content);
							option.setIsTrue(istrue);
							option.setValue(UUID.randomUUID().toString());
							option.setQuestionId(q.getId());
							setFile(file, ext, option);
							optionService.save(option);
						});
					}
				}

				if (QuestionType.SHORT_ANSWER == q.getQuestionType()) {
					setAnswer(q, question);
				}
			});
		}
		//stop the world
		List<Map<String, Object>> questiongroups = gson.fromJson(json, List.class);
		if (null != questiongroups && !questiongroups.isEmpty()) {
			questiongroups.forEach(group -> {
				String gid = (String) group.getOrDefault("group", "");
				if (Strings.isNullOrEmpty(gid)) {
					return;
				}
				Group gr = new Group();
				gr.setId(gid);
				questionService.saveGroup(gr);
				List<Map<String, Object>> qs = (List<Map<String, Object>>) group.getOrDefault("questions", null);
				if (null != qs && !qs.isEmpty()) {
					qs.forEach(q -> {
						String id = (String) q.getOrDefault("id", "");
						if (Strings.isNullOrEmpty(id)) {
							return;
						}
						Question qu = new Question();
						qu.setId(id);

						setQuestion(qu, q);

						qu.setGroupId(gid);

						questionService.save(qu);
						ids.add(qu.getId());
					});
				}
				List<Map<String, Object>> choices = (List<Map<String, Object>>) group.getOrDefault("choices", null);
				if (null != choices && !choices.isEmpty()) {
					choices.forEach(choice -> {
						String content = (String) choice.getOrDefault("content", "");
						String file2 = (String) choice.getOrDefault("file_id", "");
						String ext2 = (String) choice.getOrDefault("ext", "");
						String qid = (String) choice.getOrDefault("qid", "");
						Option option = new Option();
						option.setContent(content);
						option.setValue(UUID.randomUUID().toString());
						option.setQuestionId(qid);
						setFile(file2, ext2, option);
						option.setGroupId(gid);
						optionService.save(option);
					});
				}
			});
		}
		return ids;
	}

	@SuppressWarnings("unchecked")
	private void setAnswer(Question q, Map<String, Object> question) {
		Map<String, Object> map = (Map<String, Object>) question.getOrDefault("answer", null);
		if (null == map) {
			return;
		}
		Answer answer = new Answer();
		answer.setQuestionId(q.getId());
		String content = (String) map.getOrDefault("content", "");
		answer.setContent(content);
		String resource = (String) map.getOrDefault("resource", "");
		answer.setResource(resource);
		answerService.save(answer);
	}

	/**
	 *
	 * @param q 新的问题
	 * @param map 接受对象
	 */
	private void setQuestion(Question q, Map<String, Object> map) {
		String title = (String) map.getOrDefault("title", "");
		q.setTitle(title);

		String file = (String) map.getOrDefault("file_id", "");
		String ext = (String) map.getOrDefault("ext", "");
		setFile(file, ext, q);

		String subjectType = (String) map.getOrDefault("subject", "");
		if (!Strings.isNullOrEmpty(subjectType)) {
			q.setSubjectType(SubjectType.valueOf(subjectType));
		}

		String type = (String) map.getOrDefault("type", "");
		if (!Strings.isNullOrEmpty(type)) {
			q.setQuestionType(QuestionType.valueOf(type));
		}

		String feature = (String) map.getOrDefault("feature", "");
		if (!Strings.isNullOrEmpty(feature)) {
			q.setDimension(Dimension.valueOf(feature));
		}

		@SuppressWarnings("unchecked")
		List<String> compositeAbilitys = (List<String>) map.getOrDefault("composite_abilitys", null);
		if (null != compositeAbilitys && !compositeAbilitys.isEmpty()) {
			q.setCompositeAbilitys(Joiner.on(",").join(compositeAbilitys));
		}

		String grade = (String) map.getOrDefault("grade", "");
		q.setGrade(grade);

		String version = (String) map.getOrDefault("version", "");
		if (!Strings.isNullOrEmpty(version)) {
			q.setVersion(VersionDesc.valueOf(version));
		}

		String presentType = (String) map.getOrDefault("present_type", "");
		if (!Strings.isNullOrEmpty(presentType)) {
			q.setPresentType(PresentType.valueOf(presentType));
		}

		Float difficulty = ((Number) map.getOrDefault("difficulty", 0)).floatValue();
		q.setDifficulty(difficulty);

		boolean isNumbered = (boolean) map.getOrDefault("is_numbered", false);
		q.setIsNumbered(isNumbered);

		String songId = (String) map.getOrDefault("song_id", "");
		q.setSongId(songId);

		boolean isPlayStandardNote = (boolean) map.getOrDefault("is_play_standard_note", false);
		q.setIsPlayStandardNote(isPlayStandardNote);

		boolean isPlayStartNote = (boolean) map.getOrDefault("is_play_start_note", false);
		q.setIsPlayStartNote(isPlayStartNote);

		boolean isPlayMainChord = (boolean) map.getOrDefault("is_play_main_chord", false);
		q.setIsPlayMainChord(isPlayMainChord);

		boolean isPlayDebugPitch = (boolean) map.getOrDefault("is_play_debug_pitch", false);
		q.setIsPlayDebugPitch(isPlayDebugPitch);

		boolean isPlayRepareBeat = (boolean) map.getOrDefault("is_play_repare_beat", false);
		q.setIsPlayRepareBeat(isPlayRepareBeat);

		String templatePath = (String) map.getOrDefault("template_path", "");
		q.setTemplatePath(templatePath);

		boolean isShowTimeSignature = (boolean) map.getOrDefault("is_show_time_signature", false);
		q.setIsShowTimeSignature(isShowTimeSignature);

	}

	private void setKnowledges(Question q, Map<String, Object> map) {
		@SuppressWarnings("unchecked")
		List<Map<String, String>> knowledges = (List<Map<String, String>>) map.getOrDefault("knowledges", null);
		if (null != knowledges) {
			List<String> ks = knowledges.stream().map(knowledge -> (String) knowledge.getOrDefault("no", ""))
					.collect(Collectors.toList());
			q.setKnowledges(Joiner.on(",").join(ks));
		}
	}

	private void setFile(String file, String ext, Question question) {
		if (Strings.isNullOrEmpty(file) || Strings.isNullOrEmpty(ext)) {
			return;
		}
		if (imgs.contains(ext)) {
			question.setTitleImage(file);
		}
		if (staffs.contains(ext)) {
			question.setXmlPath(file);
		}
		if (audios.contains(ext)) {
			question.setTitleAudio(file);
		}
	}

	private void setFile(String file, String ext, Option option) {
		if (Strings.isNullOrEmpty(file) || Strings.isNullOrEmpty(ext)) {
			return;
		}
		if (imgs.contains(ext)) {
			option.setOptionImage(file);
		}
		if (staffs.contains(ext)) {
			option.setXmlPath(file);
		}
	}

}
