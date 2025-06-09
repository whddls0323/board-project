package com.springlab.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.springlab.domain.Board;
import com.springlab.domain.BoardImage;
import com.springlab.dto.BoardForm;
import com.springlab.repository.BoardImageRepository;
import com.springlab.repository.BoardRepository;

import jakarta.transaction.Transactional;

@Service
public class BoardService {

	private BoardRepository boardRepository;
	private BoardImageRepository boardImageRepository;

	public BoardService(BoardRepository boardRepository, BoardImageRepository boardImageRepository) {
		this.boardRepository = boardRepository;
		this.boardImageRepository = boardImageRepository;
	}

	public Page<Board> findPagedBoards(Pageable pageable) {
		return boardRepository.findAll(pageable);
	}

	public void insert(Board board, BoardForm form) {
		boardRepository.save(board);
		if (form.getImageFiles() != null && !form.getImageFiles().isEmpty()) {
			for (MultipartFile file : form.getImageFiles()) {
				if (file != null && !file.isEmpty() && file.getOriginalFilename() != null
						&& !file.getOriginalFilename().isBlank()) {
					UUID uuid = UUID.randomUUID();
					String imageFileName = uuid + "_" + file.getOriginalFilename();
					String uploadDir = "C:/upload/";
					String webPath = "/images/";
					File uploadFolder = new File(uploadDir);
					if (!uploadFolder.exists()) {
						uploadFolder.mkdirs();
					}

					File destinationFile = new File(uploadDir + imageFileName);

					try {
						file.transferTo(destinationFile);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
					BoardImage image = BoardImage.builder().url(webPath + imageFileName).board(board).build();
					boardImageRepository.save(image);
				}
			}
		} else {
			System.out.println("파일이 현재 비어 있습니다.");
		}
	}

	public List<Board> findAll() {
		return (List<Board>) boardRepository.findAll();
	}

	public Optional<Board> findById(Long seq) {
		return boardRepository.findById(seq);
	}

	@Transactional
	public void increaseViewCount(Long id) {
		boardRepository.incrementViewCount(id);
	}

	public void update(Board board) {
		boardRepository.save(board);
	}

	public void delete(Long seq) {
		boardRepository.deleteById(seq);
	}

	public Page<Board> findByTitle(String keyword, Pageable pageable) {
		return boardRepository.findByTitleContaining(keyword, pageable);
	}

	public Page<Board> findByWriter(String keyword, Pageable pageable) {
		return boardRepository.findByWriterContaining(keyword, pageable);
	}
}
