package com.moutaz.library.book.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "e_book")
public class EBook extends Book {

    @Column(name = "num_of_pages")
    private Integer numOfPages;

    @Column(name = "size_mb")
    private Integer sizeMb;

    public EBook() {}

    public EBook(Integer isbn, String author, String title,
                 String genre, Integer numOfPages, Integer sizeMb) {
        super(isbn, author, title, genre);
        this.numOfPages = numOfPages;
        this.sizeMb = sizeMb;
    }

    public Integer getNumOfPages()               { return numOfPages; }
    public void setNumOfPages(Integer numOfPages){ this.numOfPages = numOfPages; }

    public Integer getSizeMb()                   { return sizeMb; }
    public void setSizeMb(Integer sizeMb)        { this.sizeMb = sizeMb; }
}