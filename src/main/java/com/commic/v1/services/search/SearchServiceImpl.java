/**
 * Class: SearchServiceImpl
 *
 * @author ducvui2003
 * @created 16/10/24
 */
package com.commic.v1.services.search;

import com.commic.v1.dto.DataListResponse;
import com.commic.v1.dto.responses.BookResponse;
import com.commic.v1.entities.Book;
import com.commic.v1.exception.AppException;
import com.commic.v1.exception.ErrorCode;
import com.commic.v1.mapper.BookMapper;
import com.commic.v1.repositories.IBookRepository;
import com.commic.v1.repositories.IChapterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SearchServiceImpl implements ISearchService {
    @Autowired
    private IBookRepository bookRepository;
    @Autowired
    private IChapterRepository chapterRepository;
    @Autowired
    private BookMapper bookMapper;

    @Override
    public DataListResponse<BookResponse> getRankBy(String type, Pageable pageable) {
        DataListResponse<BookResponse> result = new DataListResponse<>();
        Page<Book> page;
        switch (type.toUpperCase()) {
            case "RATING" -> page = bookRepository.findAllOrderByRatingDesc(pageable);

            case "VIEW" -> page = bookRepository.findAllOrderByViewDesc(pageable);

            case "NEW" -> page = bookRepository.findByPublishDateOrderByNearestDate(pageable);

            default -> throw new AppException(ErrorCode.PARAMETER_NOT_VALID);
        }
        if (page.isEmpty()) throw new AppException(ErrorCode.NOT_FOUND);
        List<Book> books = page.getContent();
        List<BookResponse> data = bookToResponseDTO(books);
        result.setCurrentPage(pageable.getPageNumber() + 1);
        result.setTotalPages(page.getTotalPages());
        result.setData(data);
        return result;
    }

    @Override
    public DataListResponse<BookResponse> getRankBy(String type, Integer categoryId, Pageable pageable) {
        DataListResponse<BookResponse> result = new DataListResponse<>();
        Page<Book> page;
        switch (type.toUpperCase()) {
            case "RATING" -> page = bookRepository.findAllOrderByRatingDesc(pageable);

            case "VIEW" -> page = bookRepository.findAllOrderByViewDesc(pageable);

            case "NEW" -> page = bookRepository.findByPublishDateOrderByNearestDate(pageable);

            default -> throw new AppException(ErrorCode.PARAMETER_NOT_VALID);
        }
        if (page.isEmpty()) throw new AppException(ErrorCode.NOT_FOUND);
        List<Book> books = page.getContent();
        List<BookResponse> data = bookToResponseDTO(books);
        result.setCurrentPage(pageable.getPageNumber() + 1);
        result.setTotalPages(page.getTotalPages());
        result.setData(data);
        return result;
    }

    private List<BookResponse> bookToResponseDTO(List<Book> books) {
        List<BookResponse> result = new ArrayList<>();
        for (Book book : books) {
            Double starAvg = Optional.ofNullable(chapterRepository.countStarAvgByBookId(book.getId())).orElse(0.0);
            Integer views = Optional.ofNullable(chapterRepository.countViewByBookId(book.getId())).orElse(0);
            Integer quantityChapter = Optional.ofNullable(chapterRepository.countByBookId(book.getId())).orElse(0);
            BookResponse bookResponseDTO = bookMapper.toBookResponseDTO(book);
            bookResponseDTO.setRating(starAvg);
            bookResponseDTO.setView(views);
            bookResponseDTO.setQuantityChapter(quantityChapter);
            bookResponseDTO.setPublishDate(chapterRepository.findFirstPublishDateByBookId(book.getId()));
            List<String> categories = bookRepository.findCategoryNamesByBookId(book.getId());
            bookResponseDTO.setCategoryNames(categories);
            result.add(bookResponseDTO);
        }
        return result;
    }

    @Override
    public DataListResponse<BookResponse> getComicByPublishDate(Pageable pageable) {
        DataListResponse<BookResponse> result = new DataListResponse<>();
        Page<Book> page;
        page = bookRepository.findByPublishDateOrderByNearestDate(pageable);
        if (page.isEmpty()) throw new AppException(ErrorCode.NOT_FOUND);
        List<Book> books = page.getContent();
        List<BookResponse> data = bookToResponseDTO(books);
        result.setCurrentPage(pageable.getPageNumber() + 1);
        result.setTotalPages(page.getTotalPages());
        result.setData(data);
        return result;
    }

    @Override
    public DataListResponse<BookResponse> getComicByPublishDate(Pageable pageable, Integer categoryId) {
        DataListResponse<BookResponse> result = new DataListResponse<>();
        Page<Book> page;
        page = bookRepository.findByPublishDateOrderByNearestDate(pageable, categoryId);
        List<Book> books = page.getContent();
        if (books.isEmpty()) throw new AppException(ErrorCode.NOT_FOUND);
        List<BookResponse> data = bookToResponseDTO(books);
        result.setCurrentPage(pageable.getPageNumber() + 1);
        result.setTotalPages(page.getTotalPages());
        result.setData(data);
        return result;
    }
}
