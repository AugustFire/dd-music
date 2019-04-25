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
public class DataIniterS32 {

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
		classRoomService.found("sdgdhedghdkmonbudnoinj", "4028810a62b2b2b20162b2b3dcc70008", VersionDesc.SU_JIAO_BAN, "sdgdhrthepewuteuryhhdgd", false);
	}

	private String initBook() {
		Book book = new Book();
		book.setTitle("义务教育教科书音乐 三年级下册");
		book.setPublishHouse("江苏凤凰少年儿童出版社");
		book.setIntro("义务教育教科书音乐  三年级 下册，江苏凤凰少年儿童出版社出版发行");
		book.setIsbn("ISBN 978-7-5346-7842-4");
		book.setVersion(VersionDesc.SU_JIAO_BAN);
		book.setIsFirstVolume(false);
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
		parent1.setTitle("春天的歌");
		chapterService.save(parent1);

		Chapter child1 = new Chapter();
		child1.setBookId(bookId);
		child1.setParentId(parent1.getId());
		child1.setTeachForm(TeachForm.CHANG_GE);
		child1.setTitle("嘀哩嘀哩  郊外去");
		child1.setOrderBy(1);
		chapterService.save(child1);

		Chapter child2 = new Chapter();
		child2.setBookId(bookId);
		child2.setParentId(parent1.getId());
		child2.setTeachForm(TeachForm.YIN_YUE_ZHI_SHI_R);
		child2.setTitle("春天来了 旅行之歌");
		child2.setOrderBy(2);
		chapterService.save(child2);

		Chapter child3 = new Chapter();
		child3.setBookId(bookId);
		child3.setParentId(parent1.getId());
		child3.setTeachForm(TeachForm.XIN_SHANG);
		child3.setTitle("音乐游戏（异曲同唱）");
		child3.setOrderBy(3);
		chapterService.save(child3);

		Chapter child4 = new Chapter();
		child4.setBookId(bookId);
		child4.setParentId(parent1.getId());
		child4.setTeachForm(TeachForm.CHANG_GE);
		child4.setTitle("集体舞（旅行之歌）");
		child4.setOrderBy(4);
		chapterService.save(child4);

		Chapter child5 = new Chapter();
		child5.setBookId(bookId);
		child5.setParentId(parent1.getId());
		child5.setTeachForm(TeachForm.QING_JIN_JU);
		child5.setTitle("竖笛练习（一）");
		child5.setOrderBy(5);
		chapterService.save(child5);
		
		Chapter parent2 = new Chapter();
		parent2.setBookId(bookId);
		parent2.setTitle("悄悄话");
		chapterService.save(parent2);

		Chapter child21 = new Chapter();
		child21.setBookId(bookId);
		child21.setParentId(parent2.getId());
		child21.setTeachForm(TeachForm.CHANG_GE);
		child21.setTitle("雨天等妈妈 吉祥三宝");
		child21.setOrderBy(1);
		chapterService.save(child21);

		Chapter child22 = new Chapter();
		child22.setBookId(bookId);
		child22.setParentId(parent2.getId());
		child22.setTeachForm(TeachForm.CHANG_GE);
		child22.setTitle("小伞花  共同拥有一个家宝");
		child22.setOrderBy(2);
		chapterService.save(child22);

		Chapter child23 = new Chapter();
		child23.setBookId(bookId);
		child23.setParentId(parent2.getId());
		child23.setTeachForm(TeachForm.YIN_YUE_ZHI_SHI_R);
		child23.setTitle("采集与分享（爱是我们共同的语言）");
		child23.setOrderBy(3);
		chapterService.save(child23);

		Chapter child24 = new Chapter();
		child24.setBookId(bookId);
		child24.setParentId(parent2.getId());
		child24.setTeachForm(TeachForm.XIN_SHANG);
		child24.setTitle("我的编创（吉祥三宝）");
		child24.setOrderBy(4);
		chapterService.save(child24);
		
		Chapter child25 = new Chapter();
		child25.setBookId(bookId);
		child25.setParentId(parent2.getId());
		child25.setTeachForm(TeachForm.XIN_SHANG);
		child25.setTitle("竖笛练习（二）");
		child25.setOrderBy(5);
		chapterService.save(child25);

		Chapter parent3 = new Chapter();
		parent3.setBookId(bookId);
		parent3.setTitle("金孔雀轻轻跳");
		chapterService.save(parent3);

		Chapter child31 = new Chapter();
		child31.setBookId(bookId);
		child31.setParentId(parent3.getId());
		child31.setTeachForm(TeachForm.CHANG_GE);
		child31.setTitle("苗岭的早晨  阿细跳月");
		child31.setOrderBy(1);
		chapterService.save(child31);

		Chapter child32 = new Chapter();
		child32.setBookId(bookId);
		child32.setParentId(parent3.getId());
		child32.setTeachForm(TeachForm.CHANG_GE);
		child32.setTitle("铃铛舞  金孔雀轻轻跳");
		child32.setOrderBy(2);
		chapterService.save(child32);

		Chapter child33 = new Chapter();
		child33.setBookId(bookId);
		child33.setParentId(parent3.getId());
		child33.setTeachForm(TeachForm.YIN_YUE_JIA_GU_SHI_R);
		child33.setTitle("歌表演（金孔雀轻轻跳）");
		child33.setOrderBy(3);
		chapterService.save(child33);

		Chapter child34 = new Chapter();
		child34.setBookId(bookId);
		child34.setParentId(parent3.getId());
		child34.setTeachForm(TeachForm.XIN_SHANG);
		child34.setTitle("竖笛练习（三）");
		child34.setOrderBy(4);
		chapterService.save(child34);

		Chapter parent4 = new Chapter();
		parent4.setBookId(bookId);
		parent4.setTitle("拨动的琴弦");
		chapterService.save(parent4);

		Chapter child41 = new Chapter();
		child41.setBookId(bookId);
		child41.setParentId(parent4.getId());
		child41.setTeachForm(TeachForm.HUO_DONG);
		child41.setTitle("顽皮的小闹钟   幽默曲");
		child41.setOrderBy(1);
		chapterService.save(child41);
		
		Chapter child42 = new Chapter();
		child42.setBookId(bookId);
		child42.setParentId(parent4.getId());
		child42.setTeachForm(TeachForm.HUO_DONG);
		child42.setTitle("杜鹃   芦笛");
		child42.setOrderBy(2);
		chapterService.save(child42);
		
		Chapter child43 = new Chapter();
		child43.setBookId(bookId);
		child43.setParentId(parent4.getId());
		child43.setTeachForm(TeachForm.HUO_DONG);
		child43.setTitle("歌表演（芦笛）");
		child43.setOrderBy(3);
		chapterService.save(child43);
		
		Chapter child44 = new Chapter();
		child44.setBookId(bookId);
		child44.setParentId(parent4.getId());
		child44.setTeachForm(TeachForm.HUO_DONG);
		child44.setTitle("竖笛练习（四）");
		child44.setOrderBy(4);
		chapterService.save(child44);
		
		Chapter parent5 = new Chapter();
		parent5.setBookId(bookId);
		parent5.setTitle("赶花会");
		chapterService.save(parent5);

		Chapter child51 = new Chapter();
		child51.setBookId(bookId);
		child51.setParentId(parent5.getId());
		child51.setTeachForm(TeachForm.CHANG_GE);
		child51.setTitle("赶花会 对花");
		child51.setOrderBy(1);
		chapterService.save(child51);

		Chapter child52 = new Chapter();
		child52.setBookId(bookId);
		child52.setParentId(parent5.getId());
		child52.setTeachForm(TeachForm.CHANG_GE);
		child52.setTitle("对鲜花 编花篮");
		child52.setOrderBy(2);
		chapterService.save(child52);

		Chapter child53 = new Chapter();
		child53.setBookId(bookId);
		child53.setParentId(parent5.getId());
		child53.setTeachForm(TeachForm.XIN_SHANG);
		child53.setTitle("小小音乐剧（赶花会）");
		child53.setOrderBy(3);
		chapterService.save(child53);

		Chapter child54 = new Chapter();
		child54.setBookId(bookId);
		child54.setParentId(parent5.getId());
		child54.setTeachForm(TeachForm.HUO_DONG);
		child54.setTitle("我的编创（对鲜花）");
		child54.setOrderBy(4);
		chapterService.save(child54);
		
		Chapter child55 = new Chapter();
		child55.setBookId(bookId);
		child55.setParentId(parent5.getId());
		child55.setTeachForm(TeachForm.XIN_SHANG);
		child55.setTitle("歌表演（编花篮）");
		child55.setOrderBy(5);
		chapterService.save(child55);
		
		Chapter child56 = new Chapter();
		child56.setBookId(bookId);
		child56.setParentId(parent5.getId());
		child56.setTeachForm(TeachForm.XUE_CHANG_JING_JU);
		child56.setTitle("竖笛练习（五）");
		child56.setOrderBy(6);
		chapterService.save(child56);

		Chapter parent6 = new Chapter();
		parent6.setBookId(bookId);
		parent6.setTitle("我们的村庄");
		chapterService.save(parent6);

		Chapter child61 = new Chapter();
		child61.setBookId(bookId);
		child61.setParentId(parent6.getId());
		child61.setTeachForm(TeachForm.CHANG_GE);
		child61.setTitle("全都认识我  快乐的农夫");
		child61.setOrderBy(1);
		chapterService.save(child61);

		Chapter child62 = new Chapter();
		child62.setBookId(bookId);
		child62.setParentId(parent6.getId());
		child62.setTeachForm(TeachForm.YIN_YUE_ZHI_SHI_R);
		child62.setTitle("如今家乡山连山 八只小鹅");
		child62.setOrderBy(2);
		chapterService.save(child62);

		Chapter child63 = new Chapter();
		child63.setBookId(bookId);
		child63.setParentId(parent6.getId());
		child63.setTeachForm(TeachForm.CHANG_GE);
		child63.setTitle("我的编创1（八只小鹅）");
		child63.setOrderBy(3);
		chapterService.save(child63);

		Chapter child64 = new Chapter();
		child64.setBookId(bookId);
		child64.setParentId(parent6.getId());
		child64.setTeachForm(TeachForm.YIN_YUE_JIA_GU_SHI_R);
		child64.setTitle("我的编创2（节奏故事）");
		child64.setOrderBy(4);
		chapterService.save(child64);

		Chapter child65 = new Chapter();
		child65.setBookId(bookId);
		child65.setParentId(parent6.getId());
		child65.setTeachForm(TeachForm.XIN_SHANG);
		child65.setTitle("竖笛练习（六）");
		child65.setOrderBy(5);
		chapterService.save(child65);
		
		Chapter parent7 = new Chapter();
		parent6.setBookId(bookId);
		parent6.setTitle("开心里个来");
		chapterService.save(parent6);

		Chapter child71 = new Chapter();
		child71.setBookId(bookId);
		child71.setParentId(parent7.getId());
		child71.setTeachForm(TeachForm.CHANG_GE);
		child71.setTitle("剪彩波尔卡  打字机之歌");
		child71.setOrderBy(1);
		chapterService.save(child71);

		Chapter child72 = new Chapter();
		child72.setBookId(bookId);
		child72.setParentId(parent7.getId());
		child72.setTeachForm(TeachForm.YIN_YUE_ZHI_SHI_R);
		child72.setTitle("恰利利恰利  开心里个来");
		child72.setOrderBy(2);
		chapterService.save(child72);

		Chapter child73 = new Chapter();
		child73.setBookId(bookId);
		child73.setParentId(parent7.getId());
		child73.setTeachForm(TeachForm.CHANG_GE);
		child73.setTitle("综合表演（火车来了）");
		child73.setOrderBy(3);
		chapterService.save(child73);

		Chapter child74 = new Chapter();
		child74.setBookId(bookId);
		child74.setParentId(parent7.getId());
		child74.setTeachForm(TeachForm.YIN_YUE_JIA_GU_SHI_R);
		child74.setTitle("竖笛练习（七）");
		child74.setOrderBy(4);
		chapterService.save(child74);
		
		Chapter parent8 = new Chapter();
		parent8.setBookId(bookId);
		parent8.setTitle("星星点灯");
		chapterService.save(parent8);

		Chapter child81 = new Chapter();
		child81.setBookId(bookId);
		child81.setParentId(parent8.getId());
		child81.setTeachForm(TeachForm.CHANG_GE);
		child81.setTitle("小星星变奏曲（选段）  愉快的梦");
		child81.setOrderBy(1);
		chapterService.save(child81);

		Chapter child82 = new Chapter();
		child82.setBookId(bookId);
		child82.setParentId(parent8.getId());
		child82.setTeachForm(TeachForm.YIN_YUE_ZHI_SHI_R);
		child82.setTitle("夏夜  美丽的黄昏");
		child82.setOrderBy(2);
		chapterService.save(child82);

		Chapter child83 = new Chapter();
		child83.setBookId(bookId);
		child83.setParentId(parent8.getId());
		child83.setTeachForm(TeachForm.CHANG_GE);
		child83.setTitle("我的编创1（小星星变奏曲）");
		child83.setOrderBy(3);
		chapterService.save(child83);

		Chapter child84 = new Chapter();
		child84.setBookId(bookId);
		child84.setParentId(parent8.getId());
		child84.setTeachForm(TeachForm.YIN_YUE_JIA_GU_SHI_R);
		child84.setTitle("综合表演（夏夜）");
		child84.setOrderBy(4);
		chapterService.save(child84);
		
		Chapter child85 = new Chapter();
		child85.setBookId(bookId);
		child85.setParentId(parent8.getId());
		child85.setTeachForm(TeachForm.YIN_YUE_JIA_GU_SHI_R);
		child85.setTitle("我的编创2（梦之歌）");
		child85.setOrderBy(5);
		chapterService.save(child85);
		
		Chapter child86 = new Chapter();
		child86.setBookId(bookId);
		child86.setParentId(parent8.getId());
		child86.setTeachForm(TeachForm.YIN_YUE_JIA_GU_SHI_R);
		child86.setTitle("竖笛练习（八）");
		child86.setOrderBy(5);
		chapterService.save(child86);

	}


}
