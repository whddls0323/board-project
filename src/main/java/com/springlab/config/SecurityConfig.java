package com.springlab.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

import com.springlab.handler.LoginFailureHandler;
import com.springlab.handler.LoginSuccessHandler;
import com.springlab.handler.OAuth2LoginSuccessHandler;
import com.springlab.service.PrincipalDetailService;
import com.springlab.service.PrincipalOauth2UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig { 

	@Autowired
	private LoginSuccessHandler loginSuccessHandler;
	@Autowired
	private LoginFailureHandler loginFailureHandler;
	@Autowired
	private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
	@Autowired
	private PrincipalDetailService principalDetailService;
	@Autowired
	private PrincipalOauth2UserService principalOauth2UserService; 
	
	@Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web
        		.ignoring()
                .requestMatchers("/img/**","/favicon.ico");
    }
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.userDetailsService(principalDetailService)
			.authorizeRequests(authorizeRequests ->
				authorizeRequests
						.requestMatchers("/","/login","/WEB-INF/views/**").permitAll()
						.anyRequest().permitAll()
			)
			.formLogin(formLogin -> //이것만 쓰면 spring에서 제공하는 security 화면 사용
					formLogin
						.loginPage("/login")
						.successHandler(loginSuccessHandler)
						.failureHandler(loginFailureHandler)
						.permitAll()	
			)
			.oauth2Login(oauth2Login ->  
				oauth2Login
					.loginPage("/login")
					.userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
							.userService(principalOauth2UserService))
					.successHandler(oAuth2LoginSuccessHandler))
			.logout(logout -> logout
	            .logoutUrl("/logout")
	            .logoutSuccessUrl("/")
	            .invalidateHttpSession(true)
	            .deleteCookies("JSESSIONID")
	    )
	    .csrf().disable();
						
		return http.build();
	}
	
	@Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
