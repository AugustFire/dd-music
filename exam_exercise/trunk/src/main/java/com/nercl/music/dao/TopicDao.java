package com.nercl.music.dao;

import java.util.List;

import com.nercl.music.entity.authorize.Topic;

public interface TopicDao extends BaseDao<Topic, String> {

	List<Topic> list();

	List<Topic> list(int page);

	List<Topic> list(Integer subjectType);

	List<Topic> query(String key);

	Topic getNewest(Integer year);

	List<Topic> getByYear(Integer year);

	List<Topic> getByTitle(Integer subjectType, String title, int page);

	List<Topic> getPayed(String exerciserId);

}
