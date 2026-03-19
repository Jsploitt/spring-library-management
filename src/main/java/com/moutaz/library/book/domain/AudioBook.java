package com.moutaz.library.book.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "audio_book")
public class AudioBook extends Book {

    private Integer duration;
    private String narrator;

    public AudioBook() {}

    public AudioBook(Integer isbn, String author, String title,
                     String genre, Integer duration, String narrator) {
        super(isbn, author, title, genre);
        this.duration = duration;
        this.narrator = narrator;
    }

    public Integer getDuration()             { return duration; }
    public void setDuration(Integer duration){ this.duration = duration; }

    public String getNarrator()              { return narrator; }
    public void setNarrator(String narrator) { this.narrator = narrator; }
}