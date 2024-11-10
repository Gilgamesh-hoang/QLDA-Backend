package com.commic.v1.services.chapter;

import com.commic.v1.dto.responses.ChapterResponse;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface IChapterService {
    void deleteByBookId(Integer id);

    List<ChapterResponse> getChaptersByBookId(Integer bookId, Sort sort);

}
