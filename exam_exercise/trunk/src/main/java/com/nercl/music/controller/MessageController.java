package com.nercl.music.controller;

import java.util.Map;

import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface MessageController {

	String NOTICE = "message";

	String WARNING = "warning";

	String ERROR = "error";

	String EXAMINEE = "examinee";

	default void sendNotice(RedirectAttributes redirectAttr, Map<String, Object> map) {
		redirectAttr.addFlashAttribute(EXAMINEE, map);
	}

	default void sendNotice(MessageSource messageSource, RedirectAttributes redirectAttr, String messageKey,
	        Object... parameter) {
		String message = messageSource.getMessage(messageKey, parameter, null);
		redirectAttr.addFlashAttribute(NOTICE, message);
	}

	default void sendNotice(MessageSource messageSource, Model model, String messageKey, Object... parameter) {
		String message = messageSource.getMessage(messageKey, parameter, null);
		model.addAttribute(NOTICE, message);
	}

	default void sendWarning(MessageSource messageSource, RedirectAttributes redirectAttrs, String messageKey) {
		String message = messageSource.getMessage(messageKey, null, null);
		redirectAttrs.addFlashAttribute(WARNING, message);
	}

	default void sendWarning(MessageSource messageSource, RedirectAttributes redirectAttrs, String messageKey,
	        Object... parameter) {
		String message = messageSource.getMessage(messageKey, parameter, null);
		redirectAttrs.addFlashAttribute(WARNING, message);
	}

	default void sendError(MessageSource messageSource, RedirectAttributes redirectAttrs, String messageKey,
	        Object... parameter) {
		String message = messageSource.getMessage(messageKey, parameter, null);
		redirectAttrs.addFlashAttribute(ERROR, message);
	}

	default void sendError(MessageSource messageSource, Model model, String messageKey, Object... parameter) {
		String message = messageSource.getMessage(messageKey, parameter, null);
		model.addAttribute(ERROR, message);
	}
}
