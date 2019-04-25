package com.nercl.music.controller;

import com.google.common.io.Files;
import com.nercl.music.constant.CList;
import com.nercl.music.entity.authorize.Topic;
import com.nercl.music.entity.question.MFile;
import com.nercl.music.entity.user.Exerciser;
import com.nercl.music.entity.user.RequiredPrivilege;
import com.nercl.music.entity.user.Role;
import com.nercl.music.service.ExerciserAnswerRecordService;
import com.nercl.music.service.MFileService;
import com.nercl.music.service.TopicService;
import com.nercl.music.util.PropertiesUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
public class AnswerRecordController {

	@Autowired
	private TopicService topicService;

	@Autowired
	private ExerciserAnswerRecordService exerciserAnswerRecordService;

	@Autowired
	private MFileService mfileService;

	@Value("${music_exercise.satff.img.path}")
	private String satffImgPath;

	@Autowired
	private PropertiesUtil propertiesUtil;

	@Value("${music_exercise.answer.looksing}")
	private String lookSingPath;

	@GetMapping(value = "/answer_records")
	@RequiredPrivilege(Role.MANAGER)
	public String list(@RequestParam(value = "name", required = false) String name,
	        @RequestParam(value = "topic_id", required = false) String topicId,
	        @RequestParam(value = "year", required = false) String year,
	        @RequestParam(value = "question_type", required = false) Integer questionType,
	        @RequestParam(value = "page", required = false, defaultValue = "1") int page, Model model) {
		int currentYear = LocalDate.now().getYear();
		Integer selectYear = year == null ? currentYear : Integer.valueOf(year);
		List<Topic> topics = this.topicService.getByYear(selectYear);
		Topic topic;
		if (StringUtils.isBlank(topicId)) {
			topic = this.topicService.getNewest(selectYear);
		} else {
			topic = this.topicService.findByID(topicId);
		}
		model.addAttribute("currentYear", currentYear);
		model.addAttribute("year", selectYear);
		if (null != topic) {
			topicId = topic.getId();
			questionType = (null == questionType || questionType == 0) ? CList.Api.QuestionType.LOOK_SING
			        : questionType;
			List<Map<String, Object>> records = this.exerciserAnswerRecordService.list(null, name, topicId,
			        questionType, page);
			model.addAttribute("topicId", topic.getId());
			model.addAttribute("topics", topics);
			model.addAttribute("records", records);
			if (StringUtils.isNotBlank("name")) {
				model.addAttribute("name", name);
			}
			model.addAttribute("questionType", questionType);
		}
		return "answer/answers";
	}

	@GetMapping(value = "/answer_records/exerciser")
	@RequiredPrivilege(Role.EXERCISER)
	public String listByExerciser(@RequestParam(value = "topic_id", required = false) String topicId,
	        @RequestParam(value = "year", required = false) String year,
	        @RequestParam(value = "question_type", required = false) Integer questionType,
	        @RequestParam(value = "page", required = false, defaultValue = "1") int page, HttpSession session,
	        Model model) {
		Exerciser exerciser = (Exerciser) session.getAttribute("exerciser");
		if (null == exerciser) {
			return "redirect:/session";
		}
		int currentYear = LocalDate.now().getYear();
		Integer selectYear = year == null ? currentYear : Integer.valueOf(year);
		List<Topic> topics = this.topicService.getByYear(selectYear);
		Topic topic;
		if (StringUtils.isBlank(topicId)) {
			topic = this.topicService.getNewest(selectYear);
		} else {
			topic = this.topicService.findByID(topicId);
		}
		model.addAttribute("currentYear", currentYear);
		model.addAttribute("year", selectYear);
		if (null != topic) {
			topicId = topic.getId();
			questionType = (null == questionType || questionType == 0) ? CList.Api.QuestionType.LOOK_SING
			        : questionType;
			List<Map<String, Object>> records = this.exerciserAnswerRecordService.list(exerciser.getPersonId(), "",
			        topicId, questionType, page);
			model.addAttribute("topicId", topic.getId());
			model.addAttribute("topics", topics);
			model.addAttribute("records", records);
			model.addAttribute("questionType", questionType);
		}
		return "answer/answers";
	}

	@GetMapping(value = "/looksing_data", params = { "record_id" })
	@ResponseBody
	public String getLookSingData(@RequestParam(value = "record_id") String recordId) {
		return this.exerciserAnswerRecordService.getLookSingData(recordId);
	}

	@GetMapping(value = "/curve_data", params = { "record_id" })
	@ResponseBody
	public String getCurveData(@RequestParam(value = "record_id") String recordId) {
		return this.exerciserAnswerRecordService.getCurveData(recordId);
	}

	@GetMapping(value = "/looksing/{audio}")
	public void getLooksingAudio(@PathVariable String audio, HttpServletResponse response) {
		MFile mfile = this.mfileService.get(audio);
		if (null == mfile) {
			return;
		}
		String ext = Files.getFileExtension(mfile.getName());
		String contentType = propertiesUtil.get(ext);
		response.setContentType(contentType + ";charset=UTF-8");
		response.setHeader("Content-disposition", "filename=" + mfile.getName());
		File file = new File(lookSingPath + File.separator + mfile.getName());
		if (file.exists()) {
			response.setHeader("Content-Length", String.valueOf(file.length()));
		}
		OutputStream to = null;
		try {
			to = response.getOutputStream();
			FileUtils.copyFile(file, to);
			to.flush();
			response.flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (to != null) {
				try {
					to.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
