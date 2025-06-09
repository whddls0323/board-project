package com.springlab.controller;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springlab.domain.Board;
import com.springlab.domain.BoardImage;
import com.springlab.dto.BoardForm;
import com.springlab.dto.CommentDto;
import com.springlab.service.BoardImageService;
import com.springlab.service.BoardService;
import com.springlab.service.CommentService;

import jakarta.servlet.http.HttpSession;

@Controller
public class BoardController {

	@Autowired
	private BoardService boardService;
	@Autowired
	private BoardImageService boardImageService;
	@Autowired
	private CommentService commentService;

	@GetMapping("/boardList")
	public String boardList(HttpSession session, Model model,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("seq").descending());
		Page<Board> boardPage = boardService.findPagedBoards(pageable);

		// 서버가 재시작될 때마다 새로운 토큰 생성
		String serverToken = (String) session.getAttribute("serverToken");
		if (serverToken == null) {
			serverToken = UUID.randomUUID().toString();
			session.setAttribute("serverToken", serverToken);
		}

		model.addAttribute("boardList", boardPage.getContent());
		model.addAttribute("boardPage", boardPage);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", boardPage.getTotalPages());

		model.addAttribute("serverToken", serverToken);
		return "boardList";
	}

	@GetMapping("/insert")
	public String insertPage(Model model) {
		return "insert";
	}

	@PostMapping("/insert") // entity -> dto,entity 사용
	public String insertBoard(BoardForm form, HttpSession session, Model model) {
		String writer = (String) session.getAttribute("username");
		form.setWriter(writer);
		Board board = form.toEntity();
		System.out.println(board);
		System.out.println(form);
		boardService.insert(board, form);
		return "redirect:/boardList";
	}

	@GetMapping("/update/{seq}")
	public String updatePage(@PathVariable("seq") Long seq, Model model) {
		boardService.increaseViewCount(seq);

		Optional<Board> board = boardService.findById(seq);
		List<BoardImage> image = boardImageService.images(seq);
		List<CommentDto> commentDtos = commentService.comments(seq);
		if (board.isPresent()) {
			model.addAttribute("board", board.get());
			model.addAttribute("images", image);
			model.addAttribute("commentDtos", commentDtos);
		} else
			model.addAttribute("board", null);
		return "update";
	}

	@PostMapping("/update/{seq}") // entity -> dto,entity 사용
	public String updateBoard(@PathVariable("seq") Long seq, BoardForm form, HttpSession session, Model model) {
		Optional<Board> optionalBoard = boardService.findById(seq);
		if (optionalBoard.isPresent()) {
			Board board = optionalBoard.get();
			board.setTitle(form.getTitle());
			String writer = (String) session.getAttribute("username");
			board.setWriter(writer);
			board.setContent(form.getContent());
			board.setRegdate(new Timestamp(System.currentTimeMillis()));

			boardService.update(board);
		}

		return "redirect:/boardList";
	}

	@GetMapping("/delete/{seq}")
	public String deletePage(@PathVariable("seq") Long seq, Model model) {
		boardService.delete(seq);
		return "redirect:/boardList";
	}

	@GetMapping("/search")
	public String searchPosts(@RequestParam("type") String type, @RequestParam("keyword") String keyword,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size, Model model) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("seq").descending());
		Page<Board> boardPage;

		if ("title".equals(type)) {
			boardPage = boardService.findByTitle(keyword, pageable);
		} else if ("nickname".equals(type)) {
			boardPage = boardService.findByWriter(keyword, pageable);
		} else {
			boardPage = Page.empty();
		}

		model.addAttribute("boardList", boardPage.getContent());
		model.addAttribute("boardPage", boardPage);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", boardPage.getTotalPages());

		return "boardList";
	}
}