package com.commic.booktail;

import com.commic.v1.entities.Book;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookServiceImpl implements IBookService {
    
    @Override
    public List<Book> getAllBooks(Pageable pageable) {
        // Giả lập dữ liệu hoặc truy vấn từ cơ sở dữ liệu
        return new ArrayList<>(); // Trả về danh sách các truyện
    }

    @Override
    public Book getBookById(Integer id) {
        // Giả lập dữ liệu hoặc truy vấn từ cơ sở dữ liệu
        Book book = new Book();
        book.setId(id);
        book.setName("Example Book Name");
        book.setAuthor("Author Name");
        book.setDescription("Book description goes here.");
        book.setStatus("UNLUCKY");
        return book;
    }
}

