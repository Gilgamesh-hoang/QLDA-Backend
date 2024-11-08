package com.commic.v1.repositories;

import com.commic.v1.entities.Comment;
import com.commic.v1.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ICommentRepository extends JpaRepository<Comment, Integer> {
    Page<Comment> findAll(Pageable pageable);

    @Query("SELECT c FROM Comment c WHERE c.user.id = :userId AND c.isDeleted = false")
    Page<Comment> findAllByUserIdAndIsDeletedFalse(Integer userId,Pageable pageable);

    List<Comment> findByChapterId(Integer id);

    @Query("SELECT c FROM Comment c WHERE c.chapter.id = :idChapter")
    List<Comment> findAllByChapterId(Integer idChapter);

    Optional<Comment> findById(Integer id);

    @Query("SELECT COUNT(comment) FROM Comment comment WHERE comment.isDeleted = false")
    Optional<Integer> countByUser(User user);

    @Query("SELECT c FROM Comment c WHERE c.chapter.id = :chapterId AND c.state = 1 AND c.isDeleted = false ")
    Page<Comment> getCommentByChapterId(@Param("chapterId") Integer idChapter, Pageable pageable);

    @Query("SELECT c FROM Comment c WHERE c.chapter.book.id = :bookId AND c.state = 1 AND c.isDeleted = false ")
    Page<Comment> getCommentByBookId(@Param("bookId") Integer idBook, Pageable pageable);

    @Query("SELECT COUNT(comment) FROM Comment comment WHERE comment.isDeleted = false")
    Integer countAllComment();

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.user.id = :userId AND c.state = 1 AND c.isDeleted = false")
    Optional<Integer> countAllCommentByUserId(Integer userId);

    @Query("SELECT COUNT(c.id) FROM Comment c " +
            "JOIN c.chapter ch " +
            "JOIN ch.book b " +
            "WHERE b.id = :bookId AND c.state = 1 AND c.isDeleted = false")
    Integer countCommentsByBookId(@Param("bookId") int bookId);
}
