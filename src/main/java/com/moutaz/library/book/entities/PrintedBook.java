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
}