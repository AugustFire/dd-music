package com.nercl.music.constant;

public class ApiClient {

	public static final String ZUUL_HOST = "http://localhost:8768";

	public static final String GET_RESOURCE = ZUUL_HOST + "/resource/{rid}";

	public static final String UPLOAD_RESOURCE = ZUUL_HOST + "/resource";

	public static final String GET_RESOURCE_FILE = ZUUL_HOST + "/resource/{rid}/file";

	public static final String GET_RESOURCE_BYTES = ZUUL_HOST + "/resource/{rid}/bytes";
	
	public static final String GET_CLASS_USER = ZUUL_HOST + "/classroom/class_user/{rid}";

}
