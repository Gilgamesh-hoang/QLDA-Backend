package com.commic.testManageBook;

import com.commic.v1.api.admin.BookController;
import com.commic.v1.dto.requests.BookRequest;
import com.commic.v1.dto.responses.APIResponse;
import com.commic.v1.services.book.IBookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
public class testManageBookController {
    @InjectMocks
    private BookController bookController;

    @Mock
    private IBookService bookService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testAddBook_Success() {
        // Sample BookRequest object for testing
        BookRequest bookRequest = new BookRequest();
        bookRequest.setName("Sample Book");
        bookRequest.setAuthor("John Doe");
        bookRequest.setDescription("This is a sample book description.");
        bookRequest.setThumbnail("http://example.com/thumbnail.jpg");
        bookRequest.setStatus("AVAILABLE");
        // Change Set to List
        bookRequest.setCategoryNames(Arrays.asList("Fiction", "Adventure"));

        APIResponse<Void> mockResponse = new APIResponse<>(HttpStatus.NO_CONTENT.value(), "Add book successfully", null);

        when(bookService.addBook(any(BookRequest.class))).thenReturn(mockResponse);

        ResponseEntity<APIResponse<Void>> response = bookController.addBook(bookRequest);

        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCodeValue());
        assertEquals("Add book successfully", response.getBody().getMessage());
        verify(bookService, times(1)).addBook(bookRequest);
    }

    @Test
    public void testUpdateBook_Success() {
        // Sample BookRequest object for testing
        BookRequest bookRequest = new BookRequest();
        bookRequest.setId(1); // Assume you are updating a book with ID 1
        bookRequest.setName("Updated Book");
        bookRequest.setAuthor("Jane Doe");
        bookRequest.setDescription("This is an updated sample book description.");
        bookRequest.setThumbnail("http://example.com/updated_thumbnail.jpg");
        bookRequest.setStatus("CHECKED_OUT");
        // Change Set to List
        bookRequest.setCategoryNames(Arrays.asList("Non-Fiction", "Mystery"));

        APIResponse<Void> mockResponse = new APIResponse<>(HttpStatus.NO_CONTENT.value(), "Update book successfully", null);

        when(bookService.updateBook(any(BookRequest.class))).thenReturn(mockResponse);

        ResponseEntity<APIResponse<Void>> response = bookController.updateBook(bookRequest);

        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCodeValue());
        assertEquals("Update book successfully", response.getBody().getMessage());
        verify(bookService, times(1)).updateBook(bookRequest);
    }

    @Test
    public void testDeleteBook_Success() {
        Integer bookId = 1; // Assume you want to delete a book with ID 1
        APIResponse<Void> mockResponse = new APIResponse<>(HttpStatus.NO_CONTENT.value(), "Delete book successfully", null);

        when(bookService.deleteBook(bookId)).thenReturn(mockResponse);

        ResponseEntity<APIResponse<Void>> response = bookController.deleteBook(bookId);

        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCodeValue());
        assertEquals("Delete book successfully", response.getBody().getMessage());
        verify(bookService, times(1)).deleteBook(bookId);
    }
}
