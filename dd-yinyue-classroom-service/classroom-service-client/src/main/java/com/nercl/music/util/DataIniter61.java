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
public class DataIniter61 {

	@Autowired
	private GradeService gradeService;

	@Autowired
	private BookService bookService;

	@Autowired
	private ChapterService chapterService;

	@Autowired
	private ClassRoomService classRoomService;

	public void init() {
//		 String bookId = initBook();
//		 initChapters(bookId);
		 initClassRoom();
	}

	private void initClassRoom() {
		classRoomService.found("4028810a615er364563613087e17f0006", "4028810a62b2b2b20162b2b3dcc7000b", VersionDesc.REN_YIN_BAN, "4028810a62b2b2b20162b2b3dd920015", true);
	}

	private String initBook() {
		Book book = new Book();
		book.setTitle("义务教育教科书音乐（五线谱）六年级上册");
		book.setPublishHouse("人民音乐出版社");
		book.setIntro("义务教育教科书音乐（五线谱）六年级上册，人民音乐出版社出版发行，北京美通印刷有限公司印刷");
		book.setIsbn("ISBN 978-7-103-04299-1");
		book.setVersion(VersionDesc.REN_YIN_BAN);
		book.setIsFirstVolume(true);
		Grade grade = gradeService.findByCode("002-6");
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
		parent1.setTitle("芬芳茉莉");
		chapterService.save(parent1);

		Chapter child1 = new Chapter();
		child1.setBookId(bookId);
		child1.setParentId(parent1.getId());
		child1.setTeachForm(TeachForm.LING_TING);
		child1.setTitle("茉莉花");
		child1.setOrderBy(1);
		chapterService.save(child1);

		Chapter child2 = new Chapter();
		child2.setBookId(bookId);
		child2.setParentId(parent1.getId());
		child2.setTeachForm(TeachForm.LING_TING);
		child2.setTitle("茉莉花");
		child2.setOrderBy(2);
		chapterService.save(child2);

		Chapter child3 = new Chapter();
		child3.setBookId(bookId);
		child3.setParentId(parent1.getId());
		child3.setTeachForm(TeachForm.LING_TING);
		child3.setTitle("茉莉花");
		child3.setOrderBy(3);
		chapterService.save(child3);

		Chapter child4 = new Chapter();
		child4.setBookId(bookId);
		child4.setParentId(parent1.getId());
		child4.setTeachForm(TeachForm.YAN_CHANG);
		child4.setTitle("东边升起月亮");
		child4.setOrderBy(4);
		chapterService.save(child4);
		
		Chapter child5 = new Chapter();
		child5.setBookId(bookId);
		child5.setParentId(parent1.getId());
		child5.setTeachForm(TeachForm.YAN_CHANG);
		child5.setTitle("茉莉花");
		child5.setOrderBy(5);
		chapterService.save(child5);
		
		Chapter child6 = new Chapter();
		child6.setBookId(bookId);
		child6.setParentId(parent1.getId());
		child6.setTeachForm(TeachForm.ZHI_SHI_JI_NENG);
		child6.setTitle("发声练习");
		child6.setOrderBy(6);
		chapterService.save(child6);
		
		Chapter parent2 = new Chapter();
		parent2.setBookId(bookId);
		parent2.setTitle("悠扬民歌");
		chapterService.save(parent2);

		Chapter child21 = new Chapter();
		child21.setBookId(bookId);
		child21.setParentId(parent2.getId());
		child21.setTeachForm(TeachForm.LING_TING);
		child21.setTitle("小河淌水");
		child21.setOrderBy(1);
		chapterService.save(child21);

		Chapter child22 = new Chapter();
		child22.setBookId(bookId);
		child22.setParentId(parent2.getId());
		child22.setTeachForm(TeachForm.LING_TING);
		child22.setTitle("迪克西岛");
		child22.setOrderBy(2);
		chapterService.save(child22);

		Chapter child23 = new Chapter();
		child23.setBookId(bookId);
		child23.setParentId(parent2.getId());
		child23.setTeachForm(TeachForm.YAN_CHANG);
		child23.setTitle("妈妈格桑拉");
		child23.setOrderBy(3);
		chapterService.save(child23);
		
		Chapter child24 = new Chapter();
		child24.setBookId(bookId);
		child24.setParentId(parent2.getId());
		child24.setTeachForm(TeachForm.YAN_CHANG);
		child24.setTitle("赶圩归来阿哩哩（节选）");
		child24.setOrderBy(4);
		chapterService.save(child24);
		
		Chapter child25 = new Chapter();
		child25.setBookId(bookId);
		child25.setParentId(parent2.getId());
		child25.setTeachForm(TeachForm.ZHI_SHI_JI_NENG);
		child25.setTitle("短笛");
		child25.setOrderBy(5);
		chapterService.save(child25);

		Chapter child27 = new Chapter();
		child27.setBookId(bookId);
		child27.setParentId(parent2.getId());
		child27.setTeachForm(TeachForm.YAN_ZOU);
		child27.setTitle("学吹竖笛");
		child27.setOrderBy(7);
		chapterService.save(child27);

		Chapter parent3 = new Chapter();
		parent3.setBookId(bookId);
		parent3.setTitle("美丽童话");
		chapterService.save(parent3);

		Chapter child31 = new Chapter();
		child31.setBookId(bookId);
		child31.setParentId(parent3.getId());
		child31.setTeachForm(TeachForm.LING_TING);
		child31.setTitle("魔法师的弟子");
		child31.setOrderBy(1);
		chapterService.save(child31);

		Chapter child32 = new Chapter();
		child32.setBookId(bookId);
		child32.setParentId(parent3.getId());
		child32.setTeachForm(TeachForm.LING_TING);
		child32.setTitle("波斯市场");
		child32.setOrderBy(2);
		chapterService.save(child32);

		Chapter child33 = new Chapter();
		child33.setBookId(bookId);
		child33.setParentId(parent3.getId());
		child33.setTeachForm(TeachForm.YAN_CHANG);
		child33.setTitle("木偶兵进行曲");
		child33.setOrderBy(3);
		chapterService.save(child33);

		Chapter child34 = new Chapter();
		child34.setBookId(bookId);
		child34.setParentId(parent3.getId());
		child34.setTeachForm(TeachForm.YAN_CHANG);
		child34.setTitle("月亮姐姐快下来");
		child34.setOrderBy(4);
		chapterService.save(child34);

		Chapter child35 = new Chapter();
		child35.setBookId(bookId);
		child35.setParentId(parent3.getId());
		child35.setTeachForm(TeachForm.ZHI_SHI_JI_NENG);
		child35.setTitle("发声练习");
		child35.setOrderBy(5);
		chapterService.save(child35);
		
		Chapter child36 = new Chapter();
		child36.setBookId(bookId);
		child36.setParentId(parent3.getId());
		child36.setTeachForm(TeachForm.ZHI_SHI_JI_NENG);
		child36.setTitle("西洋乐器分类（一）");
		child36.setOrderBy(6);
		chapterService.save(child36);
		
		Chapter parent4 = new Chapter();
		parent4.setBookId(bookId);
		parent4.setTitle("京腔京韵");
		chapterService.save(parent4);

		Chapter child41 = new Chapter();
		child41.setBookId(bookId);
		child41.setParentId(parent4.getId());
		child41.setTeachForm(TeachForm.LING_TING);
		child41.setTitle("京剧唱腔联奏");
		child41.setOrderBy(1);
		chapterService.save(child41);

		Chapter child42 = new Chapter();
		child42.setBookId(bookId);
		child42.setParentId(parent4.getId());
		child42.setTeachForm(TeachForm.LING_TING);
		child42.setTitle("宝龙图打坐在开封府");
		child42.setOrderBy(2);
		chapterService.save(child42);

		Chapter child43 = new Chapter();
		child33.setBookId(bookId);
		child43.setParentId(parent4.getId());
		child43.setTeachForm(TeachForm.YAN_CHANG);
		child43.setTitle("你待同志亲如一家");
		child43.setOrderBy(3);
		chapterService.save(child43);

		Chapter child44 = new Chapter();
		child44.setBookId(bookId);
		child44.setParentId(parent4.getId());
		child44.setTeachForm(TeachForm.YAN_CHANG);
		child44.setTitle("校园小戏迷");
		child44.setOrderBy(4);
		chapterService.save(child44);
		
		Chapter child45 = new Chapter();
		child45.setBookId(bookId);
		child45.setParentId(parent4.getId());
		child45.setTeachForm(TeachForm.ZHI_SHI_JI_NENG);
		child45.setTitle("京剧行当");
		child45.setOrderBy(5);
		chapterService.save(child45);
		
		Chapter child46 = new Chapter();
		child46.setBookId(bookId);
		child46.setParentId(parent4.getId());
		child46.setTeachForm(TeachForm.YAN_ZOU);
		child46.setTitle("学吹竖笛");
		child46.setOrderBy(6);
		chapterService.save(child46);
		
		Chapter parent5 = new Chapter();
		parent5.setBookId(bookId);
		parent5.setTitle("赞美的心");
		chapterService.save(parent5);

		Chapter child51 = new Chapter();
		child51.setBookId(bookId);
		child51.setParentId(parent5.getId());
		child51.setTeachForm(TeachForm.LING_TING);
		child51.setTitle("五彩缤纷的大地");
		child51.setOrderBy(1);
		chapterService.save(child51);

		Chapter child52 = new Chapter();
		child52.setBookId(bookId);
		child52.setParentId(parent5.getId());
		child52.setTeachForm(TeachForm.LING_TING);
		child52.setTitle("黄河颂");
		child52.setOrderBy(2);
		chapterService.save(child52);

		Chapter child53 = new Chapter();
		child53.setBookId(bookId);
		child53.setParentId(parent5.getId());
		child53.setTeachForm(TeachForm.YAN_CHANG);
		child53.setTitle("今天是你的生日");
		child53.setOrderBy(3);
		chapterService.save(child53);

		Chapter child54 = new Chapter();
		child54.setBookId(bookId);
		child54.setParentId(parent5.getId());
		child54.setTeachForm(TeachForm.YAN_CHANG);
		child54.setTitle("龙的传人");
		child54.setOrderBy(4);
		chapterService.save(child54);
		
		Chapter child55 = new Chapter();
		child55.setBookId(bookId);
		child55.setParentId(parent5.getId());
		child55.setTeachForm(TeachForm.ZHI_SHI_JI_NENG);
		child55.setTitle("反复记号 D.S");
		child55.setOrderBy(4);
		chapterService.save(child55);

		Chapter parent6 = new Chapter();
		parent6.setBookId(bookId);
		parent6.setTitle("两岸情深");
		chapterService.save(parent6);

		Chapter child61 = new Chapter();
		child61.setBookId(bookId);
		child61.setParentId(parent6.getId());
		child61.setTeachForm(TeachForm.LING_TING);
		child61.setTitle("丢丢铜仔");
		child61.setOrderBy(1);
		chapterService.save(child61);

		Chapter child62 = new Chapter();
		child62.setBookId(bookId);
		child62.setParentId(parent6.getId());
		child62.setTeachForm(TeachForm.LING_TING);
		child62.setTitle("阿里山的姑娘");
		child62.setOrderBy(2);
		chapterService.save(child62);

		Chapter child63 = new Chapter();
		child63.setBookId(bookId);
		child63.setParentId(parent6.getId());
		child63.setTeachForm(TeachForm.YAN_CHANG);
		child63.setTitle("半屏山");
		child63.setOrderBy(3);
		chapterService.save(child63);

		Chapter child64 = new Chapter();
		child64.setBookId(bookId);
		child64.setParentId(parent6.getId());
		child64.setTeachForm(TeachForm.YAN_CHANG);
		child64.setTitle("阿里山的姑娘");
		child64.setOrderBy(4);
		chapterService.save(child64);
		
		Chapter child65 = new Chapter();
		child65.setBookId(bookId);
		child65.setParentId(parent6.getId());
		child65.setTeachForm(TeachForm.ZHI_SHI_JI_NENG);
		child65.setTitle("发声练习");
		child65.setOrderBy(5);
		chapterService.save(child65);
		
		Chapter child66 = new Chapter();
		child66.setBookId(bookId);
		child66.setParentId(parent6.getId());
		child66.setTeachForm(TeachForm.YAN_ZOU);
		child66.setTitle("学吹竖笛");
		child66.setOrderBy(6);
		chapterService.save(child66);
		
		Chapter parent7 = new Chapter();
		parent7.setBookId(bookId);
		parent7.setTitle("七色光彩");
		chapterService.save(parent7);

		Chapter child71 = new Chapter();
		child71.setBookId(bookId);
		child71.setParentId(parent7.getId());
		child71.setTeachForm(TeachForm.LING_TING);
		child71.setTitle("木星--欢乐使者（片段）");
		child71.setOrderBy(1);
		chapterService.save(child71);

		Chapter child72 = new Chapter();
		child72.setBookId(bookId);
		child72.setParentId(parent7.getId());
		child72.setTeachForm(TeachForm.LING_TING);
		child72.setTitle("日出");
		child72.setOrderBy(2);
		chapterService.save(child72);

		Chapter child73 = new Chapter();
		child73.setBookId(bookId);
		child73.setParentId(parent7.getId());
		child73.setTeachForm(TeachForm.YAN_CHANG);
		child73.setTitle("七色光之歌");
		child73.setOrderBy(3);
		chapterService.save(child73);

		Chapter child74 = new Chapter();
		child74.setBookId(bookId);
		child74.setParentId(parent7.getId());
		child74.setTeachForm(TeachForm.YAN_CHANG);
		child74.setTitle("萤火虫");
		child74.setOrderBy(4);
		chapterService.save(child74);

		Chapter child75 = new Chapter();
		child75.setBookId(bookId);
		child75.setParentId(parent7.getId());
		child75.setTeachForm(TeachForm.ZHI_SHI_JI_NENG);
		child75.setTitle("西洋管弦乐队排列图");
		child75.setOrderBy(5);
		chapterService.save(child75);
		
	}


}
