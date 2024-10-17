package com.commic.v1.services.book;

import com.commic.v1.dto.responses.BookResponse;
import com.commic.v1.entities.Book;
import com.commic.v1.mapper.BookMapper;
import com.commic.v1.repositories.IBookRepository;
import com.commic.v1.repositories.IChapterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements IBookService {
    @Autowired
    private IBookRepository bookRepository;
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private IChapterRepository chapterRepository;

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

    @Override
    public BookResponse getDescription(Integer id) {
        try {
            Book book = bookRepository.findById(id).get();
            if (book != null) {
                Integer quantityChapter = Optional.ofNullable(chapterRepository.countByBookId(book.getId())).orElse(0);
                Integer views = Optional.ofNullable(chapterRepository.countViewByBookId(book.getId())).orElse(0);
                Double starAvg = Optional.ofNullable(chapterRepository.countStarAvgByBookId(book.getId())).orElse(0.0);
                Date publishDate = chapterRepository.findFirstPublishDateByBookId(book.getId());
                List<String> categories = bookRepository.findCategoryNamesByBookId(book.getId());
                BookResponse bookResponseDTO = bookMapper.toBookResponseDTO(book);
                bookResponseDTO.setQuantityChapter(quantityChapter);
                bookResponseDTO.setView(views);
                bookResponseDTO.setRating(starAvg);
                bookResponseDTO.setCategoryNames(categories);
                bookResponseDTO.setPublishDate(publishDate);
                return bookResponseDTO;
            } else {
                return null;
            }
        } catch (DataAccessException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}

