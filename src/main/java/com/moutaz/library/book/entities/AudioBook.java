package com.moutaz.library.book.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "audio_book")
@Data
public class AudioBook extends Book {

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "narrator")
    private String narrator;
}