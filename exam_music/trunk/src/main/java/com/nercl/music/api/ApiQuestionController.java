package com.nercl.music.api;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.nercl.music.constant.CList;
import com.nercl.music.service.KnowledgeMapService;
import com.nercl.music.service.MFileService;
import com.nercl.music.util.ExplainPropertiesUtil;
import com.nercl.music.xml.XmlParser;

@RestController
public class ApiQuestionController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	private static final String XMl_EXT = "xml";

	private static final String NMl_EXT = "nml";

	@Autowired
	private MFileService mfileService;

	@Autowired
	private XmlParser xmlParser;

	@Autowired
	private ExplainPropertiesUtil explainPropertiesUtil;

	@Autowired
	private KnowledgeMapService knowledgeMapService;

	@Value("${exam_music.question.xml}")
	private String xmlPath;

	@PostMapping(value = "/api/exam_question", produces = JSON_PRODUCES)
	public Map<String, Object> saveQuestion(HttpServletRequest request) {
		Map<String, Object> ret = Maps.newHashMap();
		InputStream in = null;
		String uuid = UUID.randomUUID().toString();
		try {
			if (request instanceof MultipartHttpServletRequest) {
				MultipartHttpServletRequest mreq = (MultipartHttpServletRequest) request;
				in = mreq.getInputStream();
				MultipartFile file = mreq.getFile("file");
				if (file != null) {
					in = file.getInputStream();
					String filename = file.getOriginalFilename();
					if (StringUtils.isBlank(filename)) {
						ret.put("code", CList.Api.Client.PROCESSING_FAILED);
						ret.put("desc", "文件名称为空");
						return ret;
					}
					String ext = Files.getFileExtension(filename);
					if (!XMl_EXT.equalsIgnoreCase(ext) && !NMl_EXT.equalsIgnoreCase(ext)) {
						ret.put("code", CList.Api.Client.PROCESSING_FAILED);
						ret.put("desc", "非xml文件");
						return ret;
					}
					String name = uuid + "." + ext;
					File questionXmlFile = new File(xmlPath + File.separator + name);
					Files.createParentDirs(questionXmlFile);
					FileUtils.copyInputStreamToFile(in, questionXmlFile);
					boolean success = this.mfileService.save(filename, name, uuid, ext, questionXmlFile.getPath(),
					        CList.Api.FileResType.QUESTION_XML);
					if (success) {
						xmlParser.parseQuestion(questionXmlFile);
						ret.put("code", CList.Api.Client.OK);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "上传题目出错");
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
				}
			}
		}
		return ret;
	}

	@GetMapping(value = "/api/yueli/knowledge_map", produces = JSON_PRODUCES)
	public Map<String, Object> getKnowledgeMap() {
		Map<String, Object> ret = Maps.newHashMap();
		ret.put("code", CList.Api.Client.OK);
		ret.put("map", this.knowledgeMapService.getAll());
		return ret;
	}

	@GetMapping(value = "/api/question/explain", produces = JSON_PRODUCES)
	public Map<String, Object> getQuestionExplain(Integer subject_type) {
		Map<String, Object> ret = Maps.newHashMap();
		if (null == subject_type || subject_type < 1 || subject_type > 3) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "科目类型为空或不合法");
			return ret;
		}
		List<Map<String, Object>> explains = Lists.newArrayList();
		Map<String, Object> map = Maps.newHashMap();
		explains.add(map);
		map.put("explain", this.explainPropertiesUtil.get("subject.type." + subject_type));
		if (!explains.isEmpty()) {
			ret.put("code", CList.Api.Client.OK);
			ret.put("subject_type", subject_type);
			ret.put("explains", explains);
			if (subject_type == 2) {
				ret.put("shi_yin", this.explainPropertiesUtil.get("look.sing.shi.yin"));
			}
			if (subject_type == 3) {
				ret.put("shi_yin", this.explainPropertiesUtil.get("ting.yin.shi.yin"));
			}
		} else {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "未找到题目说明");
		}
		return ret;
	}

}
