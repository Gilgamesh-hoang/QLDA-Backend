package com.commic.v1.api.user;

import com.commic.v1.dto.responses.APIResponse;
import com.commic.v1.dto.responses.ChapterContentResponse;
import com.commic.v1.dto.responses.ChapterResponse;
import com.commic.v1.exception.ErrorCode;
import com.commic.v1.services.chapter.IChapterContentService;
import com.commic.v1.services.chapter.IChapterService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ChapterContentController {
    IChapterContentService chapterContentService;

    @GetMapping(value = "/chapter-{id}")
    public APIResponse<List<ChapterContentResponse>> getChapterContent(@PathVariable Integer id) {
        APIResponse<List<ChapterContentResponse>> apiResponse = new APIResponse<>();
        List<ChapterContentResponse> chapterContentResponses = chapterContentService.getChapterContent(id);
        apiResponse.setCode(ErrorCode.FOUND.getCode());
        apiResponse.setMessage(ErrorCode.FOUND.getMessage());
        apiResponse.setResult(chapterContentResponses);
        return apiResponse;
    }
}
