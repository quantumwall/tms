package org.quantum.dto;

import org.quantum.entity.User;

public record ReadUserDto(Long id, String email, String name) {

	public static ReadUserDto from(User user) {
		return new ReadUserDto(user.getId(), user.getEmail(), user.getName());
	}
}
