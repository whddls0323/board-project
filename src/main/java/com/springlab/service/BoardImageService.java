package com.springlab.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springlab.domain.BoardImage;
import com.springlab.repository.BoardImageRepository;

@Service
public class BoardImageService {

	@Autowired
	private BoardImageRepository boardImageRepository;

	public List<BoardImage> images(Long boardId) {
		return boardImageRepository.findByBoardId(boardId);
	}
}
