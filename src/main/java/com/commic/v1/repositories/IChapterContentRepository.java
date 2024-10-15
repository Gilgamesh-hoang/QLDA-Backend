package com.commic.v1.repositories;

import com.commic.v1.entities.ChapterContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IChapterContentRepository extends JpaRepository<ChapterContent, Integer> {
    List<ChapterContent> findByChapterId(Integer id);
}
