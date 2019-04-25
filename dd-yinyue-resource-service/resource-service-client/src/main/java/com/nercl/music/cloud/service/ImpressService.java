package com.nercl.music.cloud.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.nercl.music.cloud.entity.impress.Impress;
import com.nercl.music.cloud.entity.resource.Dimension;

public interface ImpressService {

	List<Map<String, Object>> getImpress(String summary, Dimension dimension);

	Impress get(String id);

	boolean add(Impress impress);

	boolean add(String sid, Map<String, Object> map, InputStream in, String filename, String ext) throws IOException;

	Map<String, Object> getImpress(String id);

	List<Map<String, Object>> getBySong(String sid);

	void update(String iid, Map<String, Object> map, InputStream in, String filename, String ext) throws IOException;

	boolean delete(String iid);

}
