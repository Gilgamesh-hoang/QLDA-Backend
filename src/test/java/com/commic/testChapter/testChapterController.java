package com.commic.testChapter;
import com.commic.v1.api.user.ChapterContentController;
import com.commic.v1.dto.responses.APIResponse;
import com.commic.v1.dto.responses.ChapterContentResponse;
import com.commic.v1.exception.ErrorCode;
import com.commic.v1.services.chapter.IChapterContentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class testChapterController {
    @InjectMocks
    private ChapterContentController chapterContentController;

    @Mock
    private IChapterContentService chapterContentService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetChapterContent_IdExists() {
        Integer chapterId = 1;

        // Giả lập dữ liệu trả về từ service
        List<ChapterContentResponse> chapterContentResponses = Collections.singletonList(new ChapterContentResponse());
        when(chapterContentService.getChapterContent(chapterId)).thenReturn(chapterContentResponses);

        // Gọi phương thức controller
        APIResponse<List<ChapterContentResponse>> response = chapterContentController.getChapterContent(chapterId);

        // Kiểm tra kết quả
        assertNotNull(response);
        assertEquals(ErrorCode.FOUND.getCode(), response.getCode());
        assertEquals(ErrorCode.FOUND.getMessage(), response.getMessage());
        assertEquals(chapterContentResponses, response.getResult());


    }

    @Test
    public void testGetChapterContent_IdDoesNotExist() {
        Integer chapterId = 99;

        // Giả lập dữ liệu trả về từ service
        when(chapterContentService.getChapterContent(chapterId)).thenReturn(Collections.emptyList());

        // Gọi phương thức controller
        APIResponse<List<ChapterContentResponse>> response = chapterContentController.getChapterContent(chapterId);

        // Kiểm tra kết quả
        assertNotNull(response);
        assertEquals(ErrorCode.FOUND.getCode(), response.getCode());
        assertEquals(ErrorCode.FOUND.getMessage(), response.getMessage());
        assertTrue(response.getResult().isEmpty());

    }
}