package com.nercl.music.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.nercl.music.constant.ApiClient;

@Component
public class CloudFileUtil {

	@Autowired
	private RestTemplate restTemplate;

	@SuppressWarnings("unchecked")
	public Map<String, Object> getResource(String rid) {
		return restTemplate.getForObject(ApiClient.GET_RESOURCE, Map.class, rid);
	}

	public InputStream download(String tfileId, String ext) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
		HttpEntity<String> entity = new HttpEntity<String>(headers);

		ResponseEntity<byte[]> response = restTemplate.exchange(ApiClient.GET_RESOURCE_BYTES, HttpMethod.GET, entity,
				byte[].class, tfileId);
		byte[] result = response.getBody();
		InputStream in = new ByteArrayInputStream(result);
		return in;

	}

	public byte[] downloadBytes(String tfileId, String ext) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
		HttpEntity<String> entity = new HttpEntity<String>(headers);

		ResponseEntity<byte[]> response = restTemplate.exchange(ApiClient.GET_RESOURCE_BYTES, HttpMethod.GET, entity,
				byte[].class, tfileId);
		byte[] bytes = response.getBody();
		return bytes;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String upload(File file, String json) {
		HttpHeaders headers = new HttpHeaders();
		MediaType mediaType = MediaType.parseMediaType("multipart/form-data;charset=UTF-8");
		headers.setContentType(mediaType);

		headers.setContentDispositionFormData("file", file.getName());

		MultiValueMap<String, Object> form = new LinkedMultiValueMap<String, Object>();
		FileSystemResource fileSystemResource = new FileSystemResource(file);
		form.add("file", fileSystemResource);
		form.add("json", json);

		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(form,
				headers);

		ResponseEntity<Map> responseEntity = restTemplate.exchange(ApiClient.UPLOAD_RESOURCE, HttpMethod.POST,
				requestEntity, Map.class);
		Map<String, Object> res = responseEntity.getBody();
		if (null == res) {
			return "";
		} else {
			return (String) res.getOrDefault("rid", "");
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String upload(InputStream is, String json) {

		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("multipart/form-data;charset=UTF-8");
		headers.setContentType(type);

		MultiValueMap<String, Object> form = new LinkedMultiValueMap<String, Object>();
		InputStreamResource inputStreamResource = new InputStreamResource(is);
		form.add("file", inputStreamResource);
		form.add("json", json);

		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(form,
				headers);
		ResponseEntity<Map> responseEntity = restTemplate.exchange(ApiClient.UPLOAD_RESOURCE, HttpMethod.POST,
				requestEntity, Map.class);
		Map<String, Object> res = responseEntity.getBody();
		if (null == res) {
			return "";
		} else {
			return (String) res.getOrDefault("rid", "");
		}
	}

}
