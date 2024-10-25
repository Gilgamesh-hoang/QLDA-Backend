package com.commic.v1.repositories;

import com.commic.v1.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByChapterId(Integer id);
}
