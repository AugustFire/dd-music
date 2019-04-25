package com.nercl.music.cloud.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class QuartzService {

	// 每分钟启动
	@Scheduled(cron = "0 0/1 * * * ?")
	public void timerToNow() {
		System.out.println("now time:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	}
}
