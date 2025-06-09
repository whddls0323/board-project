package com.springlab.domain;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Board {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seq;
	private String title;
	private String writer;
	private String content;

	@CreationTimestamp
	private Timestamp regdate;
	private int cnt;

	@OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	@OrderBy("id asc")
	@ToString.Exclude
	private List<BoardImage> boardImages;

	public Board(String title, String writer, String content, Timestamp regdate, int cnt) {
		this.title = title;
		this.writer = writer;
		this.content = content;
		this.regdate = regdate;
		this.cnt = cnt;
	}
}
