package com.commic.v1.repositories;

import com.commic.v1.entities.Chapter;
import com.commic.v1.entities.ChapterContent;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface IChapterRepository extends JpaRepository<Chapter, Integer> {
    List<Chapter> findByBookId(Integer id, Sort sort);

    Integer countByBookId(Integer id);

    //Lấy ra số sao trung bình theo id book
    @Query("SELECT AVG(rating.star) FROM Chapter chapter JOIN chapter.ratings rating WHERE chapter.book.id = :bookId")
    Double countStarAvgByBookId(Integer bookId);

    //    Lấy ra tổng số lượt xem theo id book
    @Query("SELECT SUM(chapter.view) FROM Chapter chapter WHERE chapter.book.id = :bookId")
    Integer countViewByBookId(Integer bookId);


    @Query("SELECT MIN(c.publishDate) FROM Chapter c WHERE c.book.id = :bookId")
    Date findFirstPublishDateByBookId(Integer bookId);

    @Query("SELECT cc FROM ChapterContent cc WHERE cc.chapter.id = :id ORDER BY cc.chapter.id")
    List<ChapterContent> findByChapterId(Integer id);

}
