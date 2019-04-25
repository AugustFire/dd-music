package com.nercl.music.constant;

public class ApiClient {

	/**
	 * ZUUL
	 */
	public static final String ZUUL_HOST = "http://localhost:8768";

	public static final String GET_CLASSROOM_SONGS = ZUUL_HOST + "/classroom/songs?uid={uid}&gid={gid}";

}
