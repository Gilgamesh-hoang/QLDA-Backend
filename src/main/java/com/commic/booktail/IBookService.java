package com.commic.booktail;

import com.commic.v1.entities.Book;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IBookService {
    List<Book> getAllBooks(Pageable pageable);
    Book getBookById(Integer id);
}

