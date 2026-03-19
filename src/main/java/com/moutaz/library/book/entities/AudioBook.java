package com.moutaz.library.book.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "audio_book")
@Data
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
}