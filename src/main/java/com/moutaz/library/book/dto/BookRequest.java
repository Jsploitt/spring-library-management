package com.moutaz.library.book.dto;

public class BookRequest {

    private Integer isbn;
    private String author;
    private String title;
    private String genre;
    private String bookType; // AUDIO, PRINTED, EBOOK
    private Integer duration;
    private String narrator;
    private Integer numOfPages;
    private Boolean hardcover;
    private Integer sizeMb;

    // Getters and setters for all fields
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