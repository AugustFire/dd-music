package com.nercl.music.util.xml;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nercl.music.constant.CList.Api.QuestionType;
import com.nercl.music.constant.CList.Api.SubjectType;
import com.nercl.music.util.CloudFileUtil;

@Component
public class XmlParser {

	@Autowired
	private CloudFileUtil cloudFileUtil;

	private SAXBuilder builder;

	@PostConstruct
	public void init() throws Exception {
		builder = new SAXBuilder();
		builder.setEntityResolver(new IgnoreDTDEntityResolver());
	}

	@PreDestroy
	public void destroy() throws Exception {
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> parseQuestion(File xmlFile) {
		List<Map<String, Object>> jsons = Lists.newArrayList();
		Document document = null;
		System.out.println("-------xmlFile:"+xmlFile.getPath());
		try {
			document = builder.build(xmlFile);
		} catch (JDOMException | IOException e) {
			e.printStackTrace();
		}
		Element root = document.getRootElement();
		List<Element> questions = root.getChildren("question");
		if (null == questions) {
			return jsons;
		}
		questions.forEach(question -> {
			Map<String, Object> map = Maps.newHashMap();
			jsons.add(map);
			map.put("questionType", QuestionType.SINGLE_SELECT);
			map.put("subjectType", SubjectType.YUE_LI);

			Element title = question.getChild("title");
			Optional.ofNullable(title).map(it -> it.getText()).ifPresent((text) -> {
				map.put("title", text);
			});

			Element pic = question.getChild("pic");
			Optional.ofNullable(pic).map(it -> it.getText()).ifPresent((val) -> {
				File file = new File(xmlFile.getParent() + File.separator + val);
				Optional.ofNullable(file).ifPresent((f) -> {
					if (file.exists()) {
						String json = "{'resourceType':'题目资源','fileName':'" + file.getName() + "'}";
						String rid = cloudFileUtil.upload(f, json);
						System.out.println("-------titleImage------rid:" + rid);
						map.put("titleImage", rid);
					}
				});
			});

			Element audio = question.getChild("audio");
			Optional.ofNullable(audio).map(it -> it.getText()).ifPresent((val) -> {
				File file = new File(xmlFile.getParent() + File.separator + val);
				Optional.ofNullable(file).ifPresent((f) -> {
					if (file.exists()) {
						String json = "{'resourceType':'题目资源','fileName':'" + file.getName() + "'}";
						String rid = cloudFileUtil.upload(f, json);
						System.out.println("-------titleAudio------rid:" + rid);
						map.put("titleAudio", rid);
					}
				});
			});

			Element choices = question.getChild("choices");
			Optional.ofNullable(choices).ifPresent((it) -> {
				List<Element> options = choices.getChildren("option");
				Optional.ofNullable(options).ifPresent((ops) -> {
					List<Map<String, Object>> list = Lists.newArrayList();
					map.put("listOption", list);
					ops.forEach(op -> {
						Map<String, Object> m = Maps.newHashMap();
						list.add(m);
						m.put("no", Optional.ofNullable(op.getAttributeValue("no")).orElse(""));
						m.put("content", op.getText());
						m.put("isTrue", Optional.ofNullable(op.getAttributeValue("istrue")).orElse("false"));
						Optional.ofNullable(op.getAttributeValue("img")).ifPresent(img -> {
							System.out.println("-------------img:" + img);
							File file = new File(xmlFile.getParent() + File.separator + img);
							System.out.println("-------------file:" + img);
							if (file.exists()) {
								System.out.println("bbbbbbbbbbbbb:");
								String json = "{'resourceType':'题目资源','fileName':'" + img + "'}";
								String rid = cloudFileUtil.upload(file, json);
								System.out.println("-------optionImage------rid:" + rid);
								m.put("optionImage", rid);
							}
						});

					});
				});
			});
		});
		return jsons;
	}

}
