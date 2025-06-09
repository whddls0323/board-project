package com.springlab.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springlab.domain.BoardImage;

public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {

	@Query(value = "SELECT * FROM board_image WHERE board_id = :boardId", nativeQuery = true)
	List<BoardImage> findByBoardId(@Param("boardId") Long boardId);
}
