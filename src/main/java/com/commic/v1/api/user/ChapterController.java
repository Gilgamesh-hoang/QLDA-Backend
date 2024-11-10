package com.commic.v1.api.user;


import com.commic.v1.dto.responses.ChapterResponse;
import com.commic.v1.services.chapter.IChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chapters")

public class ChapterController {
    @Autowired
    IChapterService chapterServices;

    @GetMapping("/book/{id}")
    public ResponseEntity<List<ChapterResponse>> getChapters(@PathVariable(value = "id") Integer bookId) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        List<ChapterResponse> chapters = chapterServices.getChaptersByBookId(bookId, sort);
        return ResponseEntity.ok(chapters);
    }

}
