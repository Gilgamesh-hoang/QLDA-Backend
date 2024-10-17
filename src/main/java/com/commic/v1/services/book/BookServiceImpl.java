package com.commic.v1.services.book;

import com.commic.v1.dto.responses.BookResponse;
import com.commic.v1.entities.Book;
import com.commic.v1.mapper.BookMapper;
import com.commic.v1.repositories.IBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookServiceImpl implements IBookService {
    @Autowired
    private IBookRepository bookRepository;
    @Autowired
    private BookMapper bookMapper;
    @Override
    public List<Book> getAllBooks(Pageable pageable) {
        // Giả lập dữ liệu hoặc truy vấn từ cơ sở dữ liệu
        return new ArrayList<>(); // Trả về danh sách các truyện
    }

    @Override
    public BookResponse getBookByChapterId(Integer chapterId) {
        Book book = bookRepository.findBookByChapterId(chapterId);
        if (book != null) {
            return bookMapper.toBookResponseDTO(book);
        }
        return null;
    }
}

