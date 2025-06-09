package com.springlab.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.springlab.auth.PrincipalDetail;
import com.springlab.domain.User;
import com.springlab.repository.UserRepository;

@Service
public class PrincipalDetailService implements UserDetailsService {

	private final UserRepository userRepository;

	public PrincipalDetailService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));
		return new PrincipalDetail(user);
	}
}
