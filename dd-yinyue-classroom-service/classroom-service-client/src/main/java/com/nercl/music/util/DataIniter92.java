package com.nercl.music.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nercl.music.cloud.entity.base.Grade;
import com.nercl.music.cloud.entity.classroom.Book;
import com.nercl.music.cloud.entity.classroom.Chapter;
import com.nercl.music.cloud.entity.classroom.TeachForm;
import com.nercl.music.cloud.entity.classroom.VersionDesc;
import com.nercl.music.cloud.service.BookService;
import com.nercl.music.cloud.service.ChapterService;
import com.nercl.music.cloud.service.ClassRoomService;
import com.nercl.music.cloud.service.GradeService;

@Component
public class DataIniter92 {

	@Autowired
	private GradeService gradeService;

	@Autowired
	private BookService bookService;

	@Autowired
	private ChapterService chapterService;

	@Autowired
	private ClassRoomService classRoomService;

	public void init() {
		String bookId = initBook();
		initChapters(bookId);
		initClassRoom();
	}

	private void initClassRoom() {
		classRoomService.found("4028810a615er364563613087e17f0006", "4028810a62b2b2b20162b2b3dcc7000b",
				VersionDesc.REN_YIN_BAN, "4028810a62b2b2b20162b2b3dd920015", false);
	}

	private String initBook() {
		Book book = new Book();
		book.setTitle("义务教育教科书音乐（五线谱）九年级下册");
		book.setPublishHouse("人民音乐出版社");
		book.setIntro("义务教育教科书音乐（五线谱）九年级下册，人民音乐出版社出版发行，北京美通印刷有限公司印刷");
		book.setIsbn("ISBN 978-7-103-04289-2");
		book.setVersion(VersionDesc.REN_YIN_BAN);
		book.setIsFirstVolume(false);
		Grade grade = gradeService.findByCode("003-3");
		if (null != grade) {
			book.setGradeId(grade.getId());
		}
		book.setImgTfileId("");
		bookService.save(book);
		return book.getId();
	}

	private void initChapters(String bookId) {
		Chapter parent1 = new Chapter();
		parent1.setBookId(bookId);
		parent1.setTitle("岁月回声");
		chapterService.save(parent1);

		Chapter child1 = new Chapter();
		child1.setBookId(bookId);
		child1.setParentId(parent1.getId());
		child1.setTeachForm(TeachForm.YAN_CHANG);
		child1.setTitle("游击队歌");
		child1.setOrderBy(1);
		chapterService.save(child1);

		Chapter child2 = new Chapter();
		child2.setBookId(bookId);
		child2.setParentId(parent1.getId());
		child2.setTeachForm(TeachForm.YAN_CHANG);
		child2.setTitle("保卫黄河");
		child2.setOrderBy(2);
		chapterService.save(child2);

		Chapter child3 = new Chapter();
		child3.setBookId(bookId);
		child3.setParentId(parent1.getId());
		child3.setTeachForm(TeachForm.XIN_SHANG);
		child3.setTitle("保卫黄河");
		child3.setOrderBy(3);
		chapterService.save(child3);

		Chapter child4 = new Chapter();
		child4.setBookId(bookId);
		child4.setParentId(parent1.getId());
		child4.setTeachForm(TeachForm.XIN_SHANG);
		child4.setTitle("过雪山草地");
		child4.setOrderBy(4);
		chapterService.save(child4);

		Chapter child5 = new Chapter();
		child5.setBookId(bookId);
		child5.setParentId(parent1.getId());
		child5.setTeachForm(TeachForm.XIN_SHANG);
		child5.setTitle("屠城");
		child5.setOrderBy(5);
		chapterService.save(child5);

		Chapter child6 = new Chapter();
		child6.setBookId(bookId);
		child6.setParentId(parent1.getId());
		child6.setTeachForm(TeachForm.XIN_SHANG);
		child6.setTitle("月光");
		child6.setOrderBy(6);
		chapterService.save(child6);

		Chapter child7 = new Chapter();
		child7.setBookId(bookId);
		child7.setParentId(parent1.getId());
		child7.setTeachForm(TeachForm.XIN_SHANG);
		child7.setTitle("生命之杯");
		child7.setOrderBy(7);
		chapterService.save(child7);

		Chapter parent2 = new Chapter();
		parent2.setBookId(bookId);
		parent2.setTitle("舞剧之魂");
		chapterService.save(parent2);

		Chapter child21 = new Chapter();
		child21.setBookId(bookId);
		child21.setParentId(parent2.getId());
		child21.setTeachForm(TeachForm.YAN_CHANG);
		child21.setTitle("军民团结一家亲");
		child21.setOrderBy(1);
		chapterService.save(child21);

		Chapter child22 = new Chapter();
		child22.setBookId(bookId);
		child22.setParentId(parent2.getId());
		child22.setTeachForm(TeachForm.XIN_SHANG);
		child22.setTitle("军民团结一家亲");
		child22.setOrderBy(2);
		chapterService.save(child22);

		Chapter child23 = new Chapter();
		child23.setBookId(bookId);
		child23.setParentId(parent2.getId());
		child23.setTeachForm(TeachForm.XIN_SHANG);
		child23.setTitle("场景音乐");
		child23.setOrderBy(3);
		chapterService.save(child23);

		Chapter child24 = new Chapter();
		child24.setBookId(bookId);
		child24.setParentId(parent2.getId());
		child24.setTeachForm(TeachForm.XIN_SHANG);
		child24.setTitle("西班牙舞曲");
		child24.setOrderBy(4);
		chapterService.save(child24);

		Chapter child25 = new Chapter();
		child25.setBookId(bookId);
		child25.setParentId(parent2.getId());
		child25.setTeachForm(TeachForm.XIN_SHANG);
		child25.setTitle("斯娃尼达尔圆舞曲");
		child25.setOrderBy(5);
		chapterService.save(child25);

		Chapter child26 = new Chapter();
		child26.setBookId(bookId);
		child26.setParentId(parent2.getId());
		child26.setTeachForm(TeachForm.XIN_SHANG);
		child26.setTitle("婚礼场面舞");
		child26.setOrderBy(6);
		chapterService.save(child26);

		Chapter child27 = new Chapter();
		child27.setBookId(bookId);
		child27.setParentId(parent2.getId());
		child27.setTeachForm(TeachForm.XIN_SHANG);
		child27.setTitle("飞天仙子");
		child27.setOrderBy(7);
		chapterService.save(child27);

		Chapter child28 = new Chapter();
		child28.setBookId(bookId);
		child28.setParentId(parent2.getId());
		child28.setTeachForm(TeachForm.YAN_ZOU);
		child28.setTitle("军民团结一家亲");
		child28.setOrderBy(8);
		chapterService.save(child28);

		Chapter parent3 = new Chapter();
		parent3.setBookId(bookId);
		parent3.setTitle("黑土传情");
		chapterService.save(parent3);

		Chapter child31 = new Chapter();
		child31.setBookId(bookId);
		child31.setParentId(parent3.getId());
		child31.setTeachForm(TeachForm.YAN_CHANG);
		child31.setTitle("东北风");
		child31.setOrderBy(1);
		chapterService.save(child31);

		Chapter child32 = new Chapter();
		child32.setBookId(bookId);
		child32.setParentId(parent3.getId());
		child32.setTeachForm(TeachForm.YAN_CHANG);
		child32.setTitle("乌苏里船歌");
		child32.setOrderBy(2);
		chapterService.save(child32);

		Chapter child33 = new Chapter();
		child33.setBookId(bookId);
		child33.setParentId(parent3.getId());
		child33.setTeachForm(TeachForm.XIN_SHANG);
		child33.setTitle("乌苏里船歌");
		child33.setOrderBy(3);
		chapterService.save(child33);

		Chapter child34 = new Chapter();
		child34.setBookId(bookId);
		child34.setParentId(parent3.getId());
		child34.setTeachForm(TeachForm.XIN_SHANG);
		child34.setTitle("我的家乡多美好");
		child34.setOrderBy(4);
		chapterService.save(child34);

		Chapter child35 = new Chapter();
		child35.setBookId(bookId);
		child35.setParentId(parent3.getId());
		child35.setTeachForm(TeachForm.XIN_SHANG);
		child35.setTitle("五样热情的歌");
		child35.setOrderBy(5);
		chapterService.save(child35);

		Chapter child36 = new Chapter();
		child36.setBookId(bookId);
		child36.setParentId(parent3.getId());
		child36.setTeachForm(TeachForm.XIN_SHANG);
		child36.setTitle("翁嘿呀（片段）");
		child36.setOrderBy(6);
		chapterService.save(child36);

		Chapter child37 = new Chapter();
		child37.setBookId(bookId);
		child37.setParentId(parent3.getId());
		child37.setTeachForm(TeachForm.XIN_SHANG);
		child37.setTitle("江河水");
		child37.setOrderBy(7);
		chapterService.save(child37);

		Chapter parent4 = new Chapter();
		parent4.setBookId(bookId);
		parent4.setTitle("非洲灵感");
		chapterService.save(parent4);

		Chapter child41 = new Chapter();
		child41.setBookId(bookId);
		child41.setParentId(parent4.getId());
		child41.setTeachForm(TeachForm.YAN_CHANG);
		child41.setTitle("阿伊亚——非洲灵感");
		child41.setOrderBy(1);
		chapterService.save(child41);

		Chapter child42 = new Chapter();
		child42.setBookId(bookId);
		child42.setParentId(parent4.getId());
		child42.setTeachForm(TeachForm.XIN_SHANG);
		child42.setTitle("非洲节日");
		child42.setOrderBy(2);
		chapterService.save(child42);

		Chapter child43 = new Chapter();
		child33.setBookId(bookId);
		child43.setParentId(parent4.getId());
		child43.setTeachForm(TeachForm.XIN_SHANG);
		child43.setTitle("鼓乐");
		child43.setOrderBy(3);
		chapterService.save(child43);

		Chapter child44 = new Chapter();
		child44.setBookId(bookId);
		child44.setParentId(parent4.getId());
		child44.setTeachForm(TeachForm.XIN_SHANG);
		child44.setTitle("鼓声");
		child44.setOrderBy(4);
		chapterService.save(child44);

		Chapter parent5 = new Chapter();
		parent5.setBookId(bookId);
		parent5.setTitle("戏曲撷英");
		chapterService.save(parent5);

		Chapter child51 = new Chapter();
		child51.setBookId(bookId);
		child51.setParentId(parent5.getId());
		child51.setTeachForm(TeachForm.YAN_CHANG);
		child51.setTitle("对花");
		child51.setOrderBy(1);
		chapterService.save(child51);

		Chapter child52 = new Chapter();
		child52.setBookId(bookId);
		child52.setParentId(parent5.getId());
		child52.setTeachForm(TeachForm.XIN_SHANG);
		child52.setTitle("对花");
		child52.setOrderBy(2);
		chapterService.save(child52);

		Chapter child53 = new Chapter();
		child53.setBookId(bookId);
		child53.setParentId(parent5.getId());
		child53.setTeachForm(TeachForm.XIN_SHANG);
		child53.setTitle("报花名");
		child53.setOrderBy(3);
		chapterService.save(child53);

		Chapter child54 = new Chapter();
		child54.setBookId(bookId);
		child54.setParentId(parent5.getId());
		child54.setTeachForm(TeachForm.XIN_SHANG);
		child54.setTitle("刘大哥讲话理太偏");
		child54.setOrderBy(4);
		chapterService.save(child54);

		Chapter child55 = new Chapter();
		child55.setBookId(bookId);
		child55.setParentId(parent5.getId());
		child55.setTeachForm(TeachForm.XIN_SHANG);
		child55.setTitle("十八相送");
		child55.setOrderBy(5);
		chapterService.save(child55);

	}

}
