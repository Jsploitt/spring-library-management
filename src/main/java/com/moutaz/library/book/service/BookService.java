package com.moutaz.library.book.service;

import com.moutaz.library.book.entities.Book;
import com.moutaz.library.book.dto.BookRequest;

import java.util.List;

public interface BookService {

    Book addBook(BookRequest request);

    void deleteByIsbn(Integer isbn);

    Book findByIsbn(Integer isbn);

    List<Book> findAll();

    List<Book> findByGenre(String genre);

    long countByAuthor(String author);
}