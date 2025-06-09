package com.springlab.service;

import java.util.Map;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.springlab.auth.PrincipalDetail;
import com.springlab.domain.User;
import com.springlab.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
	
	private final UserRepository userRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

		OAuth2User oauth2User = super.loadUser(userRequest);
		Map<String,Object> attributes = oauth2User.getAttributes();
		
		String email = (String) attributes.get("email");
		String name = (String) attributes.get("name");
		
		User userEntity = userRepository.findByEmail(email);
		if (userEntity == null) {
			userEntity = User.builder()
					.username(name)
					.email(email)
					.password("")
					.authority("ROLE_USER")
					.build();
			
			userRepository.save(userEntity);
		}
		
		return new PrincipalDetail(userEntity, attributes);
	}
}
