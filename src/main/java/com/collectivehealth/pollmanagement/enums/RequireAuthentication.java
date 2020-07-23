package com.collectivehealth.pollmanagement.enums;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RequireAuthentication {
	NO(0), YES(1);
	
	private int value;
	
	@JsonCreator
	public static RequireAuthentication of(int value) {
		return Arrays.stream(RequireAuthentication.values())
				.filter(tc -> value == tc.getValue())
				.findFirst().orElseThrow(IllegalArgumentException :: new);
	}
}


