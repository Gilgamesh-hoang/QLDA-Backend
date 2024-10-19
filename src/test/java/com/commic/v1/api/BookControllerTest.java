package com.commic.v1.api;

import com.commic.v1.api.user.BookController;
import com.commic.v1.dto.DataListResponse;
import com.commic.v1.dto.responses.APIResponse;
import com.commic.v1.dto.responses.BookResponse;
import com.commic.v1.exception.ErrorCode;
import com.commic.v1.services.book.IBookService;
import com.commic.v1.services.search.ISearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class BookControllerTest {
    @InjectMocks
    private BookController bookController;

    @Mock
    private IBookService bookService;
    @Mock
    private ISearchService searchServices;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetBookById() {
        Integer chapterId = 1;
        BookResponse bookResponse = new BookResponse();
        when(bookService.getBookByChapterId(chapterId)).thenReturn(bookResponse);

        ResponseEntity<BookResponse> response = bookController.getBookById(chapterId);

        assertEquals(ResponseEntity.ok(bookResponse), response);
    }

    @Test
    public void testGetDescription() {
        Integer id = 1;
        BookResponse bookResponse = new BookResponse();
        APIResponse<BookResponse> apiResponse = new APIResponse<>();
        apiResponse.setResult(bookResponse);
        when(bookService.getDescription(id)).thenReturn(bookResponse);

        APIResponse<BookResponse> response = bookController.getDescription(id);

        assertEquals(apiResponse.getResult(), response.getResult());
    }

    @Test
    public void testRank() {
        int page = 1;
        int size = 10;
        String type = "";
        Integer categoryId = 0;
        Pageable pageable = PageRequest.of(page - 1, size);
        DataListResponse<BookResponse> dataListResponse = new DataListResponse<>();
        APIResponse<DataListResponse<BookResponse>> apiResponse = new APIResponse<>();
        apiResponse.setCode(ErrorCode.FOUND.getCode());
        apiResponse.setMessage(ErrorCode.FOUND.getMessage());
        apiResponse.setResult(dataListResponse);

        when(searchServices.getRankBy(type, pageable)).thenReturn(dataListResponse);

        APIResponse<DataListResponse<BookResponse>> response = bookController.rank(page, size, type, categoryId);

        assertEquals(apiResponse.getResult(), response.getResult());
        assertEquals(apiResponse.getCode(), response.getCode());
        assertEquals(apiResponse.getMessage(), response.getMessage());
    }
}
