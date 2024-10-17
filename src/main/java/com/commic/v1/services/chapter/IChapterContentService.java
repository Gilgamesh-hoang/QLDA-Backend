package com.commic.v1.services.chapter;

import com.commic.v1.dto.responses.ChapterContentResponse;

import java.util.List;

public interface IChapterContentService {
    List<ChapterContentResponse> getChapterContent(Integer id);

    void deleteByChapterId(Integer id);
}
