package com.springlab.dto;

import com.springlab.domain.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountForm {

	private String username;
	private String password;

	public AccountForm() {
	}

	public AccountForm(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public User toEntity() {
		return new User(username, password);
	}
}
