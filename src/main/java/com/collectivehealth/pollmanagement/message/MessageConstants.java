package com.collectivehealth.pollmanagement.message;

public final class MessageConstants {

	private MessageConstants() {
	}
    
    public static final String INVALID_CREDENTIALS = "invalid-credentials";
    public static final String USERNAME_ALREADY_EXISTS = "username-already-exists";
    public static final String POLL_TITLE_ALREADY_EXISTS = "poll-with-title-already-exists";
    public static final String INVALID_USER = "invalid-user";
    public static final String INVALID_POLL = "invalid-poll";
    public static final String INVALID_POLL_OPTION = "invalid-poll-option";
    public static final String OPTIONS_DIFFERENT_POLL = "options-different-poll";
    public static final String USER_ALREADY_ASSOCIATED_TO_POLL = "user-already-associated-to-poll";
    public static final String INVALID_POLL_USER_ASSOCIATION = "invalid-poll-user-association";
}
