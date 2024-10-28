package com.shutter.photorize.domain.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shutter.photorize.domain.comment.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
