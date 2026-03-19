package com.moutaz.library.book.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "printed_book")
@Data
public class PrintedBook extends Book {

    @Column(name = "num_of_pages")
    private Integer numOfPages;

    @Column(name = "hardcover")
    private Boolean hardcover;

    public PrintedBook() {}

    public PrintedBook(Integer isbn, String author, String title,
                       String genre, Integer numOfPages, Boolean hardcover) {
        super(isbn, author, title, genre);
        this.numOfPages = numOfPages;
        this.hardcover = hardcover;
    }

    // Convenience constructor — hardcover defaults to false
    public PrintedBook(Integer isbn, String author, String title,
                       String genre, Integer numOfPages) {
        this(isbn, author, title, genre, numOfPages, false);
    }

    public String getCoverType() {
        return hardcover ? "hardcover" : "paperback";
    }
}