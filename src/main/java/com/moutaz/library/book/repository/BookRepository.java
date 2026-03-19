package com.moutaz.library.book.repository;

import com.moutaz.library.book.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(Integer isbn);

    List<Book> findByAuthorIgnoreCase(String author);

    List<Book> findByGenreIgnoreCase(String genre);

    boolean existsByIsbn(Integer isbn);
}