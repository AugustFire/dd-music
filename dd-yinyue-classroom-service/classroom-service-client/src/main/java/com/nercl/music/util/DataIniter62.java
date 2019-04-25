package com.nercl.music.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nercl.music.cloud.entity.base.Classes;
import com.nercl.music.cloud.entity.base.Grade;
import com.nercl.music.cloud.entity.base.LearnStage;
import com.nercl.music.cloud.entity.base.School;
import com.nercl.music.cloud.entity.classroom.Book;
import com.nercl.music.cloud.entity.classroom.Chapter;
import com.nercl.music.cloud.entity.classroom.TeachForm;
import com.nercl.music.cloud.entity.classroom.VersionDesc;
import com.nercl.music.cloud.service.BookService;
import com.nercl.music.cloud.service.ChapterService;
import com.nercl.music.cloud.service.ClassRoomService;
import com.nercl.music.cloud.service.ClassesService;
import com.nercl.music.cloud.service.GradeService;
import com.nercl.music.cloud.service.LearnStageService;
import com.nercl.music.cloud.service.SchoolService;

@Component
public class DataIniter62 {

	@Autowired
	private LearnStageService learnStageService;

	@Autowired
	private GradeService gradeService;

	@Autowired
	private SchoolService schoolService;

	@Autowired
	private ClassesService classesService;

	@Autowired
	private BookService bookService;

	@Autowired
	private ChapterService chapterService;

	@Autowired
	private ClassRoomService classRoomService;

	public void init() {
//		 initLearnStageGradeSchool();
//		 initSchoolClass();
//		 String bookId = initBook();
//		 initChapters(bookId);
//		 initClassRoom();
	}

	private void initClassRoom() {
		classRoomService.found("4028810a615er364563613087e17f0006", "4028810a62b2b2b20162b2b3dcc7000b", VersionDesc.REN_YIN_BAN, "4028810a62b2b2b20162b2b3dd920015", true);
	}

	private String initBook() {
		Book book = new Book();
		book.setTitle("义务教育教科书音乐（五线谱）六年级下册");
		book.setPublishHouse("人民教育出版社");
		book.setIntro("义务教育教科书音乐（五线谱）六年级 下册，人民音乐出版社出版发行，北京美通印刷有限公司印刷");
		book.setIsbn("ISBN 978-7-103-04298-4");
		book.setVersion(VersionDesc.REN_YIN_BAN);
		Grade grade = gradeService.findByCode("002-6");
		if (null != grade) {
			book.setGradeId(grade.getId());
		}
		book.setImgTfileId("");
		bookService.save(book);
		return book.getId();
	}

	private void initSchoolClass() {
		String cityId = "8a106ed9624c59b601624c5b1e5f0736";
		String regionId = "8a106ed9624c59b601624c5b1ef1073b";
		School school = new School();
		school.setCode("huashifuxiao");
		LearnStage learnStage = learnStageService.findByCode("002");
		if (null != learnStage) {
			school.setLearnStageId(learnStage.getId());
		}
		school.setCityId(cityId);
		school.setRegionId(regionId);
		school.setName("华中师范大学附属小学");
		schoolService.save(school);

		Classes classes = new Classes();
		classes.setSchoolId(school.getId());
		classes.setName("六年级三班");
		Grade grade = gradeService.findByCode("002-6");
		if (null != grade) {
			classes.setGradeId(grade.getId());
		}
		classes.setStartYear(2018);
		classes.setEndYear(2019);
		classesService.save(classes);
	}

	private void initChapters(String bookId) {
		Chapter parent1 = new Chapter();
		parent1.setBookId(bookId);
		parent1.setTitle("古风新韵");
		chapterService.save(parent1);

		Chapter child1 = new Chapter();
		child1.setBookId(bookId);
		child1.setParentId(parent1.getId());
		child1.setTeachForm(TeachForm.LING_TING);
		child1.setTitle("关山月");
		child1.setOrderBy(1);
		chapterService.save(child1);

		Chapter child2 = new Chapter();
		child2.setBookId(bookId);
		child2.setParentId(parent1.getId());
		child2.setTeachForm(TeachForm.LING_TING);
		child2.setTitle("但愿人长久");
		child2.setOrderBy(2);
		chapterService.save(child2);

		Chapter child3 = new Chapter();
		child3.setBookId(bookId);
		child3.setParentId(parent1.getId());
		child3.setTeachForm(TeachForm.YAN_CHANG);
		child3.setTitle("游子吟");
		child3.setOrderBy(3);
		chapterService.save(child3);

		Chapter child4 = new Chapter();
		child4.setBookId(bookId);
		child4.setParentId(parent1.getId());
		child4.setTeachForm(TeachForm.YAN_CHANG);
		child4.setTitle("花非花");
		child4.setOrderBy(4);
		chapterService.save(child4);

		Chapter child5 = new Chapter();
		child5.setBookId(bookId);
		child5.setParentId(parent1.getId());
		child5.setTeachForm(TeachForm.ZHI_SHI_JI_NENG);
		child5.setTitle("古琴");
		child5.setOrderBy(5);
		chapterService.save(child5);

		Chapter child6 = new Chapter();
		child6.setBookId(bookId);
		child6.setParentId(parent1.getId());
		child6.setTeachForm(TeachForm.YAN_ZOU);
		child6.setTitle("学吹竖笛");
		child6.setOrderBy(6);
		chapterService.save(child6);

		Chapter parent2 = new Chapter();
		parent2.setBookId(bookId);
		parent2.setTitle("月下踏歌");
		chapterService.save(parent2);

		Chapter child21 = new Chapter();
		child21.setBookId(bookId);
		child21.setParentId(parent2.getId());
		child21.setTeachForm(TeachForm.LING_TING);
		child21.setTitle("阿细跳月");
		child21.setOrderBy(1);
		chapterService.save(child21);

		Chapter child22 = new Chapter();
		child22.setBookId(bookId);
		child22.setParentId(parent2.getId());
		child22.setTeachForm(TeachForm.LING_TING);
		child22.setTitle("火把节（片段）");
		child22.setOrderBy(2);
		chapterService.save(child22);

		Chapter child23 = new Chapter();
		child23.setBookId(bookId);
		child23.setParentId(parent2.getId());
		child23.setTeachForm(TeachForm.YAN_CHANG);
		child23.setTitle("转圆圈");
		child23.setOrderBy(3);
		chapterService.save(child23);

		Chapter child24 = new Chapter();
		child24.setBookId(bookId);
		child24.setParentId(parent2.getId());
		child24.setTeachForm(TeachForm.YAN_CHANG);
		child24.setTitle("我抱着月光，月光抱着我");
		child24.setOrderBy(4);
		chapterService.save(child24);

		Chapter child25 = new Chapter();
		child25.setBookId(bookId);
		child25.setParentId(parent2.getId());
		child25.setTeachForm(TeachForm.ZHI_SHI_JI_NENG);
		child25.setTitle("发声练习民族乐器分类（一）");
		child25.setOrderBy(5);
		chapterService.save(child25);

		Chapter parent3 = new Chapter();
		parent3.setBookId(bookId);
		parent3.setTitle("银屏之声");
		chapterService.save(parent3);

		Chapter child31 = new Chapter();
		child31.setBookId(bookId);
		child31.setParentId(parent3.getId());
		child31.setTeachForm(TeachForm.LING_TING);
		child31.setTitle("爱是一首歌");
		child31.setOrderBy(1);
		chapterService.save(child31);

		Chapter child32 = new Chapter();
		child32.setBookId(bookId);
		child32.setParentId(parent3.getId());
		child32.setTeachForm(TeachForm.YAN_CHANG);
		child32.setTitle("两颗小星星");
		child32.setOrderBy(2);
		chapterService.save(child32);

		Chapter child33 = new Chapter();
		child33.setBookId(bookId);
		child33.setParentId(parent3.getId());
		child33.setTeachForm(TeachForm.YAN_CHANG);
		child33.setTitle("滑雪歌");
		child33.setOrderBy(3);
		chapterService.save(child33);

		Chapter child34 = new Chapter();
		child34.setBookId(bookId);
		child34.setParentId(parent3.getId());
		child34.setTeachForm(TeachForm.YAN_CHANG);
		child34.setTitle("DO RE MI");
		child34.setOrderBy(4);
		chapterService.save(child34);

		Chapter child35 = new Chapter();
		child35.setBookId(bookId);
		child35.setParentId(parent3.getId());
		child35.setTeachForm(TeachForm.YAN_ZOU);
		child35.setTitle("学吹竖笛");
		child35.setOrderBy(5);
		chapterService.save(child35);

		Chapter parent4 = new Chapter();
		parent4.setBookId(bookId);
		parent4.setTitle("美好祝愿");
		chapterService.save(parent4);

		Chapter child41 = new Chapter();
		child41.setBookId(bookId);
		child41.setParentId(parent4.getId());
		child41.setTeachForm(TeachForm.LING_TING);
		child41.setTitle("龙腾虎跃");
		child41.setOrderBy(1);
		chapterService.save(child41);

		Chapter child42 = new Chapter();
		child42.setBookId(bookId);
		child42.setParentId(parent4.getId());
		child42.setTeachForm(TeachForm.YAN_CHANG);
		child42.setTitle("拍手拍手");
		child42.setOrderBy(2);
		chapterService.save(child42);

		Chapter child43 = new Chapter();
		child33.setBookId(bookId);
		child43.setParentId(parent4.getId());
		child43.setTeachForm(TeachForm.YAN_CHANG);
		child43.setTitle("明天会更好");
		child43.setOrderBy(3);
		chapterService.save(child43);

		Chapter child44 = new Chapter();
		child44.setBookId(bookId);
		child44.setParentId(parent4.getId());
		child44.setTeachForm(TeachForm.ZHI_SHI_JI_NENG);
		child44.setTitle("发声练习民族乐器分类（二）");
		child44.setOrderBy(4);
		chapterService.save(child44);

		Chapter parent5 = new Chapter();
		parent5.setBookId(bookId);
		parent5.setTitle("快乐的阳光");
		chapterService.save(parent5);

		Chapter child51 = new Chapter();
		child51.setBookId(bookId);
		child51.setParentId(parent5.getId());
		child51.setTeachForm(TeachForm.LING_TING);
		child51.setTitle("守住这一片阳光");
		child51.setOrderBy(1);
		chapterService.save(child51);

		Chapter child52 = new Chapter();
		child52.setBookId(bookId);
		child52.setParentId(parent5.getId());
		child52.setTeachForm(TeachForm.LING_TING);
		child52.setTitle("光辉的太阳");
		child52.setOrderBy(2);
		chapterService.save(child52);

		Chapter child53 = new Chapter();
		child53.setBookId(bookId);
		child53.setParentId(parent5.getId());
		child53.setTeachForm(TeachForm.YAN_CHANG);
		child53.setTitle("榕树爷爷");
		child53.setOrderBy(3);
		chapterService.save(child53);

		Chapter child54 = new Chapter();
		child54.setBookId(bookId);
		child54.setParentId(parent5.getId());
		child54.setTeachForm(TeachForm.ZHI_SHI_JI_NENG);
		child54.setTitle("一把雨伞圆溜溜");
		child54.setOrderBy(4);
		chapterService.save(child54);

		Chapter child55 = new Chapter();
		child55.setBookId(bookId);
		child55.setParentId(parent5.getId());
		child55.setTeachForm(TeachForm.ZHI_SHI_JI_NENG);
		child55.setTitle("变声期嗓音发声练习");
		child55.setOrderBy(5);
		chapterService.save(child55);

		Chapter child56 = new Chapter();
		child56.setBookId(bookId);
		child56.setParentId(parent5.getId());
		child56.setTeachForm(TeachForm.YAN_ZOU);
		child56.setTitle("学吹竖笛");
		child56.setOrderBy(6);
		chapterService.save(child56);

		Chapter parent6 = new Chapter();
		parent6.setBookId(bookId);
		parent6.setTitle("神奇的印象");
		chapterService.save(parent6);

		Chapter child61 = new Chapter();
		child61.setBookId(bookId);
		child61.setParentId(parent6.getId());
		child61.setTeachForm(TeachForm.LING_TING);
		child61.setTitle("海德薇格主题");
		child61.setOrderBy(1);
		chapterService.save(child61);

		Chapter child62 = new Chapter();
		child62.setBookId(bookId);
		child62.setParentId(parent6.getId());
		child62.setTeachForm(TeachForm.YAN_CHANG);
		child62.setTitle("瀑布（片段）");
		child62.setOrderBy(2);
		chapterService.save(child62);

		Chapter child63 = new Chapter();
		child63.setBookId(bookId);
		child63.setParentId(parent6.getId());
		child63.setTeachForm(TeachForm.YAN_CHANG);
		child63.setTitle("火车来了");
		child63.setOrderBy(3);
		chapterService.save(child63);

		Chapter child64 = new Chapter();
		child64.setBookId(bookId);
		child64.setParentId(parent6.getId());
		child64.setTeachForm(TeachForm.ZHI_SHI_JI_NENG);
		child64.setTitle("飞天曲");
		child64.setOrderBy(4);
		chapterService.save(child64);

		Chapter child65 = new Chapter();
		child65.setBookId(bookId);
		child65.setParentId(parent6.getId());
		child65.setTeachForm(TeachForm.ZHI_SHI_JI_NENG);
		child65.setTitle("民族管弦乐队演奏图");
		child65.setOrderBy(5);
		chapterService.save(child65);

		Chapter parent7 = new Chapter();
		parent7.setBookId(bookId);
		parent7.setTitle("放飞梦想");
		chapterService.save(parent7);

		Chapter child71 = new Chapter();
		child71.setBookId(bookId);
		child71.setParentId(parent7.getId());
		child71.setTeachForm(TeachForm.LING_TING);
		child71.setTitle("和平颂（片段）");
		child71.setOrderBy(1);
		chapterService.save(child71);

		Chapter child72 = new Chapter();
		child72.setBookId(bookId);
		child72.setParentId(parent7.getId());
		child72.setTeachForm(TeachForm.LING_TING);
		child72.setTitle("欢乐颂");
		child72.setOrderBy(2);
		chapterService.save(child72);

		Chapter child73 = new Chapter();
		child73.setBookId(bookId);
		child73.setParentId(parent7.getId());
		child73.setTeachForm(TeachForm.YAN_CHANG);
		child73.setTitle("永远是朋友");
		child73.setOrderBy(3);
		chapterService.save(child73);

		Chapter child74 = new Chapter();
		child74.setBookId(bookId);
		child74.setParentId(parent7.getId());
		child74.setTeachForm(TeachForm.ZHI_SHI_JI_NENG);
		child74.setTitle("我们是朋友");
		child74.setOrderBy(4);
		chapterService.save(child74);

		Chapter child75 = new Chapter();
		child75.setBookId(bookId);
		child75.setParentId(parent7.getId());
		child75.setTeachForm(TeachForm.ZHI_SHI_JI_NENG);
		child75.setTitle("贝多芬");
		child75.setOrderBy(5);
		chapterService.save(child75);
	}

	private void initLearnStageGradeSchool() {

		LearnStage youeryuan = new LearnStage();
		youeryuan.setTitle("幼儿园");
		youeryuan.setCode("001");
		learnStageService.save(youeryuan);

		Grade tuoban = new Grade();
		tuoban.setName("托班");
		tuoban.setCode("001-1");
		tuoban.setLearnStageId(youeryuan.getId());
		gradeService.save(tuoban);

		Grade xiaoban = new Grade();
		xiaoban.setName("小班");
		xiaoban.setCode("001-2");
		xiaoban.setLearnStageId(youeryuan.getId());
		gradeService.save(xiaoban);

		Grade zhongban = new Grade();
		zhongban.setName("中班");
		zhongban.setCode("001-3");
		zhongban.setLearnStageId(youeryuan.getId());
		gradeService.save(zhongban);

		Grade daban = new Grade();
		daban.setName("大班");
		daban.setCode("001-4");
		daban.setLearnStageId(youeryuan.getId());
		gradeService.save(daban);

		LearnStage xiaoxue = new LearnStage();
		xiaoxue.setTitle("小学");
		xiaoxue.setCode("002");
		learnStageService.save(xiaoxue);

		Grade yinianji = new Grade();
		yinianji.setName("一年级");
		yinianji.setCode("002-1");
		yinianji.setLearnStageId(xiaoxue.getId());
		gradeService.save(yinianji);

		Grade ernianji = new Grade();
		ernianji.setName("二年级");
		ernianji.setCode("002-2");
		ernianji.setLearnStageId(xiaoxue.getId());
		gradeService.save(ernianji);

		Grade sannianji = new Grade();
		sannianji.setName("三年级");
		sannianji.setCode("002-3");
		sannianji.setLearnStageId(xiaoxue.getId());
		gradeService.save(sannianji);

		Grade sinianji = new Grade();
		sinianji.setName("四年级");
		sinianji.setCode("002-4");
		sinianji.setLearnStageId(xiaoxue.getId());
		gradeService.save(sinianji);

		Grade wunianji = new Grade();
		wunianji.setName("五年级");
		wunianji.setCode("002-5");
		wunianji.setLearnStageId(xiaoxue.getId());
		gradeService.save(wunianji);

		Grade liunianji = new Grade();
		liunianji.setName("六年级");
		liunianji.setCode("002-6");
		liunianji.setLearnStageId(xiaoxue.getId());
		gradeService.save(liunianji);

		LearnStage chuzhong = new LearnStage();
		chuzhong.setTitle("初中");
		chuzhong.setCode("003");
		learnStageService.save(chuzhong);

		Grade chuyi = new Grade();
		chuyi.setName("初一");
		chuyi.setCode("003-1");
		chuyi.setLearnStageId(chuzhong.getId());
		gradeService.save(chuyi);

		Grade chuer = new Grade();
		chuer.setName("初二");
		chuer.setCode("003-2");
		chuer.setLearnStageId(chuzhong.getId());
		gradeService.save(chuer);

		Grade chusan = new Grade();
		chusan.setName("初三");
		chusan.setCode("003-3");
		chusan.setLearnStageId(chuzhong.getId());
		gradeService.save(chusan);

		LearnStage gaozhong = new LearnStage();
		gaozhong.setTitle("高中");
		gaozhong.setCode("004");
		learnStageService.save(gaozhong);

		Grade gaoyi = new Grade();
		gaoyi.setName("高一");
		gaoyi.setCode("004-1");
		gaoyi.setLearnStageId(gaozhong.getId());
		gradeService.save(gaoyi);

		Grade gaoer = new Grade();
		gaoer.setName("高二");
		gaoer.setCode("004-2");
		gaoer.setLearnStageId(gaozhong.getId());
		gradeService.save(gaoer);

		Grade gaosan = new Grade();
		gaosan.setName("高三");
		gaosan.setCode("004-3");
		gaosan.setLearnStageId(gaozhong.getId());
		gradeService.save(gaosan);
	}

}
