package com.commic.testManageBook;

import com.commic.v1.dto.requests.BookRequest;
import com.commic.v1.dto.responses.APIResponse;
import com.commic.v1.dto.responses.BookResponse;
import com.commic.v1.dto.responses.CategoryResponse;
import com.commic.v1.entities.Book;
import com.commic.v1.entities.Category;
import com.commic.v1.mapper.BookMapper;
import com.commic.v1.repositories.IBookRepository;
import com.commic.v1.repositories.ICategoryRepository;
import com.commic.v1.services.book.BookServiceImpl;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.commic.v1.services.category.CategoryService;
import com.commic.v1.services.chapter.ChapterServiceImpl;
import com.commic.v1.services.chapter.IChapterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest  // Khởi động toàn bộ context của Spring Boot
@Transactional
public class testManageBookSevice {
    @Mock
    private IBookRepository bookRepository;

    @Mock
    private ICategoryRepository categoryRepository;
    @Mock
    private ChapterServiceImpl chapterService;
    @Mock
    private BookMapper bookMapper;
    @Autowired
    CategoryService categoryService;
    @InjectMocks
    private BookServiceImpl bookService;  // The class containing the addBook method
    private BookRequest bookRequest;
    private BookRequest bookRequest2;
    private Book existingBook;
    private Book book;
    private Category category;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookRequest = new BookRequest();
        bookRequest.setId(1);
        bookRequest.setName("Book");
        bookRequest.setAuthor("Author");
        bookRequest.setDescription("Description");
        bookRequest.setThumbnail("updated_thumbnail.jpg");
        bookRequest.setStatus("Available");
        Set<String> categorySet = Set.of("Bí ẩn", "Tự lực");
        List<String> categoryList = categorySet.stream().collect(Collectors.toList());
        bookRequest.setCategoryNames(categoryList);
        bookRequest2 = new BookRequest();
        bookRequest2.setId(1);
        bookRequest2.setName("THẦN HỒN VÕ ĐẾ");
        book = new Book();
        book.setName("THẦN HỒN VÕ ĐẾ");
    }

    @Test
    void deleteBook_BookNotFound() {
        when(bookRepository.existsById(1)).thenReturn(false);
        APIResponse<Void> response = bookService.deleteBook(1);
        assertEquals("Book not found", response.getMessage());
    }
    @Test
    public void testUpdateBook_BookExists() {
        existingBook = new Book(); // Create a Book object with required fields
        existingBook.setId(1); // Use Long type for ID
        existingBook.setName("Existing Book");
        existingBook.setAuthor("Author");
        existingBook.setDescription("Description");
        existingBook.setThumbnail("Thumbnail");
        existingBook.setStatus("Available");

        Category category1 = new Category();
        category1.setName("Fiction");
        category1.setBooks(new HashSet<>(Collections.singletonList(existingBook)));

        existingBook.setCategories(new HashSet<>(Collections.singletonList(category1)));

        when(bookRepository.findById(1)).thenReturn(Optional.of(existingBook));
        APIResponse<Void> response = bookService.updateBook(bookRequest);

        // Assert
        assertEquals("Add book successfully", response.getMessage());
    }

    @Test
    public void testUpdateBook_BookNotFound() {
        // Arrange
        BookRequest bookRequest = new BookRequest();
        bookRequest.setId(1);

        when(bookRepository.findById(1)).thenReturn(Optional.empty());

        // Act
        APIResponse<Void> response = bookService.updateBook(bookRequest);


        assertEquals("Book not found", response.getMessage());
    }



    @Test
    public void deleteBook_Success() {
        // Arrange
        Integer bookId = 1;
        Book book = new Book(); // Replace with actual Book constructor
        book.setId(bookId);
        book.setIsDeleted(false);

        when(bookRepository.existsById(bookId)).thenReturn(true);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        // Act
        APIResponse<Void> response = bookService.deleteBook(bookId);

        assertEquals("Delete book successfully", response.getMessage());
        verify(chapterService).deleteByBookId(bookId);
        verify(bookRepository).save(book);
    }

    @Test
    void deleteBook_InternalError() {
        when(bookRepository.existsById(1)).thenReturn(true);
        when(bookRepository.findById(1)).thenThrow(RuntimeException.class);
        APIResponse<Void> response = bookService.deleteBook(1);
        assertEquals("Delete book failed", response.getMessage());
    }

    @Test
    public void testAddBook_BookAlreadyExists() {
        // Arrange
        when(bookMapper.toBook(bookRequest)).thenReturn(book);
        Example<Book> example = Example.of(Book.builder().name(book.getName()).build());
        when(bookRepository.exists(example)).thenReturn(true);

        // Act
        APIResponse<Void> response = bookService.addBook(bookRequest);

        // Assert
        assertEquals("Book name already exists", response.getMessage());
    }

    @Test
    public void addBook_BookNameExists_ReturnsConflict() {
        when(bookMapper.toBook(any())).thenReturn(book);
        when(bookRepository.exists(any())).thenReturn(true);

        APIResponse<Void> response = bookService.addBook(bookRequest2);

        assertEquals("Book name already exists", response.getMessage());
    }

    @Test
    public void addBook_Success_ReturnsNoContent() {
        when(bookMapper.toBook(any())).thenReturn(book);
        when(bookRepository.exists(any())).thenReturn(false);
        when(categoryRepository.findByNameIn(any())).thenReturn(new HashSet<>());

        APIResponse<Void> response = bookService.addBook(bookRequest);

        assertEquals("Add book successfully", response.getMessage());
        verify(bookRepository).save(book);
    }


}
