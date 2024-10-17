package com.commic.v1.service;

import com.commic.v1.dto.responses.BookResponse;
import com.commic.v1.entities.Book;
import com.commic.v1.mapper.BookMapper;
import com.commic.v1.repositories.IBookRepository;
import com.commic.v1.repositories.IChapterRepository;
import com.commic.v1.services.book.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class BookServiceTest {

    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private IBookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private IChapterRepository chapterRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetBookByChapterId() {
        Integer chapterId = 1;
        Book book = new Book();
        BookResponse bookResponse = new BookResponse();
        when(bookRepository.findBookByChapterId(chapterId)).thenReturn(book);
        when(bookMapper.toBookResponseDTO(book)).thenReturn(bookResponse);

        BookResponse response = bookService.getBookByChapterId(chapterId);

        assertEquals(bookResponse, response);
    }

    @Test
    public void testGetDescription() {
        Integer id = 1;
        Book book = new Book();
        BookResponse bookResponse = new BookResponse();
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(chapterRepository.countByBookId(book.getId())).thenReturn(0);
        when(chapterRepository.countViewByBookId(book.getId())).thenReturn(0);
        when(chapterRepository.countStarAvgByBookId(book.getId())).thenReturn(0.0);
        when(chapterRepository.findFirstPublishDateByBookId(book.getId())).thenReturn(new Date(System.currentTimeMillis()));
        when(bookRepository.findCategoryNamesByBookId(book.getId())).thenReturn(new ArrayList<>());
        when(bookMapper.toBookResponseDTO(book)).thenReturn(bookResponse);

        BookResponse response = bookService.getDescription(id);

        assertEquals(bookResponse, response);
    }
}
