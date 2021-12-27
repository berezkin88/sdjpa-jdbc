package com.example.sdjpajdbc.dao;

import com.example.sdjpajdbc.domain.Book;

public interface BookDao {

    Book save(Book book);
    Book update(Book book);
    Book getByTitle(String title);
    Book getById(Long id);
    void deleteById(Long id);
}
