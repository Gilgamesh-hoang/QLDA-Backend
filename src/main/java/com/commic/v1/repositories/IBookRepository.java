package com.commic.v1.repositories;

import com.commic.v1.entities.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IBookRepository extends JpaRepository<Book, Integer> {
    List<Book> findAll();

    Page<Book> findAll(Pageable pageable);

    Page<Book> findByNameContaining(@Param("name") String name, Pageable pageable);

    Page<Book> findByNameContainingAndCategoriesId(@Param("name") String name, @Param("categoryId") Integer categoryId, Pageable pageable);

    @Query("SELECT book FROM Book book JOIN book.chapters chapter GROUP BY book.id ORDER BY SUM(chapter.view) DESC")
    Page<Book> findAllOrderByViewDesc(Pageable pageable);

    @Query("SELECT book FROM Book book JOIN book.categories category JOIN book.chapters chapter WHERE category.id = :categoryId GROUP BY book.id ORDER BY SUM(chapter.view) DESC ")
    Page<Book> findAllOrderByViewDesc(Integer categoryId, Pageable pageable);

    @Query("SELECT book FROM Book book JOIN book.chapters chapter JOIN chapter.ratings rating GROUP BY book.id ORDER BY AVG(rating.star) DESC")
    Page<Book> findAllOrderByRatingDesc(Pageable pageable);

    @Query("SELECT book FROM Book book JOIN book.categories category JOIN book.chapters chapter JOIN chapter.ratings rating WHERE category.id= :categoryId GROUP BY book.id ORDER BY AVG(rating.star) DESC ")
    Page<Book> findAllOrderByRatingDesc(Integer categoryId, Pageable pageable);

    @Query("SELECT DISTINCT c.name FROM Book b JOIN b.categories c WHERE b.id = :bookId")
    List<String> findCategoryNamesByBookId(Integer bookId);

    @Query("SELECT DISTINCT b FROM Book b JOIN FETCH b.chapters c ORDER BY c.publishDate DESC")
    Page<Book> findByPublishDateOrderByNearestDate(Pageable pageable);

    @Query("SELECT DISTINCT b FROM Category category JOIN category.books b JOIN FETCH b.chapters c WHERE category.id = :categoryId ORDER BY c.publishDate DESC")
    Page<Book> findByPublishDateOrderByNearestDate(Pageable pageable, @Param("categoryId") Integer categoryId);

    @Query("SELECT DISTINCT book FROM Category category JOIN category.books book JOIN book.chapters chapter WHERE category.id = :categoryId ORDER BY chapter.publishDate DESC")
    Page<Book> findByPublishDateOrderByNearestDate(Integer categoryId, Pageable pageable);

    @Query("SELECT book FROM Book book JOIN book.chapters chapter WHERE chapter.id = :chapterId AND book.isDeleted = false")
    Book findBookByChapterId(@Param("chapterId") Integer chapterId);

    Optional<Book> findById(Integer id);

    @Query("SELECT book.thumbnail FROM Book book WHERE book.id = :bookId")
    Optional<String> findThumbnailBookId(Integer bookId);

    @Query("SELECT SUM(chapter.view) FROM Book book JOIN book.chapters chapter")
    Integer countAllViews();

    @Query("SELECT COUNT(book) FROM Book book")
    Integer countAllBooks();

    @Query("SELECT SUM(chapter.view) FROM Book b JOIN b.chapters chapter WHERE b.id = :bookId")
    Integer countAllViewsByBookId(@Param("bookId") Integer bookId);

    @Query("SELECT COUNT(chapter) FROM Book b JOIN b.chapters chapter WHERE b.id = :bookId")
    Integer countAllChapterByBookId(@Param("bookId") Integer bookId);

    @Query("SELECT COUNT(comment) FROM Book b JOIN b.chapters chapter JOIN chapter.comments comment WHERE b.id = :bookId")
    Integer countAllCommentByBookId(Integer bookId);
}
