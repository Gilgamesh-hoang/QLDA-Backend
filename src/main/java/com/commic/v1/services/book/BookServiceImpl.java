package com.commic.v1.services.book;

import com.commic.v1.dto.requests.BookRequest;
import com.commic.v1.dto.responses.APIResponse;
import com.commic.v1.dto.responses.BookResponse;
import com.commic.v1.entities.Book;
import com.commic.v1.entities.Category;
import com.commic.v1.mapper.BookMapper;
import com.commic.v1.repositories.IBookRepository;
import com.commic.v1.repositories.ICategoryRepository;
import com.commic.v1.repositories.IChapterRepository;
import com.commic.v1.services.chapter.IChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.*;

@Service
public class BookServiceImpl implements IBookService {
    @Autowired
    private IBookRepository bookRepository;
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private IChapterRepository chapterRepository;
    @Autowired
    private ICategoryRepository categoryRepository;
    @Autowired
    private IChapterService chapterService;
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

    @Override
    public APIResponse<Void> addBook(BookRequest bookRequest) {
        Book book = bookMapper.toBook(bookRequest);
        // check exist book name
        Example<Book> example = Example.of(Book.builder().name(book.getName()).build());
        if (bookRepository.exists(example)) {
            return new APIResponse<>(HttpStatus.CONFLICT.value(), "Book name already exists", null);
        }

        try {
            Set<Category> categories = categoryRepository.findByNameIn(bookRequest.getCategoryNames());
            book.setCategories(categories);
            book.setIsDeleted(false);
            book = bookRepository.save(book);
            // Add this book to each category's set of books
            for (Category category : categories) {
                category.getBooks().add(book);
            }

            categoryRepository.saveAll(categories);
            return new APIResponse<>(HttpStatus.NO_CONTENT.value(), "Add book successfully", null);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            return new APIResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Add book failed", null);
        }
    }

    @Override
    public APIResponse<Void> updateBook(BookRequest bookRequest) {
        // Find the book by its ID
        Book book = bookRepository.findById(bookRequest.getId()).orElse(null);

        // If the book doesn't exist, return a NOT_FOUND response
        if (book == null) {
            return new APIResponse<>(HttpStatus.NOT_FOUND.value(), "Book not found", null);
        }

        try {
            // Check if the book contains all the categories from the request
            boolean isContainsAll = new HashSet<>(book.getCategories().stream().map(Category::getName).toList()).containsAll(bookRequest.getCategoryNames());

            // If the book doesn't contain all the categories
            if (!isContainsAll) {
                // Remove this book from each category's set of books
                for (Category category : book.getCategories()) {
                    category.getBooks().remove(book);
                }
                // Save the updated categories
                categoryRepository.saveAll(book.getCategories());

                // Find the new categories from the request
                Set<Category> newCategories = categoryRepository.findByNameIn(bookRequest.getCategoryNames());

                // Add this book to each new category's set of books
                for (Category category : newCategories) {
                    category.getBooks().add(book);
                }
                // Save the new categories
                categoryRepository.saveAll(newCategories);
                // Set the new categories to the book
                book.setCategories(newCategories);
            }

            // Update the book's details
            book.setName(bookRequest.getName());
            book.setAuthor(bookRequest.getAuthor());
            book.setDescription(bookRequest.getDescription());
            book.setThumbnail(bookRequest.getThumbnail());
            book.setStatus(bookRequest.getStatus());

            // Save the updated book
            book = bookRepository.save(book);

            // Return a successful response
            return new APIResponse<>(HttpStatus.NO_CONTENT.value(), "Add book successfully", null);
        } catch (RuntimeException ex) {
            // If an exception occurs, print the stack trace and return an error response
            ex.printStackTrace();
            return new APIResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Add book failed", null);
        }
    }

    @Override
    public APIResponse<Void> deleteBook(Integer id) {
        // check exist book
        if (!bookRepository.existsById(id)) {
            return new APIResponse<>(HttpStatus.NOT_FOUND.value(), "Book not found", null);
        }

        try {
            Book book = bookRepository.findById(id).orElse(null);
            book.setIsDeleted(true);
            bookRepository.save(book);

            chapterService.deleteByBookId(id);

            return new APIResponse<>(HttpStatus.NO_CONTENT.value(), "Delete book successfully", null);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            return new APIResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Delete book failed", null);
        }

    }
}

