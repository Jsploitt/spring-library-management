package com.moutaz.library.book.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "book")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "book_type")
@Data
public abstract class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Integer isbn;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String genre;

    @Column(name = "ref_code")
    private String refCode;

    // ── Constructors ──────────────────────────────────────────
    public Book() {}

    public Book(Integer isbn, String author, String title, String genre) {
        this.isbn = isbn;
        this.author = author;
        this.title = title;
        this.genre = genre;
        this.refCode = generateReference();
    }

    // ── Business Logic ────────────────────────────────────────
    private String generateReference() {
        String authorPart = author.substring(0, 2).toUpperCase();
        String genrePart  = genre.substring(0, 2).toUpperCase();
        return authorPart + "-" + genrePart;
    }

    public boolean verifyISBN(Integer isbn) {
        int n1 = isbn / 1000;
        int n2 = (isbn / 100) % 10;
        int n3 = (isbn / 10) % 10;
        int n4 = isbn % 10;
        return (n1 * 3 + n2 * 2 + n3 * 1) % 4 == n4;
    }
}