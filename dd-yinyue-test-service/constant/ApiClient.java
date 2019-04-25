package com.nercl.music.constant;

public class ApiClient {

	/**
	 * ZUUL
	 */
	public static final String ZUUL_HOST = "http://localhost:8768";

	/**
	 * USER
	 */
	public static final String GET_USERS = ZUUL_HOST + "/user/users/{uids}";

	public static final String GET_USER = ZUUL_HOST + "/user/{uid}";

	public static final String GET_USER_PHOTO = ZUUL_HOST + "/user/{uid}/photo";

	public static final String GET_CLASSROOM_USER = ZUUL_HOST + "/user/user/users";

	/**
	 * RESOURCE
	 */
	public static final String GET_RESOURCE = ZUUL_HOST + "/resource/{rid}";

	public static final String UPLOAD_RESOURCE = ZUUL_HOST + "/resource";

	public static final String GET_RESOURCE_FILE = ZUUL_HOST + "/resource/{rid}/file";

	public static final String GET_RESOURCE_BYTES = ZUUL_HOST + "/resource/{rid}/bytes";

	public static final String GET_RESOURCES = ZUUL_HOST + "/resource/resources?rids={rids}";

	public static final String GET_RESOURCE_SONG = ZUUL_HOST + "/resource/song/{sid}";

	public static final String GET_RESOURCE_IMPRESS = ZUUL_HOST + "/resource/song/impress/{id}";
	
	public static final String GET_RESOURCE_ATTACHMENTS = ZUUL_HOST + "/resource/song/{id}/attachments";

	/**
	 * QUESTION
	 */
	public static final String SAVE_QUESTIONS = ZUUL_HOST + "/question";

	public static final String SAVE_QUESTIONS2 = ZUUL_HOST + "/question/question";

	public static final String GET_QUESTION = ZUUL_HOST + "/question/{qid}";
	
	public static final String GET_QUESTIONS = ZUUL_HOST + "/question/questions?ids={qids}";
	
	public static final String GET_APPROVAL = ZUUL_HOST + "/question/approval";

	/**
	 * 根据Id查询省信息
	 */
	public static final String GET_USER_DISTRICTS_PROVINCE = ZUUL_HOST + "/user/area/district/province";

	/**
	 * 根据Id查询城市信息
	 */
	public static final String GET_USER_DISTRICTS_CITY = ZUUL_HOST + "/user/area/district/city";

	/**
	 * 根据Id查询区信息
	 */
	public static final String GET_USER_DISTRICTS_REGION = ZUUL_HOST + "/user/area/district/region";

}
