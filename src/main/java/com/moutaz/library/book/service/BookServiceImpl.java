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

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book addBook(BookRequest request) {
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Book with ISBN " + request.getIsbn() + " already exists");
        }

        Book book = switch (request.getBookType().toUpperCase()) {
            case "AUDIO" -> new AudioBook(
                    request.getIsbn(), request.getAuthor(), request.getTitle(),
                    request.getGenre(), request.getDuration(), request.getNarrator());
            case "PRINTED" -> new PrintedBook(
                    request.getIsbn(), request.getAuthor(), request.getTitle(),
                    request.getGenre(), request.getNumOfPages(),
                    request.getHardcover() != null && request.getHardcover());
            case "EBOOK" -> new EBook(
                    request.getIsbn(), request.getAuthor(), request.getTitle(),
                    request.getGenre(), request.getNumOfPages(), request.getSizeMb());
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
}