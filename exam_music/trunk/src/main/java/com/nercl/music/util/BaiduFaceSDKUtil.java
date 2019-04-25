package com.nercl.music.util;

import java.util.Arrays;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.baidu.aip.face.AipFace;

@Component
public class BaiduFaceSDKUtil {

	@Value("${baidu.face.sdk.app_id}")
	private String appId;

	@Value("${baidu.face.sdk.api_key}")
	private String apiKey;

	@Value("${baidu.face.sdk.secret_key}")
	private String secretKey;

	private AipFace client = null;

	@PostConstruct
	public void init() throws Exception {
		client = new AipFace(appId, apiKey, secretKey);

		// 可选：设置网络连接参数
		client.setConnectionTimeoutInMillis(2000);
		client.setSocketTimeoutInMillis(60000);

	}

	@PreDestroy
	public void destroy() throws Exception {
	}

	public boolean faceRegister(String photoPath, String uid, String name) {
		HashMap<String, String> options = new HashMap<String, String>();
		JSONObject res = client.addUser(uid, name, Arrays.asList("group1"), photoPath, options);
		int errorCode = 0;
		try {
			errorCode = res.getInt("error_code");
		} catch (JSONException e1) {
			errorCode = 1;
			e1.printStackTrace();
		}
		return errorCode == 0;
	}

}
