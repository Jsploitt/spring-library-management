package com.moutaz.library.book.service;

import org.springframework.stereotype.Service;

@Service
public class BookUtilityServiceImpl implements BookUtilityService {

    @Override
    public boolean verifyIsbn(Integer isbn) {
        int n1 = isbn / 1000;
        int n2 = (isbn / 100) % 10;
        int n3 = (isbn / 10) % 10;
        int n4 = isbn % 10;
        return (n1 * 3 + n2 * 2 + n3 * 1) % 4 == n4;
    }

    @Override
    public String generateRefCode(String author, String genre) {
        if (author == null || genre == null || author.isBlank() || genre.isBlank()) {
            return null;
        }
        String authorPart = author.length() >= 2 ? author.substring(0, 2) : author;
        String genrePart = genre.length() >= 2 ? genre.substring(0, 2) : genre;
        return (authorPart + "-" + genrePart).toUpperCase();
    }
}
