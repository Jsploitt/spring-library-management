package com.moutaz.library.book.service;

public interface BookUtilityService {

    boolean verifyIsbn(Integer isbn);

    String generateRefCode(String author, String genre);
}
