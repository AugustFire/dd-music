package com.nercl.music.util;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

@Component
public class PropertiesUtil {

	private static final String PROPETTY_FILE = "mime.properties";

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
		if (Strings.isNullOrEmpty(key)) {
			return "application/octet-stream";
		}
		String value = properties.getProperty(key);
		if (Strings.isNullOrEmpty(value)) {
			value = properties.getProperty(key.toLowerCase());
		}
		if (Strings.isNullOrEmpty(value)) {
			return "application/octet-stream";
		}
		return value;
	}

}
