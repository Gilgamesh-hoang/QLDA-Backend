package com.commic.v1.service;

import com.commic.v1.dto.DataListResponse;
import com.commic.v1.dto.responses.BookResponse;
import com.commic.v1.entities.Book;
import com.commic.v1.exception.AppException;
import com.commic.v1.exception.ErrorCode;
import com.commic.v1.mapper.BookMapper;
import com.commic.v1.repositories.IBookRepository;
import com.commic.v1.repositories.IChapterRepository;
import com.commic.v1.services.search.SearchServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class SearchServiceTest {

    @Mock
    private IBookRepository bookRepository;

    @Mock
    private IChapterRepository chapterRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private SearchServiceImpl searchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetRankBy_WithRatingType_ReturnsBooks() {
        Pageable pageable = PageRequest.of(0, 5);
        Book book = new Book(); // Tạo object Book mẫu
        Page<Book> page = new PageImpl<>(List.of(book)); // Giả lập Page<Book>

        when(bookRepository.findAllOrderByRatingDesc(pageable)).thenReturn(page);
        when(bookMapper.toBookResponseDTO(any())).thenReturn(new BookResponse());

        DataListResponse<BookResponse> response = searchService.getRankBy("RATING", pageable);

        assertNotNull(response);
        assertEquals(1, response.getTotalPages());
        assertEquals(1, response.getData().size());
    }

    @Test
    void testGetRankBy_WithInvalidType_ThrowsException() {
        Pageable pageable = PageRequest.of(0, 5);

        AppException exception = assertThrows(AppException.class, () ->
                searchService.getRankBy("INVALID", pageable)
        );

        assertEquals(ErrorCode.PARAMETER_NOT_VALID, exception.getErrorCode());
    }

    @Test
    void testGetRankBy_WithNoResults_ThrowsNotFoundException() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Book> emptyPage = new PageImpl<>(Collections.emptyList());

        when(bookRepository.findAllOrderByRatingDesc(pageable)).thenReturn(emptyPage);

        AppException exception = assertThrows(AppException.class, () ->
                searchService.getRankBy("RATING", pageable)
        );

        assertEquals(ErrorCode.NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void testGetRankBy_WithCategoryId_ReturnsBooks() {
        Pageable pageable = PageRequest.of(0, 5);
        Book book = new Book(); // Tạo object Book mẫu
        Page<Book> page = new PageImpl<>(List.of(book)); // Giả lập Page<Book>

        when(bookRepository.findAllOrderByRatingDesc(pageable)).thenReturn(page);
        when(bookMapper.toBookResponseDTO(any())).thenReturn(new BookResponse());

        DataListResponse<BookResponse> response = searchService.getRankBy("RATING", 1, pageable);

        assertNotNull(response);
        assertEquals(1, response.getTotalPages());
        assertEquals(1, response.getData().size());
    }
}
