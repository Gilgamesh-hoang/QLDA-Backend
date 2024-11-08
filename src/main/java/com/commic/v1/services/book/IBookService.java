package com.commic.v1.services.book;

import com.commic.v1.dto.requests.BookRequest;
import com.commic.v1.dto.responses.APIResponse;
import com.commic.v1.dto.responses.BookResponse;
import com.commic.v1.entities.Book;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IBookService {
    List<Book> getAllBooks(Pageable pageable);

    BookResponse getDescription(Integer id);

    APIResponse<Void> addBook(BookRequest bookRequest);

    APIResponse<Void> updateBook(BookRequest bookRequest);

    BookResponse getBookByChapterId(Integer chapterId);

    APIResponse<Void> deleteBook(Integer id);
}

