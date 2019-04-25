package com.nercl.music.util;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

@Component
public class ExplainPropertiesUtil {

	private static final String PROPETTY_FILE = "explain.properties";

	private static Properties properties = null;

	@PostConstruct
	public void init() throws Exception {
		try {
			synchronized (this) {
				if (null == properties) {
					properties = PropertiesLoaderUtils.loadAllProperties(PROPETTY_FILE);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@PreDestroy
	public void destroy() throws Exception {
	}

	public String get(String key) {
		return properties.getProperty(key);
	}

}
