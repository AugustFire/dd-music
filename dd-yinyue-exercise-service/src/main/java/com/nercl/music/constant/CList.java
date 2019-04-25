package com.nercl.music.constant;

public interface CList {

	interface Api {

		interface Client {

			/**
			 * 服务调用成功
			 */
			Integer OK = 200;

			/**
			 * 没有权限
			 */
			Integer NO_PRIVILEGE = 201;

			/**
			 * 用户名或者密码错误
			 */
			Integer LOGIN_OR_PASSWORD_ERROR = 202;

			/**
			 * 用户未登录，或者登录过期
			 */
			Integer UNAUTHORIZED = 401;

			/**
			 * 处理失败，往平台写入数据失败或者平台校验请求失败时返回
			 */
			Integer PROCESSING_FAILED = 402;

			/**
			 * 客户端版本过老，需要强制升级才能使用，8.1.获取通讯密钥ck 节可能会返回
			 */
			Integer NEED_UPDATE = 403;

			/**
			 * 找不到服务页面
			 */
			Integer NOT_FOUND = 404;

			/**
			 * 账号同时在多地方登录
			 */
			Integer REPEATE_LOGIN = 405;

			/**
			 * 系统异常
			 */
			Integer INTERNAL_ERROR = 500;

			/**
			 * 客户端校验未通过，禁止访问
			 */
			Integer ACCESS_DENIED = 503;

			/**
			 * 非法密钥
			 */
			Integer INVALID_KEY = 504;

		}

		interface QuestionType {

			/**
			 * 单选题
			 */
			Integer SINGLE_SELECT = 1;

			/**
			 * 多选题
			 */
			Integer MULTI_SELECT = 2;

			/**
			 * 简答题
			 */
			Integer SHORT_ANSWER = 3;

			/**
			 * 视唱题
			 */
			Integer LOOK_SING = 4;
		}

		interface ResType {

			/**
			 * 图片
			 */
			Integer IMAGE = 1;

			/**
			 * 五线谱
			 */
			Integer STAVE = 2;

			/**
			 * 音视频
			 */
			Integer AUDIO_VIDEO = 3;

		}

		interface SubjectType {

			/**
			 * 乐理
			 */
			Integer YUE_LI = 1;

			/**
			 * 视唱
			 */
			Integer LOOK_SING = 2;

			/**
			 * 听音
			 */
			Integer TING_YIN = 3;
		}

		interface AnswerType {

			/**
			 * 选项
			 */
			Integer OPTION = 1;

			/**
			 * 五线谱
			 */
			Integer STAVE = 2;

			/**
			 * 音程
			 */
			Integer YINCHENG = 3;

			/**
			 * 旋律
			 */
			Integer XUANLV = 4;

			/**
			 * 和旋性质
			 */
			Integer HEXUANXINGZHI = 5;
		}

		interface TitleFileType {

			/**
			 * 图片
			 */
			Integer IMAGE = 1;

			/**
			 * 五线谱
			 */
			Integer STAVE = 2;

			/**
			 * 音视频
			 */
			Integer AUDIO_VIDEO = 3;
		}

		interface OptionType {

			/**
			 * 文本
			 */
			Integer TEXT = 0;

			/**
			 * 图片
			 */
			Integer IMAGE = 1;
		}

		interface SecretLevel {
			/**
			 * 可公开
			 */
			Integer OPEN = 0;

			/**
			 * 秘密
			 */
			Integer SECRET = 1;

			/**
			 * 机密
			 */
			Integer ADVANCE_SECRET = 2;

			/**
			 * 绝密
			 */
			Integer HIGHER_SECRET = 3;

		}

		interface Tune {
			/**
			 * b g大调
			 */
			Integer B_G_DADIAO = -7;

			/**
			 * b d大调
			 */
			Integer B_D_DADIAO = -6;

			/**
			 * b c大调
			 */
			Integer B_C_DADIAO = -5;

			/**
			 * b a大调
			 */
			Integer B_A_DADIAO = -4;
			/**
			 * b e大调
			 */
			Integer B_E_DADIAO = -3;

			/**
			 * b b大调
			 */
			Integer B_B_DADIAO = -2;

			/**
			 * f大调
			 */
			Integer B_F_DADIAO = -1;

			/**
			 * f 大调
			 */
			Integer F_DADIAO = 0;

			/**
			 * c 大调
			 */
			Integer C_DADIAO = 1;

			/**
			 * b 大调
			 */
			Integer B_DADIAO = 2;

			/**
			 * e 大调
			 */
			Integer E_DADIAO = 3;

			/**
			 * a 大调
			 */
			Integer A_DADIAO = 4;

			/**
			 * d 大调
			 */
			Integer D_DADIAO = 5;

			/**
			 * g 大调
			 */
			Integer G_DADIAO = 6;

			/**
			 * c大调
			 */
			Integer DADIAO = 7;

		}

		interface ExamField {

			/**
			 * 单音
			 */
			Integer SINGLE = 10;

			/**
			 * 音程音高记写
			 */
			Integer INTERVAL_PITCH = 11;

			/**
			 * 音程性质
			 */
			Integer INTERVAL_NATURE = 12;

			/**
			 * 和弦音高记写
			 */
			Integer CHORD_PITCH = 13;

			/**
			 * 和弦性质
			 */
			Integer CHORD_NATURE = 14;

			/**
			 * 节奏听写
			 */
			Integer PHYTHMIC = 15;

			/**
			 * 旋律听写
			 */
			Integer MELODY = 16;

			/**
			 * 节奏时值组合
			 */
			Integer RHYTHMIC_DURATION = 20;

			/**
			 * 写音程、和弦
			 */
			Integer WRITING_INTERVAL_OR_CHORD = 21;

			/**
			 * 写音阶
			 */
			Integer WRITING_SYLLABLE = 22;

			/**
			 * 旋律分析写作
			 */
			Integer MELODY_WRITING = 23;

			/**
			 * 移调
			 */
			Integer TRANSPOSE = 24;

			/**
			 * 视唱
			 */
			Integer SIGHT_SINGING = 30;

		}

		interface YinChengAnswer {

			/**
			 * 纯一度
			 */
			Integer PERFECT_EIGHTH = 0;

			/**
			 * 小二度
			 */
			Integer MINOR_SECOND = 1;

			/**
			 * 大二度
			 */
			Integer MAJOR_SECOND = 2;

			/**
			 * 小三度
			 */
			Integer MINOR_THIRD = 3;

			/**
			 * 大三度
			 */
			Integer MAJOR_THIRD = 4;

			/**
			 * 纯四度
			 */
			Integer PERFECT_FOURTH = 5;

			/**
			 * 增四度
			 */
			Integer AUGMENTED_FOURTH = 6;

			/**
			 * 减五度
			 */
			Integer DIMINISHED_FIFTH = 7;

			/**
			 * 纯五度
			 */
			Integer PERFECT_FIFTH = 8;

			/**
			 * 小六度
			 */
			Integer MINOR_SIXTH = 9;

			/**
			 * 大六度
			 */
			Integer MAJOR_SIXTH = 10;

			/**
			 * 小七度
			 */
			Integer MINOR_SEVENTH = 11;

			/**
			 * 大七度
			 */
			Integer MAJOR_SEVENTH = 12;

			/**
			 * 纯八度
			 */
			Integer OCTAVE = 13;
		}

		/**
		 * 西方调名
		 */
		interface KeySigName {

			Integer NONE = 0;

			Integer C_SHARP = 1;

			Integer F_SHARP = 2;

			Integer B = 3;

			Integer E = 4;

			Integer A = 5;

			Integer D = 6;

			Integer G = 7;

			Integer C = 8;

			Integer F = 9;

			Integer BB = 10;

			Integer BE = 11;

			Integer BA = 12;

			Integer BD = 13;

			Integer BG = 14;

			Integer BC = 15;

			Integer A_SHARP = 16;

			Integer D_SHARP = 17;

			Integer G_SHARP = 18;

			Integer L_C_SHARP = 19;

			Integer L_F_SHARP = 20;

			Integer L_B = 21;

			Integer L_E = 22;

			Integer L_A = 23;

			Integer L_D = 24;

			Integer L_G = 25;

			Integer L_C = 26;

			Integer L_F = 27;

			Integer L_BB = 28;

			Integer L_BE = 29;

			Integer L_BA = 30;
		}

		interface KeyMode {

			Integer NONE = 0;

			/**
			 * 自然大调
			 */
			Integer NATURAL_MAJOR = 1;

			/**
			 * 和声大调
			 */
			Integer HARMONY_MAJOR = 2;

			/**
			 * 旋律大调
			 */
			Integer MELODIC_MAJOR = 3;

			/**
			 * 自然小调
			 */
			Integer NATURAL_MINOR = 4;

			/**
			 * 和声小调
			 */
			Integer HARMONY_MINOR = 5;

			/**
			 * 旋律小调
			 */
			Integer MELODIC_MINOR = 6;

			/**
			 * 伊奥尼亚调式
			 */
			Integer AEOLIAN = 7;

			/**
			 * 利底亚调式
			 */
			Integer LYDIAN = 8;

			/**
			 * 混合利底亚调式
			 */
			Integer MIXOLYDIAN = 9;

			/**
			 * 爱奥尼亚调式
			 */
			Integer IONIAN = 10;

			/**
			 * 多利亚调式
			 */
			Integer DORIAN = 11;

			/**
			 * 弗里几亚调式
			 */
			Integer PHRYGIAN = 12;

			/**
			 * 洛克里亚调式
			 */
			Integer LOCRIAN = 13;
		}

		/**
		 * 中国调式注音
		 */
		interface ChineseTonic {

			Integer NONE = 0;

			/**
			 * 宫
			 */
			Integer GONG = 1;

			/**
			 * 商
			 */
			Integer SHANG = 2;

			/**
			 * 角
			 */
			Integer JIAO = 3;

			/**
			 * 徵
			 */
			Integer ZHI = 4;

			/**
			 * 羽
			 */
			Integer YU = 5;
		}

		/**
		 * 中国调式
		 */
		interface ChineseKeyMode {

			Integer NONE = 0;

			/**
			 * 五声调式
			 */
			Integer PENTATONIC = 1;

			/**
			 * 六声调式（加清角）
			 */
			Integer LIUSHENG_QINGJIAO = 2;

			/**
			 * 六声调式（加变宫）
			 */
			Integer LIUSHENG_BIANGONG = 3;

			/**
			 * 六声调式（加变徵）
			 */
			Integer LIUSHENG_BIANZHI = 4;

			/**
			 * 六声调式（加闰）
			 */
			Integer LIUSHENG_RUN = 5;

			/**
			 * 七声雅乐调式
			 */
			Integer QISHENG_YAYUE = 6;

			/**
			 * 七声清乐调式
			 */
			Integer QISHENG_QINGYUE = 7;

			/**
			 * 七声燕乐调式
			 */
			Integer QISHENG_YANYUE = 8;

		}

		/**
		 * 文件资源类型
		 */
		interface FileResType {

			/**
			 * 问题xml
			 */
			Integer QUESTION_XML = 1;

			/**
			 * 问题img
			 */
			Integer QUESTION_IMG = 2;

			/**
			 * 问题audio
			 */
			Integer QUESTION_AUDIO = 3;

			/**
			 * 练习者答案xml
			 */
			Integer EXERCISER_ANSWER_XML = 6;

			/**
			 * 练习者答案audio
			 */
			Integer EXERCISER_ANSWER_AUDIO = 7;

		}
	}

}
