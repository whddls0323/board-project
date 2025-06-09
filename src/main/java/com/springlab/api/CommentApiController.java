package com.springlab.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.springlab.domain.Board;
import com.springlab.domain.Comment;
import com.springlab.dto.CommentDto;
import com.springlab.repository.BoardRepository;
import com.springlab.repository.CommentRepository;
import com.springlab.service.CommentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j // log.info 사용가능
@RequiredArgsConstructor // final이 붙은 필드 생성자 자동 생성
@RestController // 데이터를 json형식으로 응답
public class CommentApiController {

	private final CommentService commentService;
	private final BoardRepository boardRepository;
	private final CommentRepository commentRepository;

	@PostMapping("/api/board/{boardId}/comments")
	public ResponseEntity<CommentDto> create(@PathVariable("boardId") Long boardId, @RequestBody CommentDto dto) {
		CommentDto createdDto = commentService.create(boardId, dto);
		log.debug("Created comment: {}", createdDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdDto);
	}

	@PostMapping("/api/board/{boardId}/ai-comment")
	public ResponseEntity<Map<String, String>> generateAiComment(@PathVariable("boardId") Long boardId) {
		Board board = boardRepository.findById(boardId)
				.orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

		String postTitle = board.getTitle();
		String postContent = board.getContent();

		List<Comment> comments = commentRepository.findByBoardId(boardId);
		String allComments = comments.stream().map(Comment::getBody).collect(Collectors.joining("\n"));

		// FastAPI 요청을 위한 Map 생성
		Map<String, String> payload = new HashMap<>();
		payload.put("post", postTitle + "\n" + postContent);
		payload.put("comments", allComments);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Map<String, String>> request = new HttpEntity<>(payload, headers);

		RestTemplate restTemplate = new RestTemplate();
		try {
			ResponseEntity<Map> response = restTemplate.postForEntity("http://localhost:8000/generate-comment", request,
					Map.class);

			if (response.getStatusCode().is2xxSuccessful()) {
				String aiComment = (String) response.getBody().get("ai_comment");
				return ResponseEntity.ok(Map.of("aiComment", aiComment));
			} else {
				return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(Map.of("message", "FastAPI 응답 실패"));
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "FastAPI 연결 실패"));
		}
	}

	@PatchMapping("/api/comments/{id}")
	public ResponseEntity<CommentDto> update(@PathVariable("id") Long id, @RequestBody CommentDto dto) {
		CommentDto updatedDto = commentService.update(id, dto);
		log.debug("Updated comment: {}", updatedDto);
		return ResponseEntity.status(HttpStatus.OK).body(updatedDto);
	}

	@DeleteMapping("/api/comments/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
		commentService.delete(id);
		log.info("Deleted comment with id: {}", id);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}