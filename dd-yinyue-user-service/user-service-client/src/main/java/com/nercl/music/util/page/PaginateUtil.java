package com.nercl.music.util.page;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

public class PaginateUtil {

	private String paginationNameInParams;

	private static PaginateUtil instance = new PaginateUtil();

	public static String translateToFrench(String str) {
		return str;
	}

	public static PaginateUtil getInstance() {
		return instance;
	}

	private PaginateUtil() {
	}

	// public String getPaginatePath(HttpServletRequest request) {
	// String pathInfo = (String)
	// request.getAttribute("javax.servlet.forward.path_info");
	// String url = (String)
	// request.getAttribute("javax.servlet.forward.servlet_path");
	// if (!Strings.isNullOrEmpty(pathInfo)) {
	// url = url + pathInfo;
	// }
	// String queryString = (String)
	// request.getAttribute("javax.servlet.forward.query_string");
	// if (!Strings.isNullOrEmpty(queryString)) {
	// url = url + extractQueryStringWithoutPagination(queryString, request);
	// }
	// return url;
	// }

	public String getPaginatePath(HttpServletRequest request) {
		String url = request.getServletPath();
		String queryString = request.getQueryString();
		if (!Strings.isNullOrEmpty(queryString)) {
			url = url + extractQueryStringWithoutPagination(queryString, request);
		}
		return url;
	}

	private String extractQueryStringWithoutPagination(String queryString, HttpServletRequest request) {
		StringBuilder urlBuilder = new StringBuilder();
		if (!Strings.isNullOrEmpty(queryString)) {
			urlBuilder.append("?");
			Iterator<String> params = Splitter.on("&").omitEmptyStrings().split(queryString).iterator();
			while (params.hasNext()) {
				String q = (String) params.next();
				List<String> kv = Lists.newArrayList(Splitter.on("=").omitEmptyStrings().split(q));
				String name = kv.isEmpty() ? null : (String) kv.get(0);
				if ((name != null) && (!getPaginationNameInParams().equals(name))) {
					urlBuilder.append(name).append("=").append(request.getParameter(name)).append("&");
				}
			}
		}
		String result = urlBuilder.toString();
		result = result.endsWith("&") ? result.substring(0, result.length() - 1) : result;
		return "?".endsWith(result) ? "" : result;
	}

	public void setPaginationNameInParams(String paginationNameInParams) {
		this.paginationNameInParams = paginationNameInParams;
	}

	public String getPaginationNameInParams() {
		return Strings.isNullOrEmpty(this.paginationNameInParams) ? "page" : this.paginationNameInParams;
	}

}
