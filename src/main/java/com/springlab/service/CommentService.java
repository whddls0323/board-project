package com.springlab.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springlab.domain.Board;
import com.springlab.domain.Comment;
import com.springlab.dto.CommentDto;
import com.springlab.repository.BoardRepository;
import com.springlab.repository.CommentRepository;

@Service
public class CommentService {

	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private BoardRepository boardRepository;

	public List<CommentDto> comments(Long boardId) {

		return commentRepository.findByBoardId(boardId).stream().map(comment -> CommentDto.createCommentDto(comment))
				.collect(Collectors.toList());
	}

	@Transactional
	public CommentDto create(Long boardId, CommentDto dto) {
		Board board = boardRepository.findById(boardId)
				.orElseThrow(() -> new IllegalArgumentException("댓글 생성 실패. " + "대상 게시글이 없습니다."));

		Comment comment = Comment.createComment(dto, board);

		Comment created = commentRepository.save(comment);
		return CommentDto.createCommentDto(created);
	}

	@Transactional
	public CommentDto update(Long id, CommentDto dto) {
		Comment target = commentRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("댓글 수정 실패 " + "대상 댓글이 없습니다."));
		target.patch(dto);
		Comment updated = commentRepository.save(target);
		return CommentDto.createCommentDto(updated);
	}

	@Transactional
	public CommentDto delete(Long id) {
		Comment target = commentRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("댓글 삭제 실패 " + "대상이 없습니다."));
		commentRepository.delete(target);
		return CommentDto.createCommentDto(target);
	}
}