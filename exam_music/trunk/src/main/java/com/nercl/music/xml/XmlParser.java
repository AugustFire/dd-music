package com.nercl.music.xml;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang3.StringUtils;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.nercl.music.constant.CList;
import com.nercl.music.entity.Answer;
import com.nercl.music.entity.CheckRecord;
import com.nercl.music.entity.ExamQuestion;
import com.nercl.music.entity.Option;
import com.nercl.music.service.AnswerService;
import com.nercl.music.service.ExamQuestionService;
import com.nercl.music.service.OptionService;
import com.nercl.music.util.DESCryption;
import com.nercl.music.util.StaffUtil;

@Component
public class XmlParser {

	private SAXBuilder builder;

	@Autowired
	private ExamQuestionService examQuestionService;

	@Autowired
	private OptionService optionService;

	@Autowired
	private AnswerService answerService;

	@Value("${exam_music.domain}")
	private String domain;

	@Autowired
	private DESCryption dESCryption;

	@Autowired
	private StaffUtil staffUtil;

	@PostConstruct
	public void init() throws Exception {
		builder = new SAXBuilder();
		builder.setEntityResolver(new IgnoreDTDEntityResolver());
	}

	@PreDestroy
	public void destroy() throws Exception {
	}

	@SuppressWarnings("unchecked")
	public void parseQuestion(File xmlFile) {

		Document document;
		try {
			document = builder.build(xmlFile);
			Element root = document.getRootElement();

			Element questionElement = root.getChild("question");
			if (null == questionElement) {
				return;
			}

			ExamQuestion examQuestion = new ExamQuestion();
			examQuestion.setXmlPath(this.domain + "/file/" + xmlFile.getName().split("\\.")[0]);
			List<Option> options = Lists.newArrayList();

			Attribute guidAttribute = questionElement.getAttribute("guid");
			if (null == guidAttribute) {
				return;
			}
			String guid = guidAttribute.getValue();
			if (StringUtils.isBlank(guid)) {
				return;
			}
			examQuestion.setId(guid);

			Element titleElement = questionElement.getChild("title");
			if (null == titleElement) {
				return;
			}
			String title = titleElement.getText();
			if (StringUtils.isBlank(title)) {
				return;
			}
			examQuestion.setTitle(title);

			Element titlememoElement = questionElement.getChild("titlememo");
			if (null != titlememoElement) {
				examQuestion.setExplaine(titlememoElement.getText());
			}

			String imgPath = "";
			List<Element> imgElements = questionElement.getChildren("img");
			for (Element imgElement : imgElements) {
				imgPath = imgPath + imgElement.getText();
			}
			examQuestion.setTitleImage(imgPath);

			String audioPath = "";
			List<Element> audioElements = questionElement.getChildren("audio");
			for (Element audioElement : audioElements) {
				audioPath = audioPath + audioElement.getText();
			}
			examQuestion.setTitleAudio(audioPath);

			Element subjectElement = questionElement.getChild("subject");
			if (null != subjectElement) {
				String subject = subjectElement.getText();
				if (StringUtils.isNotBlank(subject)) {
					examQuestion.setSubjectType(Integer.valueOf(subject));
				}
			}

			Integer questionType = null;
			Element typeElement = questionElement.getChild("type");
			if (null != typeElement) {
				String type = typeElement.getText();
				if (StringUtils.isNotBlank(type)) {
					questionType = Integer.valueOf(type);
					examQuestion.setQuestionType(questionType);
				}
			}

			Element ispublicElement = questionElement.getChild("ispublic");
			if (null != ispublicElement) {
				examQuestion.setIsOpen("1".equals(ispublicElement.getText()));
			}

			Element examfieldElement = questionElement.getChild("examfield");
			if (null != examfieldElement && StringUtils.isNotBlank(examfieldElement.getText())) {
				examQuestion.setExamField(Integer.valueOf(examfieldElement.getText()));
			}

			Element keysigElement = questionElement.getChild("keysig");
			if (null != keysigElement && StringUtils.isNotBlank(keysigElement.getText())) {
				examQuestion.setTune(Integer.valueOf(keysigElement.getText()));
			}

			Element tempoElement = questionElement.getChild("tempo");
			if (null != tempoElement && StringUtils.isNotBlank(tempoElement.getText())) {
				examQuestion.setTempo(Integer.valueOf(tempoElement.getText()));
			}

			Element preparetimeElement = questionElement.getChild("preparetime");
			if (null != preparetimeElement && StringUtils.isNotBlank(preparetimeElement.getText())) {
				examQuestion.setPrepareTime(Integer.valueOf(preparetimeElement.getText()));
			}

			Element limittimeElement = questionElement.getChild("limittime");
			if (null != limittimeElement && StringUtils.isNotBlank(limittimeElement.getText())) {
				examQuestion.setLimitTime(Integer.valueOf(limittimeElement.getText()));
			}

			Element difficultyElement = questionElement.getChild("difficulty");
			if (null != difficultyElement && StringUtils.isNotBlank(difficultyElement.getText())) {
				examQuestion.setDifficulty(Float.valueOf(difficultyElement.getText()));
			}

			Element reliabilityElement = questionElement.getChild("reliability");
			if (null != reliabilityElement && StringUtils.isNotBlank(reliabilityElement.getText())) {
				examQuestion.setReliability(Float.valueOf(reliabilityElement.getText()));
			}

			Element validityElement = questionElement.getChild("validity");
			if (null != validityElement && StringUtils.isNotBlank(validityElement.getText())) {
				examQuestion.setValidity(Float.valueOf(validityElement.getText()));
			}

			Element discriminationElement = questionElement.getChild("discrimination");
			if (null != discriminationElement && StringUtils.isNotBlank(discriminationElement.getText())) {
				examQuestion.setDiscrimination(Float.valueOf(discriminationElement.getText()));
			}

			Element secretlevelElement = questionElement.getChild("secretlevel");
			if (null != secretlevelElement && StringUtils.isNotBlank(secretlevelElement.getText())) {
				examQuestion.setSecretLevel(Integer.valueOf(secretlevelElement.getText()));
			}

			Element estimatetimeElement = questionElement.getChild("estimatetime");
			if (null != estimatetimeElement && StringUtils.isNotBlank(estimatetimeElement.getText())) {
				examQuestion.setEstimatetime(Integer.valueOf(estimatetimeElement.getText()));
			}

			Element analysisElement = questionElement.getChild("analysis");
			if (null != analysisElement) {
				examQuestion.setAnalysis(analysisElement.getText());
			}

			Element knowledgeElement = questionElement.getChild("knowledge");
			if (null != knowledgeElement) {
				examQuestion.setKnowledge(knowledgeElement.getText());
			}

			Element isplaystandardnoteElement = questionElement.getChild("isplaystandardnote");
			if (null != isplaystandardnoteElement) {
				examQuestion.setIsPlayStandardNote("1".equals(isplaystandardnoteElement.getText()));
			} else {
				examQuestion.setIsPlayStandardNote(Boolean.TRUE);
			}

			Element isplaypreparebeatElement = questionElement.getChild("isplaypreparebeat");
			if (null != isplaypreparebeatElement) {
				examQuestion.setIsPlayRepareBeat("1".equals(isplaypreparebeatElement.getText()));
			} else {
				examQuestion.setIsPlayRepareBeat(Boolean.TRUE);
			}

			Element isplaymainchordElement = questionElement.getChild("isplaymainchord");
			if (null != isplaymainchordElement) {
				examQuestion.setIsPlayMainChord("1".equals(isplaymainchordElement.getText()));
			}

			Element isplaydebugpitchElement = questionElement.getChild("isplaydebugpitch");
			if (null != isplaydebugpitchElement) {
				examQuestion.setIsPlayMainChord("1".equals(isplaydebugpitchElement.getText()));
			}

			Element isplaystartnoteElement = questionElement.getChild("isplaystartnote");
			if (null != isplaystartnoteElement) {
				examQuestion.setIsPlayStartNote("1".equals(isplaystartnoteElement.getText()));
			}

			Element groupnumElement = questionElement.getChild("groupnum");
			if (null != groupnumElement) {
				examQuestion.setGroupnum(Integer.valueOf(groupnumElement.getText()));
			}

			Element playnumElement = questionElement.getChild("playnum");
			if (null != playnumElement) {
				examQuestion.setPlaynum(Integer.valueOf(playnumElement.getText()));
			}

			Element delayElement = questionElement.getChild("delay");
			if (null != delayElement) {
				examQuestion.setDelay(Integer.valueOf(delayElement.getText()));
			}

			Element measurenumElement = questionElement.getChild("measurenum");
			if (null != measurenumElement) {
				examQuestion.setMeasurenum(Integer.valueOf(measurenumElement.getText()));
			}

			Element numeratorElement = questionElement.getChild("numerator");
			if (null != numeratorElement) {
				if (StringUtils.isBlank(numeratorElement.getText())) {
					examQuestion.setNumerator(4);
				} else {
					examQuestion.setNumerator(Integer.valueOf(numeratorElement.getText()));
				}
			} else {
				examQuestion.setNumerator(4);
			}

			Element denominatorElement = questionElement.getChild("denominator");
			if (null != denominatorElement) {
				if (StringUtils.isBlank(denominatorElement.getText())) {
					examQuestion.setDenominator(4);
				} else {
					examQuestion.setDenominator(Integer.valueOf(denominatorElement.getText()));
				}
			} else {
				examQuestion.setDenominator(4);
			}

			Element partElement = root.getChild("part");
			if (null == partElement || null == partElement.getChildren() || partElement.getChildren().isEmpty()) {
				examQuestion.setHasStaff(false);
			}else{
				examQuestion.setHasStaff(true);
			}

			if (CList.Api.QuestionType.SINGLE_SELECT.equals(questionType)
			        || CList.Api.QuestionType.MULTI_SELECT.equals(questionType)) {
				Element choicesElement = questionElement.getChild("choices");
				if (null == choicesElement) {
					return;
				}
				List<Element> optionElements = choicesElement.getChildren("option");
				for (Element optionElement : optionElements) {
					Option option = new Option();
					Attribute noAttribute = optionElement.getAttribute("no");
					if (null != noAttribute) {
						String no = noAttribute.getValue();
						if (StringUtils.isNotBlank(no)) {
							option.setValue(no);
						}
					}

					Attribute typeAttribute = optionElement.getAttribute("type");
					if (null != typeAttribute) {
						String type = typeAttribute.getValue();
						if ("0".equals(type)) {
							String optionImagePath = optionElement.getText();
							option.setOptionImage(optionImagePath);
						} else if ("1".equals(type)) {
							String content = optionElement.getText();
							option.setContent(content);
						} else {
							String xmlPath = optionElement.getText();
							option.setXmlPath(xmlPath);
						}
					}
					options.add(option);
				}
			}
			examQuestion.setCommitedTime(System.currentTimeMillis());
			examQuestion.setCheckStatus(CheckRecord.Status.FOR_CHECKED);
			this.examQuestionService.save(examQuestion);
			options.forEach(option -> {
				option.setExamQuestionId(examQuestion.getId());
				this.optionService.save(option);
			});
			if (CList.Api.QuestionType.LOOK_SING.equals(questionType)) {
				this.staffUtil.createStaffPic(xmlFile);
			}
		} catch (JDOMException | IOException e) {
			e.printStackTrace();
		}
	}

	public void parseAnswer(File xmlFile) {
		Document document = null;
		try {
			document = builder.build(xmlFile);
		} catch (JDOMException | IOException e) {
			e.printStackTrace();
		}
		Element root = document.getRootElement();
		Element answerElement = root.getChild("answer");
		if (null == answerElement) {
			return;
		}
		Attribute guidAttribute = answerElement.getAttribute("guid");
		if (null == guidAttribute) {
			return;
		}
		String guid = guidAttribute.getValue();
		if (StringUtils.isBlank(guid)) {
			return;
		}
		Element answertypeElement = answerElement.getChild("answertype");
		if (null == answertypeElement) {
			return;
		}
		String answertype = answertypeElement.getValue();
		if (CList.Api.AnswerType.OPTION.equals(Integer.valueOf(answertype))) {
			Element answercontentElement = answerElement.getChild("answercontent");
			if (null == answercontentElement) {
				return;
			}
			String answercontent = answercontentElement.getValue();
			String[] answercontents = this.dESCryption.decode(Strings.nullToEmpty(answercontent)).split(",");
			for (String ac : answercontents) {
				Option option = this.optionService.get(guid, ac);
				if (null != option) {
					option.setTrue(true);
					this.optionService.update(option);
				}
			}
		} else {
			boolean isStaff = true;
			Answer answer = this.answerService.getByQuestion(guid);
			if (null != answer) {
				return;
			}
			answer = new Answer();
			answer.setExamQuestionId(guid);
			answer.setXmlPath(this.domain + "/file/" + xmlFile.getName().split("\\.")[0]);
			Element answercontentElement = answerElement.getChild("answercontent");
			if (null != answercontentElement) {
				String answercontent = answercontentElement.getValue();
				if (StringUtils.isNotBlank(answercontent)) {
					answer.setContent(answercontent);
					isStaff = false;
				}
			}
			this.answerService.saveAnswer(answer);
			if (isStaff) {
				this.staffUtil.createStaffPic(xmlFile);
			}
		}
	}

}
