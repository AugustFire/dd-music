package com.nercl.music.util.page;

import java.util.List;

public class PaginateSupportUtil {

	public static <T> PaginateSupportArray<T> pagingList(List<T> list, int page, int pageSize, int total) {
		return new PaginateSupportArray<T>(list, page, pageSize, total);
	}
}
