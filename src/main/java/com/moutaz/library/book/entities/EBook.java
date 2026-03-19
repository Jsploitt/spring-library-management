package com.moutaz.library.book.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "e_book")
@Data
public class EBook extends Book {

    @Column(name = "num_of_pages")
    private Integer numOfPages;

    @Column(name = "size_mb")
    private Integer sizeMb;
}