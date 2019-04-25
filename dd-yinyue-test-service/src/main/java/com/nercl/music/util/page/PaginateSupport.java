package com.nercl.music.util.page;

public abstract interface PaginateSupport {

	public abstract int getTotal();

	public abstract int getPageSize();

	public abstract int getPage();
}
