package com.moutaz.library.book.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class BookRequest {

    @NotNull(message = "ISBN is required")
    private Integer isbn;

    @NotBlank(message = "Author is required")
    private String author;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Genre is required")
    private String genre;

    @NotBlank(message = "bookType is required (AUDIO, PRINTED, EBOOK)")
    private String bookType;

    // Subtype-specific fields — no validation here,
    // service enforces which fields are required per type
    private Integer duration;
    private String narrator;
    private Integer numOfPages;
    private Boolean hardcover;
    private Integer sizeMb;

    // Getters and setters
    public Integer getIsbn()                    { return isbn; }
    public void setIsbn(Integer isbn)           { this.isbn = isbn; }

    public String getAuthor()                   { return author; }
    public void setAuthor(String author)        { this.author = author; }

    public String getTitle()                    { return title; }
    public void setTitle(String title)          { this.title = title; }

    public String getGenre()                    { return genre; }
    public void setGenre(String genre)          { this.genre = genre; }

    public String getBookType()                 { return bookType; }
    public void setBookType(String bookType)    { this.bookType = bookType; }

    public Integer getDuration()                { return duration; }
    public void setDuration(Integer duration)   { this.duration = duration; }

    public String getNarrator()                 { return narrator; }
    public void setNarrator(String narrator)    { this.narrator = narrator; }

    public Integer getNumOfPages()              { return numOfPages; }
    public void setNumOfPages(Integer n)        { this.numOfPages = n; }

    public Boolean getHardcover()               { return hardcover; }
    public void setHardcover(Boolean hardcover) { this.hardcover = hardcover; }

    public Integer getSizeMb()                  { return sizeMb; }
    public void setSizeMb(Integer sizeMb)       { this.sizeMb = sizeMb; }
}