package com.example.sdjpajdbc.dao;

import com.example.sdjpajdbc.domain.Author;

public interface AuthorDao {

    Author getById(Long id);

    Author getByFirstAndLastName(String firstName, String lastName);

    Author saveNewAuthor(Author author);
}
