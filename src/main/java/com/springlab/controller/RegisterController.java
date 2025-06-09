package com.springlab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springlab.domain.User;
import com.springlab.dto.AccountForm;
import com.springlab.service.AccountService;

@Controller
public class RegisterController { // 회원가입

	@Autowired
	private AccountService accountService;

	@GetMapping("/register")
	public String register() {
		return "register";
	}

	@PostMapping("/register")
	public String addAccount(AccountForm form, Model model, RedirectAttributes rttr) {
		User user = form.toEntity();
		boolean isVerified = accountService.verification(user);
		System.out.println("아이디 유무: " + isVerified);
		if (isVerified) {
			model.addAttribute("errorMessage", "아이디(로그인 전화번호, 로그인 전용 아이디) 또는 비밀번호가 이미 있습니다. 다르게 입력해 주세요.");
			return "register";
		} else {
			accountService.insert(user);
			return "registerSuccess";
		}
	}
}
