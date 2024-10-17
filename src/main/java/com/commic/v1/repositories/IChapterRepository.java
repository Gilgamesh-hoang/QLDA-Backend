package com.commic.v1.repositories;

import com.commic.v1.entities.Chapter;
import com.commic.v1.entities.ChapterContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface IChapterRepository extends JpaRepository<Chapter, Integer> {
    @Query("SELECT cc FROM ChapterContent cc WHERE cc.chapter.id = :id ORDER BY cc.chapter.id")
    List<ChapterContent> findByChapterId(Integer id);
}
