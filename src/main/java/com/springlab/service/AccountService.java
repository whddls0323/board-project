package com.springlab.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.springlab.domain.User;
import com.springlab.repository.UserRepository;

@Service
public class AccountService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public boolean verification(User user) {
		Optional<User> existingAccount = userRepository.findByUsername(user.getUsername());
		String foundUsername = null;

		if (existingAccount.isPresent()) {
			foundUsername = existingAccount.get().getUsername();
			System.out.println("DB에 있는 아이디: " + foundUsername);
		}

		if (foundUsername != null)
			return true;

		else
			return false;

	}

	public void insert(User user) {
		String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		userRepository.save(user);
	}
}
