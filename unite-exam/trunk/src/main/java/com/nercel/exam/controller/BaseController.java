package com.nercel.exam.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.ui.Model;

public interface BaseController {

	default <T> CompletableFuture<T> execute(Supplier<T> supplier) {
		return CompletableFuture.supplyAsync(supplier);
	}

	default <T extends Object> CompletableFuture<List<T>> sequence(List<CompletableFuture<T>> futures) {
		CompletableFuture<Void> allDoneFuture = CompletableFuture
		        .allOf(futures.toArray(new CompletableFuture[futures.size()]));
		return allDoneFuture
		        .thenApply(v -> futures.stream().map(future -> future.join()).collect(Collectors.<T>toList()));
	}

	default List<Object> list(Object... objs) {
		return Arrays.asList(objs);
	}

	default Map<Object, Object> map(Object k, Object v) {
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put(k, v);
		return map;
	}

	default <T> void setValueToModel(Model model, String k0, T v0) {
		model.addAttribute(k0, v0);
	}

	default <T> void setValueToModel(Model model, String k0, T v0, String k1, T v1) {
		setValueToModel(model, k0, v0);
		model.addAttribute(k1, v1);
	}

	default <T> void setValueToModel(Model model, String k0, T v0, String k1, T v1, String k2, T v2) {
		setValueToModel(model, k0, v0, k1, v1);
		model.addAttribute(k2, v2);
	}
}
