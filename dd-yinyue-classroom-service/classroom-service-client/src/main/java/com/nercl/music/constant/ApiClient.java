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

	public static final String GET_USER2 = ZUUL_HOST + "/user/user2/{uid}";

	public static final String GET_USERS_GENDER = ZUUL_HOST + "/user/users/gender?ids={ids}";

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

	public static final String GET_RESOURCE_SONGS = ZUUL_HOST + "/resource/songs?sids={sids}";

	public static final String GET_RESOURCE_IMPRESS = ZUUL_HOST + "/resource/song/impress/{id}";

	public static final String GET_RESOURCE_ATTACHMENTS = ZUUL_HOST + "/resource/song/{id}/attachments";

	/**
	 * TEST
	 */
	public static final String SAVE_QUESTIONS = ZUUL_HOST + "/test";

	public static final String SAVE_QUESTIONS2 = ZUUL_HOST + "/test/question";

	public static final String GET_QUESTION = ZUUL_HOST + "/test/question2/{qid}";

	public static final String GET_USER_QUESTION = ZUUL_HOST + "/test/question/{qid}?uid={uid}";

	//根据题目id,用户id和作业id查询
	public static final String GET_USER_TASK_QUESTION = ZUUL_HOST + "/test/question/{qid}?uid={userId}&tid={taskId}";

	public static final String GET_USER_ANSWER = ZUUL_HOST + "/test/{qid}/answer/{uid}/{tid}";

	public static final String GET_QUESTIONS = ZUUL_HOST + "/test/questions?ids={qids}";
	
	public static final String GET_APPROVAL = ZUUL_HOST + "/test/approval";

	public static final String GET_TASK_AMOUNT = ZUUL_HOST + "/test/task_amount?task_id={task_id}";

	public static final String GET_TASK_ANSWERS = ZUUL_HOST + "/test/task_answers?taskId={taskId}&uid={uid}";

	public static final String GET_TASK_ANSWER_DETAILS = ZUUL_HOST
			+ "/test/task_answers_details?taskId={taskId}&uid={uid}";

	//批阅学生作答
	public static final String MARK_ANSWER = ZUUL_HOST + "/test/answer/{answerRecordId}?score={score}";

	public static final String COMMIT_ANSWER = ZUUL_HOST + "/test/{taskId}/answers/{uid}";

	public static final String AUTO_MARK = ZUUL_HOST + "/test/{taskId}/auto_mark";

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
