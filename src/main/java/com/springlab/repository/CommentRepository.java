package com.springlab.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springlab.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

	@Query(value = "SELECT * FROM comment WHERE board_id = :boardId", nativeQuery = true)
	List<Comment> findByBoardId(@Param("boardId") Long boardId);

	@Query(value = "SELECT * FROM comment WHERE nickname = :nickname", nativeQuery = true)
	List<Comment> findByNickName(@Param("nickname") String nickname);
}
