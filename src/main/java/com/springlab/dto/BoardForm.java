package com.springlab.dto;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.springlab.domain.Board;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class BoardForm {

	private String title;
	private String writer;
	private String content;
	private Timestamp regdate;
	private int cnt;
	private List<MultipartFile> imageFiles;

	public BoardForm(String title, String writer, String content, Timestamp regdate, int cnt,
			List<MultipartFile> imageFiles) {
		this.title = title;
		this.writer = writer;
		this.content = content;
		this.regdate = regdate;
		this.cnt = cnt;
		this.imageFiles = imageFiles;
	}

	public Board toEntity() {
		return new Board(title, writer, content, regdate, cnt);
	}
}
