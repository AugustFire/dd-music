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

	public static final String GET_CLASS_USER = ZUUL_HOST + "/classroom/class_user/{uid}";

	public static final String GET_CLASS = ZUUL_HOST + "/classroom/class/{cid}";

	public static final String GET_CHAPTER = ZUUL_HOST + "/classroom/chapter/{cid}";

	public static final String GET_MY_CLASSROOM = ZUUL_HOST + "/classroom/my_classrooms?uid={uid}";

	public static final String GET_TASK = ZUUL_HOST + "/classroom/tasks?rid={rid}";

	public static final String GET_CLASSSTUDENT_NUM = ZUUL_HOST + "/classroom/class_student/num?cid={cid}";

	public static final String GET_CLASSSTUDENTS = ZUUL_HOST + "/classroom/class_student/students?cid={cid}";

	public static final String GET_CLASSSTUDENTS2 = ZUUL_HOST + "/classroom/v2/class_user/students?cid={cid}";

	public static final String GET_CLASSES = ZUUL_HOST + "/classroom/class_student/classes?sid={sid}&gid={gid}";

	public static final String GET_GRADES = ZUUL_HOST + "/classroom/class/grades?sid={sid}";

	public static final String GET_GRADE_STUDENT_NUM = ZUUL_HOST
			+ "/classroom/class_student/grade_student_num?sid={sid}&gid={gid}";

	public static final String GET_GRADE_STUDENT_NUM2 = ZUUL_HOST
			+ "/classroom/v2/class_user/grade_student_num?sid={sid}&gid={gid}";

	public static final String GET_CLASS_STUDENT_NUM2 = ZUUL_HOST + "/classroom/v2/class_user/num?cid={cid}";

	public static final String GET_TEACHERS_CLASSES = ZUUL_HOST
			+ "/classroom/class_student/teachers_classes?sid={sid}&gid={gid}";

	public static final String ANSWER_NOTICE = ZUUL_HOST
			+ "/classroom/answer_notice?roomId={roomId}&senderId={senderId}&answer={answer}";

	public static final String GET_GRADE = ZUUL_HOST + "/classroom/grades?code={code}";

	public static final String GET_CLASSROOM_STUDENTS = ZUUL_HOST + "/classroom/{rid}/students";

	public static final String GET_CLASS_STUDENTS = ZUUL_HOST + "/classroom/class_user_students/{teacherId}";

	public static final String GET_SCHOOL_STUDENTS = ZUUL_HOST + "/classroom/school_students/{sid}";

	public static final String GET_TEACHER_INFO = ZUUL_HOST + "/classroom/v2/teacher_info?sid={sid}";

	public static final String GET_CLASS_STUDENTS_V2 = ZUUL_HOST + "/classroom/v2/class_user/students?cid={cid}";

	public static final String GET_TASKS = ZUUL_HOST + "/classroom/tasks?cids={cids}";

	public static final String GET_TASK_COMMENTS = ZUUL_HOST + "/classroom/task_comments?cids={cids}";

	public static final String GET_TASK_QUESTIONS = ZUUL_HOST + "/classroom/task/questions?cids={cids}";

	public static final String GET_TASK_QUESTIONS2 = ZUUL_HOST
			+ "/classroom/task/questions?cid={cid}&beginAt={beginAt}&endAt={endAt}";
	
	public static final String GET_CLASS_TEACHER = ZUUL_HOST + "/classroom/class_teacher/{cid}";
	
	public static final String GET_CLASS_STUDNETS = ZUUL_HOST + "/classroom/class_student/students?cid={cid}";

}
