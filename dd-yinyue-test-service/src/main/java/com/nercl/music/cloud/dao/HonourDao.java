package com.nercl.music.cloud.dao;

import com.nercl.music.cloud.entity.ActivityType;
import com.nercl.music.cloud.entity.AwardLevel;
import com.nercl.music.cloud.entity.Honour;
import com.nercl.music.util.page.PaginateSupportArray;

public interface HonourDao extends BaseDao<Honour, String> {

	PaginateSupportArray<Honour> findAllHonours(String sid,ActivityType activityType, AwardLevel awardLevel,int page, int pageSize);


}
