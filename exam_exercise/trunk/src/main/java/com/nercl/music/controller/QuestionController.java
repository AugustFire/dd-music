package com.nercl.music.controller;

import com.google.common.base.Joiner;
import com.google.common.io.Files;
import com.nercl.music.entity.question.ExamQuestion;
import com.nercl.music.entity.question.MFile;
import com.nercl.music.entity.user.RequiredPrivilege;
import com.nercl.music.entity.user.Role;
import com.nercl.music.service.ExamQuestionService;
import com.nercl.music.service.MFileService;
import com.nercl.music.service.TopicService;
import com.nercl.music.util.StaffUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Controller
public class QuestionController {

	@Autowired
	private ExamQuestionService questionService;

	@Autowired
	private MFileService mfileService;

	@Autowired
	private StaffUtil staffUtil;

	@Autowired
	private TopicService topicService;

	@Value("${music_exercise.satff.img.path}")
	private String satffImgPath;

	@Value("${music_exercise.question.img}")
	private String imgPath;

	@GetMapping(value = "/questions")
	public String getQuestions(Integer subjectType, String tid,
	        @RequestParam(value = "page", required = false, defaultValue = "1") int page, Model model) {
		List<ExamQuestion> questions = questionService.getQuestionsBySubjectType(subjectType, page);
		List<String> ids = topicService.getQuestions(tid);
		model.addAttribute("questions", questions);
		model.addAttribute("ids", ids);
		model.addAttribute("idss", Joiner.on(",").join(ids.iterator()));
		return "question/questions";
	}

	@GetMapping(value = "/img", params = { "path" })
	public void getOptionImg(@RequestParam(value = "path") String path, HttpServletResponse resp) {
		MFile mfile = this.mfileService.get(path);
		if (null != mfile) {
			String imgPath = mfile.getPath();
			File imgFile = new File(imgPath);
			this.setStaffPicToResponse(resp, imgFile);
			return;
		}
	}

	@GetMapping(value = "/staff_img", params = { "xml_path" })
	public void getStaffImg(@RequestParam(value = "xml_path") String xmlPath, HttpServletResponse resp) {
		String imgFilePath = this.satffImgPath + File.separator + xmlPath + ".png";
		File imgFile = new File(imgFilePath);
		if (imgFile.exists()) {
			this.setStaffPicToResponse(resp, imgFile);
			return;
		}
		try {
			Files.createParentDirs(imgFile);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		MFile mfile = this.mfileService.get(xmlPath);
		if (null != mfile) {
			xmlPath = mfile.getPath();
		}
		int result = this.staffUtil.getStaffPic(xmlPath, imgFilePath);
		if (result > 0) {
			this.setStaffPicToResponse(resp, imgFile);
		}
	}

	private void setStaffPicToResponse(HttpServletResponse resp, File imgFile) {
		resp.setHeader("Pragma", "no-cache");
		resp.setHeader("Cache-Control", "no-cache");
		resp.setDateHeader("Expires", 0);
		resp.setContentType("image/png");
		ServletOutputStream sos = null;
		FileInputStream is = null;
		BufferedImage image = null;
		try {
			sos = resp.getOutputStream();
			is = new FileInputStream(imgFile);
			image = ImageIO.read(is);
			ImageIO.write(image, "png", sos);
			image.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != sos) {
				try {
					sos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 跳转到题库管理页面
	 *
	 * @param type
	 *            题型
	 * @param title
	 *            题干
	 * @param difficulty
	 *            难度系数
	 * @param page
	 *            翻页
	 */
	@GetMapping(value = "/questions/manage")
	@RequiredPrivilege(Role.MANAGER)
	public String list(@RequestParam(value = "type", required = false) Integer type,
	        @RequestParam(value = "title", required = false) String title,
	        @RequestParam(value = "difficulty", required = false) Float difficulty,
	        @RequestParam(value = "page", required = false, defaultValue = "1") int page, Model model)
	        throws Exception {
		List<ExamQuestion> questions = this.questionService.list(type, title, difficulty, page);
		model.addAttribute("type", type);
		// model.addAttribute("difficulty", difficulty);
		model.addAttribute("questions", questions);
		return "question/questions_manage";
	}

	/**
	 * 删除题目
	 *
	 * @param ids
	 *            题目ID集合
	 */
	@GetMapping(value = "/exam_question/delete/{ids}")
	@ResponseBody
	@RequiredPrivilege(Role.MANAGER)
	public boolean pass(@PathVariable String[] ids) {
		return this.questionService.delete(ids);
	}

}
