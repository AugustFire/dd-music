package com.nercl.music.xml;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.io.FileUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;
import com.google.common.io.Files;
import com.nercl.music.constant.CList;
import com.nercl.music.entity.question.ExamQuestion;
import com.nercl.music.entity.question.Option;
import com.nercl.music.service.ExamQuestionService;
import com.nercl.music.service.MFileService;
import com.nercl.music.service.OptionService;
import com.nercl.music.util.DESCryption;

@Component
public class XmlParser2 {

	private SAXBuilder builder;

	@Autowired
	private ExamQuestionService examQuestionService;

	@Autowired
	private OptionService optionService;

	@Autowired
	private DESCryption desCryption;

	@Autowired
	private MFileService mfileService;

	@Value("${music_exercise.question.img}")
	private String imgPath;

	@PostConstruct
	public void init() throws Exception {
		builder = new SAXBuilder();
		builder.setEntityResolver(new IgnoreDTDEntityResolver());
	}

	@PreDestroy
	public void destroy() throws Exception {
	}

	public void parseQuestion(File folder) throws Exception {
		File[] files = folder.listFiles();
		if (null == files || files.length == 0) {
			return;
		}
		Arrays.stream(files).filter(file -> {
			return "xml".equals(Files.getFileExtension(file.getName()));
		}).forEach(file -> {
			parselXml(file);
		});
	}

	private void parselXml(File xmlFile) {
		Document document;
		try {
			document = builder.build(xmlFile);
			Element root = document.getRootElement();

			ExamQuestion examQuestion = new ExamQuestion();
			examQuestion.setId(UUID.randomUUID().toString());
			examQuestion.setSubjectType(CList.Api.SubjectType.YUE_LI);
			Element typeElement = root.getChild("题型");
			if (null != typeElement) {
				examQuestion.setQuestionType("0".equals(typeElement.getValue()) ? CList.Api.QuestionType.SINGLE_SELECT
				        : CList.Api.QuestionType.MULTI_SELECT);
			}

			Element titleElement = root.getChild("题目文字");
			if (null != titleElement) {
				Element imgElement = titleElement.getChild("img");
				if (null != imgElement) {
					String imgPath = imgElement.getValue();
					File img = new File(xmlFile.getParentFile().getPath() + imgPath);
					copyImg(img.getPath(), examQuestion);
				}
				String title = titleElement.getValue();
				if (!Strings.isNullOrEmpty(title)) {
					if (title.contains("jpeg")) {
						String[] titles = title.split("jpeg");
						title = titles[titles.length - 1];
					}
					if (title.contains("jpg")) {
						String[] titles = title.split("jpg");
						title = titles[titles.length - 1];
					}
					if (title.contains("png")) {
						String[] titles = title.split("png");
						title = titles[titles.length - 1];
					}
					if(!title.contains("image")){
						examQuestion.setTitle(desCryption.encode(title));
					}
				}
			}

			Element diffcultElement = root.getChild("困难度");
			if (null != diffcultElement) {
				if("(1)".equals(diffcultElement.getValue())){
					examQuestion.setDifficulty(1f);
				}else{
					examQuestion.setDifficulty(Float.valueOf(diffcultElement.getValue()));
				}
			}

			Element analysisElement = root.getChild("详解");
			if (null != analysisElement) {
				examQuestion.setAnalysis(analysisElement.getValue());
			}

			examQuestionService.save(examQuestion);

			Element optionsElement = root.getChild("ChoiceList");
			if (null != optionsElement) {
				@SuppressWarnings("unchecked")
				List<Element> choices = optionsElement.getChildren("ChoiceData");
				if (null != choices) {
					for (int i = 0; i < choices.size(); i++) {
						Option option = new Option();
						Element contentElement = choices.get(i).getChild("选项内容");
						if (null != contentElement) {
							Element imgElement = contentElement.getChild("img");
							if (null != imgElement) {
								String imgPath = imgElement.getValue();
								File img = new File(xmlFile.getParentFile().getPath() + imgPath);
								copyOptionImg(img.getPath(), option);
							} else {
								String content = contentElement.getValue();
								if (!Strings.isNullOrEmpty(content)) {
									option.setContent(desCryption.encode(content));
								}
							}
						}
						Element isTrueElement = choices.get(i).getChild("答案");
						if (null != isTrueElement) {
							String isTrue = isTrueElement.getValue();
							option.setTrue("1".equals(isTrue));
						}
						option.setValue(String.valueOf(i + 1));
						option.setExamQuestionId(examQuestion.getId());
						optionService.save(option);
					}
				}
			}
		} catch (JDOMException | IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void copyImg(String originImg, ExamQuestion examQuestion) {
		String uuid = UUID.randomUUID().toString();
		File originImgFile = new File(originImg);
		if (null == originImgFile || !originImgFile.exists()) {
			return;
		}
		String filename = originImgFile.getName();
		String ext = Files.getFileExtension(filename);
		String name = uuid + "." + ext;
		File f = new File(imgPath + File.separator + name);
		try {
			FileUtils.copyFile(originImgFile, f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		boolean success = this.mfileService.save(filename, name, uuid, ext, f.getPath(), null);
		if (success) {
			examQuestion.setTitleImage(uuid);
		}
	}

	private void copyOptionImg(String originImg, Option option) {
		String uuid = UUID.randomUUID().toString();
		File originImgFile = new File(originImg);
		if (null == originImgFile || !originImgFile.exists()) {
			return;
		}
		String filename = originImgFile.getName();
		String ext = Files.getFileExtension(filename);
		String name = uuid + "." + ext;
		File f = new File(imgPath + File.separator + name);
		try {
			FileUtils.copyFile(originImgFile, f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		boolean success = this.mfileService.save(filename, name, uuid, ext, f.getPath(), null);
		if (success) {
			option.setOptionImage(uuid);
		}
	}

}
