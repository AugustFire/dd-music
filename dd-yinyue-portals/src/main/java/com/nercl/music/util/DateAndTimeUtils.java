package com.nercl.music.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

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
	
	
	public static void main(String[] args) {
		
		System.out.println(startOfThisMonth().toString());
		System.out.println(startOfThisMonth().toEpochSecond(ZoneOffset.MIN));
		System.out.println(startOfThisMonth().toEpochSecond(ZoneOffset.MAX));
		System.out.println(startOfThisMonth().toEpochSecond(ZoneOffset.UTC));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String format = startOfThisMonth().format(formatter);
		System.out.println(startOfThisMonth().format(formatter ));
		System.out.println(Instant.now().toEpochMilli());
		
//		System.out.println(endOfThisMonth());
	}
}
