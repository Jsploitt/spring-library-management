package com.moutaz.library.book.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
}