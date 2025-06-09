package com.springlab.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.springlab.service.AccountService;
import com.springlab.service.BoardService;

@Controller
public class MainController { // 처음 화면

	private final AccountService accountService;
	private final BoardService boardService;

	public MainController(AccountService accountService, BoardService boardService) {
		this.accountService = accountService;
		this.boardService = boardService;
	}

	@GetMapping("/")
	public String home() {
		return "redirect:/login";
	}

	@GetMapping("/login")
	public String login(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			String errorMessage = (String) session.getAttribute("errorMessage");
			model.addAttribute("errorMessage", errorMessage);
			session.removeAttribute("errorMessage");
		}
		return "login";
	}
}
