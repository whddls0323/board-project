package com.springlab.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.springlab.domain.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
	@Modifying
	@Query("update Board b set b.cnt = b.cnt + 1 where b.id = :id")
	void incrementViewCount(@Param("id") Long id);

	Page<Board> findByTitleContaining(String keyword, Pageable pageable);

	Page<Board> findByWriterContaining(String keyword, Pageable pageable);
}
