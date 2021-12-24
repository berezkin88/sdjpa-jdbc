package com.example.sdjpajdbc.dao;

import com.example.sdjpajdbc.domain.Author;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("local")
@DataJpaTest
@ComponentScan(basePackages = {"com/example/sdjpajdbc/dao"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuthorDaoImplTest {

    @Autowired
    AuthorDao authorDao;

    @Test
    void testGetAuthorById() {
        var author = authorDao.getById(1L);

        assertThat(author).isNotNull();
    }

    @Test
    void authorDao_findByFirstAndLastNames() {
        var firstName = "Craig";
        var lastName = "Walls";
        var author = authorDao.getByFirstAndLastName(firstName, lastName);

        assertThat(author)
            .isNotNull();
    }

    @Test
    void authorDao_saveNewAuthor() {
        var author = new Author();
        author.setLastName("Berezkin");
        author.setFirstName("Oleksandr");
        var savedAuthor = authorDao.saveNewAuthor(author);

        assertThat(savedAuthor).isNotNull();
    }

    @Test
    void authorDao_updateAuthor() {
        var author = new Author();
        author.setLastName("B");
        author.setFirstName("Oleksandr");
        var savedAuthor = authorDao.saveNewAuthor(author);

        savedAuthor.setLastName("Berezkin");
        var updatedAuthor = authorDao.updateAuthor(savedAuthor);

        assertThat(updatedAuthor.getLastName()).isEqualTo("Berezkin");
    }

    @Test
    void authorDao_deleteAuthorById() {
        var author = new Author();
        author.setLastName("B");
        author.setFirstName("Oleksandr");
        var savedAuthor = authorDao.saveNewAuthor(author);

        authorDao.deleteAuthorById(savedAuthor.getId());

        var deletedAuthor = authorDao.getById(savedAuthor.getId());
        assertThat(deletedAuthor).isNull();
    }
}