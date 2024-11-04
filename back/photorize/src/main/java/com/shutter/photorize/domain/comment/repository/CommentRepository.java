package com.shutter.photorize.domain.comment.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shutter.photorize.domain.comment.entity.Comment;
import com.shutter.photorize.domain.memory.entity.Memory;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

	@Query("SELECT c FROM Comment c JOIN FETCH c.member WHERE c.memory = :memory ORDER BY c.createdAt DESC")
	Slice<Comment> findCommentsWithMemberByMemory(@Param("memory") Memory memory, Pageable pageable);
}
