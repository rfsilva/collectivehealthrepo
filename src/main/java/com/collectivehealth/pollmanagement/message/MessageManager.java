package com.collectivehealth.pollmanagement.message;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import lombok.extern.log4j.Log4j2;

@Log4j2
public final class MessageManager {

	private static final ResourceBundle resourceMessage = ResourceBundle
			.getBundle("messages");

	private MessageManager() {
	}

	public static String getMessage(String key, Object... args) {
		String text;

		try {
			text = resourceMessage.getString(key);
		} catch (MissingResourceException ex) {
			log.info("Message not found", ex);
			text = key;
		}

		if ((args != null) && (args.length > 0)) {
			text = MessageFormat.format(text, args);
		}

		return text;
	}

	public static String getMessage(String key) {
		String text;

		try {
			text = resourceMessage.getString(key);
		} catch (MissingResourceException ex) {
			log.info("Message not found", ex);
			text = key;
		}

		return text;
	}

}
