package com.springlab;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import com.springlab.domain.Board;
import com.springlab.domain.BoardImage;
import com.springlab.dto.BoardForm;
import com.springlab.repository.BoardImageRepository;
import com.springlab.repository.BoardRepository;
import com.springlab.service.BoardService;

@ExtendWith(MockitoExtension.class)
class Test0115ApplicationTests {

	@Mock
	private BoardRepository boardRepository;

	@Mock
	private BoardImageRepository boardImageRepository;

	@InjectMocks
	private BoardService boardService;

	@Test
	void insert_shouldSaveBoardAndImage() throws Exception {

		Board board = new Board();
		BoardForm form = new BoardForm();

		MockMultipartFile file = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test content".getBytes());
		form.setImageFiles(List.of(file));

		boardService.insert(board, form);

		verify(boardRepository).save(board);

		ArgumentCaptor<BoardImage> captor = ArgumentCaptor.forClass(BoardImage.class);
		verify(boardImageRepository).save(captor.capture());

		BoardImage savedImage = captor.getValue();
		assertNotNull(savedImage);
		assertTrue(savedImage.getUrl().contains("test.jpg"));
	}
}
