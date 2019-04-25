package com.nercl.music.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

import org.springframework.util.Assert;

public class DateAndTimeUtils {

	/**
	 * 取本月第一天
	 */
	public static LocalDate firstDayOfThisMonth() {
		LocalDate today = LocalDate.now();
		return today.with(TemporalAdjusters.firstDayOfMonth());
	}

	/**
	 * 取本月第N天
	 */
	public static LocalDate dayOfThisMonth(int n) {
		LocalDate today = LocalDate.now();
		return today.withDayOfMonth(n);
	}

	/**
	 * 取本月最后一天
	 */
	public static LocalDate lastDayOfThisMonth() {
		LocalDate today = LocalDate.now();
		return today.with(TemporalAdjusters.lastDayOfMonth());
	}
	
	/**
	 * 取本月第一天的开始时间
	 */
	public static LocalDateTime startOfThisMonth() {
		return LocalDateTime.of(firstDayOfThisMonth(), LocalTime.MIN);
	}


	/**
	 * 取本月最后一天的结束时间
	 */
	public static LocalDateTime endOfThisMonth() {
		return LocalDateTime.of(lastDayOfThisMonth(), LocalTime.MAX);
	}
	
	/**
	 * 将字符串转日期成Long类型的时间戳，格式为：yyyy-MM-dd HH:mm:ss
	 */
	public static Long convertTimeToLong(String time) { 
		Assert.notNull(time, "time is null");
		DateTimeFormatter ftf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime parse = LocalDateTime.parse("2018-05-29 13:52:50", ftf);
		return LocalDateTime.from(parse).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}  
	
	/**
	 * 将Long类型的时间戳转换成String 类型的时间格式，时间格式为：yyyy-MM-dd HH:mm:ss
	 */
	public static String convertTimeToString(Long time){
		Assert.notNull(time, "time is null");
		DateTimeFormatter ftf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return ftf.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(time),ZoneId.systemDefault()));
	}
	
	public static void main(String[] args) {
		Long convertTimeToLong = convertTimeToLong("2018-05-29 13:52:50");
		System.out.println(convertTimeToLong);
		System.out.println(convertTimeToString(convertTimeToLong));
	}

}
