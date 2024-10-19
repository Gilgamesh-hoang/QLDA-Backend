package com.commic.v1.services.book;

import com.commic.v1.dto.responses.BookResponse;
import com.commic.v1.entities.Book;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IBookService {
    List<Book> getAllBooks(Pageable pageable);

    BookResponse getBookByChapterId(Integer chapterId);

    BookResponse getDescription(Integer id);
}

