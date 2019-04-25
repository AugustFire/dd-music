package com.nercl.music.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

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

import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.nercl.music.constant.CList;
import com.nercl.music.entity.Exam;
import com.nercl.music.entity.ExamPaper;
import com.nercl.music.entity.MFile;
import com.nercl.music.entity.RequiredPrivilege;
import com.nercl.music.entity.user.Role;
import com.nercl.music.service.AnswerRecordService;
import com.nercl.music.service.ExamPaperService;
import com.nercl.music.service.ExamService;
import com.nercl.music.service.MFileService;
import com.nercl.music.util.PropertiesUtil;
import com.nercl.music.util.StaffUtil;

@Controller
public class AnswerRecordController {

	@Autowired
	private AnswerRecordService answerRecordService;

	@Autowired
	private ExamService examService;

	@Autowired
	private ExamPaperService examPaperService;

	@Autowired
	private StaffUtil staffUtil;

	@Value("${exam_music.satff.img.path}")
	private String satffImgPath;

	@Value("${exam_music.answer.examinee.looksing}")
	private String looksing;

	@Autowired
	private MFileService mfileService;

	@Autowired
	private PropertiesUtil propertiesUtil;

	@GetMapping(value = "/answer_records")
	@RequiredPrivilege(Role.MANAGER)
	public String list(@RequestParam(value = "name", required = false) String name,
	        @RequestParam(value = "exam_no", required = false) String examNo,
	        @RequestParam(value = "exam_id", required = false) String examId,
	        @RequestParam(value = "exam_paper_id", required = false) String examPaperId,
	        @RequestParam(value = "question_type", required = false) Integer questionType,
	        @RequestParam(value = "page", required = false, defaultValue = "1") int page, Model model) {
		Exam exam = null;
		if (StringUtils.isBlank(examId)) {
			exam = this.examService.getUsedToExam(LocalDate.now().getYear());
		} else {
			exam = this.examService.get(examId);
		}
		model.addAttribute("currentYear", LocalDate.now().getYear());
		if (null != exam) {
			List<ExamPaper> examPapers = this.examPaperService.getByExam(exam.getId());
			if (null != examPapers && !examPapers.isEmpty()) {
				questionType = (null == questionType || questionType == 0) ? CList.Api.QuestionType.SHORT_ANSWER
				        : questionType;
				examPaperId = (StringUtils.isBlank(examPaperId) ? examPapers.get(0).getId() : examPaperId);
				List<Map<String, Object>> records = this.answerRecordService.list(name, examNo, examId, examPaperId,
				        questionType, page);
				model.addAttribute("examId", exam.getId());
				model.addAttribute("exams", Lists.newArrayList(exam));
				model.addAttribute("examPapers", examPapers);
				model.addAttribute("examPaperId", examPaperId);
				model.addAttribute("records", records);
				if (StringUtils.isNotBlank("name")) {
					model.addAttribute("name", name);
				}
				if (StringUtils.isNotBlank("examNo")) {
					model.addAttribute("examNo", examNo);
				}
				model.addAttribute("questionType", questionType);
			}
		}
		return "answer/answers";
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
			int result = this.staffUtil.getStaffPic(xmlPath, imgFilePath);
			if (result > 0) {
				this.setStaffPicToResponse(resp, imgFile);
			}
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
			// if (imgFile.exists()) {
			// imgFile.delete();
			// }
		}
	}

	@GetMapping(value = "/looksing_parser", params = { "exam_id", "exam_paper_id", "examinee_id", "question_id" })
	@ResponseBody
	public void getLookSingParser(@RequestParam(value = "exam_id") String examId,
	        @RequestParam(value = "exam_paper_id") String examPaperId,
	        @RequestParam(value = "examinee_id") String examineeId,
	        @RequestParam(value = "question_id") String questionId, HttpServletResponse resp) {
		this.answerRecordService.getLookSingParser(examId, examPaperId, examineeId, questionId);
	}

	/**
	 * getDataForLookSing
	 * 
	 * @param examId
	 * @param examPaperId
	 * @param examineeId
	 * @param questionId
	 * @param resp
	 */
	@GetMapping(value = "/looksing_data", params = { "exam_id", "exam_paper_id", "examinee_id", "question_id" })
	@ResponseBody
	public String getLookSingData(@RequestParam(value = "exam_id") String examId,
	        @RequestParam(value = "exam_paper_id") String examPaperId,
	        @RequestParam(value = "examinee_id") String examineeId,
	        @RequestParam(value = "question_id") String questionId, HttpServletResponse resp) {
		String data = this.answerRecordService.getLookSingData(examId, examPaperId, examineeId, questionId);
		return data;
	}

	/**
	 * getDataForCurve
	 * 
	 * @param examId
	 * @param examPaperId
	 * @param examineeId
	 * @param questionId
	 * @param resp
	 */
	@GetMapping(value = "/curve_data", params = { "exam_id", "exam_paper_id", "examinee_id", "question_id" })
	@ResponseBody
	public String getCurveData(@RequestParam(value = "exam_id") String examId,
	        @RequestParam(value = "exam_paper_id") String examPaperId,
	        @RequestParam(value = "examinee_id") String examineeId,
	        @RequestParam(value = "question_id") String questionId, HttpServletResponse resp) {
		String data = this.answerRecordService.getCurveData(examId, examPaperId, examineeId, questionId);
		return data;
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
		File file = new File(looksing + File.separator + mfile.getName());
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
