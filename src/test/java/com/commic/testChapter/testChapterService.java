package com.commic.testChapter;
import com.commic.v1.dto.responses.ChapterContentResponse;
import com.commic.v1.entities.Chapter;
import com.commic.v1.entities.ChapterContent;
import com.commic.v1.mapper.ChapterMapper;
import com.commic.v1.repositories.IChapterContentRepository;
import com.commic.v1.repositories.IChapterRepository;
import com.commic.v1.services.chapter.ChapterContentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class testChapterService {
        @Mock
        private IChapterRepository chapterRepository;

        @Mock
        private ChapterMapper chapterMapper;

        @Mock
        private IChapterContentRepository contentRepository;

        @InjectMocks
        private ChapterContentServiceImpl chapterContentService;

        private Chapter chapter;
        private ChapterContent chapterContent;
        private ChapterContentResponse chapterContentResponse;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        // Initialize Chapter and ChapterContent with test data
        chapter = new Chapter();
        chapter.setId(1);
        chapter.setView(0);

        chapterContent = new ChapterContent();
        chapterContent.setId(1);
        chapterContent.setChapter(chapter);
    }

    @Test
    public void testGetChapterContent_IdExists() {
        Integer chapterId = 1;

        // Mocking dependencies
        when(chapterRepository.findById(chapterId)).thenReturn(Optional.of(chapter));
        when(chapterRepository.findByChapterId(chapterId)).thenReturn(List.of(chapterContent));
        when(chapterMapper.convertToDTO(any(ChapterContent.class))).thenReturn(new ChapterContentResponse());

        // Call the method
        List<ChapterContentResponse> response = chapterContentService.getChapterContent(chapterId);

        // Verify results
        assertNotNull(response);
        assertFalse(response.isEmpty());
        verify(chapterRepository).save(any(Chapter.class)); // Verifies that the view count is updated
    }
    @Test
    public void testGetChapterContent_NotExist() {
        Integer chapterId = 999;

        // Mock dependencies to return empty results
        when(chapterRepository.findById(chapterId)).thenReturn(Optional.empty());

        // Execute and assert exception
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> chapterContentService.getChapterContent(chapterId)
        );

        // Assert that the status and message are as expected
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());

    }
}
