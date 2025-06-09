package com.springlab.domain;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.springlab.dto.CommentDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor
@ToString
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "board_id", nullable = false)
	private Board board;

	@Column(length = 255)
	private String nickname;

	@Column(columnDefinition = "TEXT")
	private String body;

	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private Timestamp regdate;

	@UpdateTimestamp
	@Column(nullable = false)
	private Timestamp updatedate;

	public Comment(Board board, String nickname, String body, Timestamp regdate, Timestamp updatedate) {
		this.board = board;
		this.nickname = nickname;
		this.body = body;
		this.regdate = regdate;
		this.updatedate = new Timestamp(System.currentTimeMillis());
	}

	public static Comment createComment(CommentDto dto, Board board) {
		if (dto.getId() != null)
			throw new IllegalArgumentException("댓글 생성 실패. 댓글의 id가 없어야 합니다.");
		if (dto.getBoardId() != board.getSeq())
			throw new IllegalArgumentException("댓글 생성 실패. 게시글의 id가 잘못됐습니다.");
		return new Comment(board, dto.getNickname(), dto.getBody(), dto.getRegdate(), dto.getUpdatedate());
	}

	public void patch(CommentDto dto) {
		if (this.id != dto.getId())
			throw new IllegalArgumentException("댓글 수정 실패. 잘못된 id가 입력됐습니다." + "내가입력한: " + this.id + " db: " + dto);
		if (dto.getNickname() != null)
			this.nickname = dto.getNickname();
		if (dto.getBody() != null)
			this.body = dto.getBody();

		this.updatedate = new Timestamp(System.currentTimeMillis());
	}

}
