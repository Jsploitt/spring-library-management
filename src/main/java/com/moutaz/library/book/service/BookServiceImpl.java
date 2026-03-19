package com.moutaz.library.book.service;

import com.moutaz.library.book.entities.*;
import com.moutaz.library.book.dto.BookRequest;
import com.moutaz.library.book.repository.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookUtilityService bookUtilityService;

    public BookServiceImpl(BookRepository bookRepository, BookUtilityService bookUtilityService) {
        this.bookRepository = bookRepository;
        this.bookUtilityService = bookUtilityService;
    }

    @Override
    public Book addBook(BookRequest request) {
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Book with ISBN " + request.getIsbn() + " already exists");
        }

        Book book = switch (request.getBookType().toUpperCase()) {
            case "AUDIO" -> {
                AudioBook audioBook = new AudioBook();
                populateCommonFields(audioBook, request);
                audioBook.setDuration(request.getDuration());
                audioBook.setNarrator(request.getNarrator());
                yield audioBook;
            }
            case "PRINTED" -> {
                PrintedBook printedBook = new PrintedBook();
                populateCommonFields(printedBook, request);
                printedBook.setNumOfPages(request.getNumOfPages());
                printedBook.setHardcover(request.getHardcover() != null && request.getHardcover());
                yield printedBook;
            }
            case "EBOOK" -> {
                EBook eBook = new EBook();
                populateCommonFields(eBook, request);
                eBook.setNumOfPages(request.getNumOfPages());
                eBook.setSizeMb(request.getSizeMb());
                yield eBook;
            }
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid book type. Must be AUDIO, PRINTED, or EBOOK");
        };

        return bookRepository.save(book);
    }

    @Override
    public void deleteByIsbn(Integer isbn) {
        Book book = findByIsbn(isbn);
        bookRepository.delete(book);
    }

    @Override
    public Book findByIsbn(Integer isbn) {
        return bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Book with ISBN " + isbn + " not found"));
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public List<Book> findByGenre(String genre) {
        return bookRepository.findByGenreIgnoreCase(genre);
    }

    @Override
    public long countByAuthor(String author) {
        return bookRepository.findByAuthorIgnoreCase(author).size();
    }

    private void populateCommonFields(Book book, BookRequest request) {
        book.setIsbn(request.getIsbn());
        book.setAuthor(request.getAuthor());
        book.setTitle(request.getTitle());
        book.setGenre(request.getGenre());
        book.setRefCode(bookUtilityService.generateRefCode(request.getAuthor(), request.getGenre()));
    }
}