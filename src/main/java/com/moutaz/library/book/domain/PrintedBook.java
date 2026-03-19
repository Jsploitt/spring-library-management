package com.moutaz.library.book.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "printed_book")
public class PrintedBook extends Book {

    @Column(name = "num_of_pages")
    private Integer numOfPages;

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

    public Integer getNumOfPages()               { return numOfPages; }
    public void setNumOfPages(Integer numOfPages){ this.numOfPages = numOfPages; }

    public Boolean getHardcover()                { return hardcover; }
    public void setHardcover(Boolean hardcover)  { this.hardcover = hardcover; }
}