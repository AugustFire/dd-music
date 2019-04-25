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
public class DataIniterS31 {

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
		classRoomService.found("sdgdhedghdkmonbudnoinj", "4028810a62b2b2b20162b2b3dcc70008", VersionDesc.SU_JIAO_BAN, "sdgdhrthepewuteuryhhdgd", true);
	}

	private String initBook() {
		Book book = new Book();
		book.setTitle("义务教育教科书音乐 三年级上册");
		book.setPublishHouse("江苏凤凰少年儿童出版社");
		book.setIntro("义务教育教科书音乐  三年级 上册，江苏凤凰少年儿童出版社出版发行");
		book.setIsbn("ISBN 978-7-5364-7843-1");
		book.setVersion(VersionDesc.SU_JIAO_BAN);
		book.setIsFirstVolume(true);
		Grade grade = gradeService.findByCode("002-3");
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
		parent1.setTitle("我的朋友Do Re Mi 1");
		chapterService.save(parent1);

		Chapter child1 = new Chapter();
		child1.setBookId(bookId);
		child1.setParentId(parent1.getId());
		child1.setTeachForm(TeachForm.CHANG_GE);
		child1.setTitle("Do Re Mi G大调小步舞曲");
		child1.setOrderBy(1);
		chapterService.save(child1);

		Chapter child2 = new Chapter();
		child2.setBookId(bookId);
		child2.setParentId(parent1.getId());
		child2.setTeachForm(TeachForm.XIN_SHANG);
		child2.setTitle("七个小兄弟 音乐是好朋友");
		child2.setOrderBy(2);
		chapterService.save(child2);

		Chapter child3 = new Chapter();
		child3.setBookId(bookId);
		child3.setParentId(parent1.getId());
		child3.setTeachForm(TeachForm.CHANG_GE);
		child3.setTitle("音乐游戏（小音符找朋友）");
		child3.setOrderBy(3);
		chapterService.save(child3);

		Chapter child4 = new Chapter();
		child4.setBookId(bookId);
		child4.setParentId(parent1.getId());
		child4.setTeachForm(TeachForm.YIN_YUE_ZHI_SHI_R);
		child4.setTitle("竖笛练习（一）");
		child4.setOrderBy(4);
		chapterService.save(child4);

		Chapter parent2 = new Chapter();
		parent2.setBookId(bookId);
		parent2.setTitle("快乐恰恰恰");
		chapterService.save(parent2);

		Chapter child21 = new Chapter();
		child21.setBookId(bookId);
		child21.setParentId(parent2.getId());
		child21.setTeachForm(TeachForm.CHANG_GE);
		child21.setTitle("微笑波尔卡 希腊舞曲");
		child21.setOrderBy(1);
		chapterService.save(child21);

		Chapter child22 = new Chapter();
		child22.setBookId(bookId);
		child22.setParentId(parent2.getId());
		child22.setTeachForm(TeachForm.YIN_YUE_ZHI_SHI_R);
		child22.setTitle("木瓜恰恰恰 阿西里西");
		child22.setOrderBy(2);
		chapterService.save(child22);

		Chapter child23 = new Chapter();
		child23.setBookId(bookId);
		child23.setParentId(parent2.getId());
		child23.setTeachForm(TeachForm.CHANG_GE);
		child23.setTitle("律动（希腊舞曲）");
		child23.setOrderBy(3);
		chapterService.save(child23);

		Chapter child24 = new Chapter();
		child24.setBookId(bookId);
		child24.setParentId(parent2.getId());
		child24.setTeachForm(TeachForm.YIN_YUE_ZHI_SHI_R);
		child24.setTitle("竖笛练习（二）");
		child24.setOrderBy(4);
		chapterService.save(child24);
		
		Chapter parent3 = new Chapter();
		parent3.setBookId(bookId);
		parent3.setTitle("百灵鸟的歌");
		chapterService.save(parent3);

		Chapter child31 = new Chapter();
		child31.setBookId(bookId);
		child31.setParentId(parent3.getId());
		child31.setTeachForm(TeachForm.CHANG_GE);
		child31.setTitle("对鸟 大鸟笼");
		child31.setOrderBy(1);
		chapterService.save(child31);

		Chapter child32 = new Chapter();
		child32.setBookId(bookId);
		child32.setParentId(parent3.getId());
		child32.setTeachForm(TeachForm.CHANG_GE);
		child32.setTitle("顽皮的小杜鹃 白鸽");
		child32.setOrderBy(2);
		chapterService.save(child32);

		Chapter child33 = new Chapter();
		child33.setBookId(bookId);
		child33.setParentId(parent3.getId());
		child33.setTeachForm(TeachForm.YIN_YUE_ZHI_SHI_R);
		child33.setTitle("叫我唱歌我唱歌");
		child33.setOrderBy(3);
		chapterService.save(child33);

		Chapter child34 = new Chapter();
		child34.setBookId(bookId);
		child34.setParentId(parent3.getId());
		child34.setTeachForm(TeachForm.YIN_YUE_ZHI_SHI_R);
		child34.setTitle("歌表演");
		child34.setOrderBy(4);
		chapterService.save(child34);

		Chapter child35 = new Chapter();
		child35.setBookId(bookId);
		child35.setParentId(parent3.getId());
		child35.setTeachForm(TeachForm.XIN_SHANG);
		child35.setTitle("竖笛练习（三）");
		child35.setOrderBy(5);
		chapterService.save(child35);
		
		Chapter parent4 = new Chapter();
		parent4.setBookId(bookId);
		parent4.setTitle("采山谣");
		chapterService.save(parent4);

		Chapter child41 = new Chapter();
		child41.setBookId(bookId);
		child41.setParentId(parent4.getId());
		child41.setTeachForm(TeachForm.CHANG_GE);
		child41.setTitle("八月桂花遍地开 打枣");
		child41.setOrderBy(1);
		chapterService.save(child41);

		Chapter child42 = new Chapter();
		child42.setBookId(bookId);
		child42.setParentId(parent4.getId());
		child42.setTeachForm(TeachForm.YIN_YUE_ZHI_SHI_R);
		child42.setTitle("树叶儿飘飘 溜溜山歌");
		child42.setOrderBy(2);
		chapterService.save(child42);

		Chapter child43 = new Chapter();
		child33.setBookId(bookId);
		child43.setParentId(parent4.getId());
		child43.setTeachForm(TeachForm.HUO_DONG);
		child43.setTitle("综合表演（夸秋天）");
		child43.setOrderBy(3);
		chapterService.save(child43);

		Chapter child44 = new Chapter();
		child44.setBookId(bookId);
		child44.setParentId(parent4.getId());
		child44.setTeachForm(TeachForm.CHANG_GE);
		child44.setTitle("我的编创（溜溜山歌）");
		child44.setOrderBy(4);
		chapterService.save(child44);
		
		Chapter child45 = new Chapter();
		child45.setBookId(bookId);
		child45.setParentId(parent4.getId());
		child45.setTeachForm(TeachForm.XIN_SHANG);
		child45.setTitle("竖笛练习（四）");
		child45.setOrderBy(5);
		chapterService.save(child45);
		
		Chapter parent5 = new Chapter();
		parent5.setBookId(bookId);
		parent5.setTitle("诗韵悠悠");
		chapterService.save(parent5);

		Chapter child51 = new Chapter();
		child51.setBookId(bookId);
		child51.setParentId(parent5.getId());
		child51.setTeachForm(TeachForm.HUO_DONG);
		child51.setTitle("读唐诗 小儿垂钓");
		child51.setOrderBy(1);
		chapterService.save(child51);

		Chapter child52 = new Chapter();
		child52.setBookId(bookId);
		child52.setParentId(parent5.getId());
		child52.setTeachForm(TeachForm.JI_TI_WU_R);
		child52.setTitle("游子吟 小儿垂钓");
		child52.setOrderBy(2);
		chapterService.save(child52);

		Chapter child53 = new Chapter();
		child53.setBookId(bookId);
		child53.setParentId(parent5.getId());
		child53.setTeachForm(TeachForm.XIN_SHANG);
		child53.setTitle("歌表演（游子吟）");
		child53.setOrderBy(3);
		chapterService.save(child53);

		Chapter child54 = new Chapter();
		child54.setBookId(bookId);
		child54.setParentId(parent5.getId());
		child54.setTeachForm(TeachForm.XIN_SHANG);
		child54.setTitle("我的编创（“画”唐诗）");
		child54.setOrderBy(4);
		chapterService.save(child54);
		
		Chapter child55 = new Chapter();
		child55.setBookId(bookId);
		child55.setParentId(parent5.getId());
		child55.setTeachForm(TeachForm.XIN_SHANG);
		child55.setTitle("露一手（诵读《长歌行》）");
		child55.setOrderBy(5);
		chapterService.save(child55);
		
		Chapter child56 = new Chapter();
		child56.setBookId(bookId);
		child56.setParentId(parent5.getId());
		child56.setTeachForm(TeachForm.XIN_SHANG);
		child56.setTitle("竖笛练习（五）");
		child56.setOrderBy(6);
		chapterService.save(child56);

		Chapter parent6 = new Chapter();
		parent6.setBookId(bookId);
		parent6.setTitle("牧笛声声");
		chapterService.save(parent6);

		Chapter child61 = new Chapter();
		child61.setBookId(bookId);
		child61.setParentId(parent6.getId());
		child61.setTeachForm(TeachForm.CHANG_GE);
		child61.setTitle("牧童短笛 听一阵阵歌声");
		child61.setOrderBy(1);
		chapterService.save(child61);

		Chapter child62 = new Chapter();
		child62.setBookId(bookId);
		child62.setParentId(parent6.getId());
		child62.setTeachForm(TeachForm.YIN_YUE_ZHI_SHI_R);
		child62.setTitle("牧童之歌 牧羊女");
		child62.setOrderBy(2);
		chapterService.save(child62);

		Chapter child63 = new Chapter();
		child63.setBookId(bookId);
		child63.setParentId(parent6.getId());
		child63.setTeachForm(TeachForm.XIN_SHANG);
		child63.setTitle("综合表演（牧童之歌）");
		child63.setOrderBy(3);
		chapterService.save(child63);

		Chapter child64 = new Chapter();
		child64.setBookId(bookId);
		child64.setParentId(parent6.getId());
		child64.setTeachForm(TeachForm.CHANG_GE);
		child64.setTitle("竖笛练习（六）");
		child64.setOrderBy(4);
		chapterService.save(child64);
		
		Chapter parent7 = new Chapter();
		parent7.setBookId(bookId);
		parent7.setTitle("爷爷故事多");
		chapterService.save(parent7);

		Chapter child71 = new Chapter();
		child71.setBookId(bookId);
		child71.setParentId(parent7.getId());
		child71.setTeachForm(TeachForm.CHANG_GE);
		child71.setTitle("映山红 司马光砸缸");
		child71.setOrderBy(1);
		chapterService.save(child71);

		Chapter child72 = new Chapter();
		child72.setBookId(bookId);
		child72.setParentId(parent7.getId());
		child72.setTeachForm(TeachForm.YIN_YUE_ZHI_SHI_R);
		child72.setTitle("爷爷为我打月饼 儿童团放哨歌");
		child72.setOrderBy(2);
		chapterService.save(child72);

		Chapter child73 = new Chapter();
		child73.setBookId(bookId);
		child73.setParentId(parent7.getId());
		child73.setTeachForm(TeachForm.XIN_SHANG);
		child73.setTitle("小小音乐剧（ 司马光砸缸）");
		child73.setOrderBy(3);
		chapterService.save(child73);

		Chapter child74 = new Chapter();
		child74.setBookId(bookId);
		child74.setParentId(parent7.getId());
		child74.setTeachForm(TeachForm.CHANG_GE);
		child74.setTitle("竖笛练习（七）");
		child74.setOrderBy(4);
		chapterService.save(child74);
		
		Chapter parent8 = new Chapter();
		parent8.setBookId(bookId);
		parent8.setTitle("快乐十分钟");
		chapterService.save(parent8);

		Chapter child81 = new Chapter();
		child81.setBookId(bookId);
		child81.setParentId(parent8.getId());
		child81.setTeachForm(TeachForm.CHANG_GE);
		child81.setTitle("哦，十分钟 放轻松");
		child81.setOrderBy(1);
		chapterService.save(child81);

		Chapter child82 = new Chapter();
		child82.setBookId(bookId);
		child82.setParentId(parent8.getId());
		child82.setTeachForm(TeachForm.YIN_YUE_ZHI_SHI_R);
		child82.setTitle("快乐的孩子爱唱歌 跳到我这里来");
		child82.setOrderBy(2);
		chapterService.save(child82);

		Chapter child83 = new Chapter();
		child83.setBookId(bookId);
		child83.setParentId(parent8.getId());
		child83.setTeachForm(TeachForm.XIN_SHANG);
		child83.setTitle("集体舞（跳到我这来）");
		child83.setOrderBy(3);
		chapterService.save(child83);
		
		Chapter child84 = new Chapter();
		child84.setBookId(bookId);
		child84.setParentId(parent8.getId());
		child84.setTeachForm(TeachForm.CHANG_GE);
		child84.setTitle("音乐游戏（节奏九宫格）");
		child84.setOrderBy(4);
		chapterService.save(child84);

		Chapter child85 = new Chapter();
		child85.setBookId(bookId);
		child85.setParentId(parent8.getId());
		child85.setTeachForm(TeachForm.CHANG_GE);
		child85.setTitle("竖笛练习（八）");
		child85.setOrderBy(5);
		chapterService.save(child85);
		
	}


}
