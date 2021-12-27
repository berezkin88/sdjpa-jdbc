package com.example.sdjpajdbc.dao;

import com.example.sdjpajdbc.domain.Book;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("local")
@DataJpaTest
@ComponentScan(basePackages = {"com/example/sdjpajdbc/dao"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookDaoImplTest {

    @Autowired
    BookDao bookDao;

    @Test
    void save() {
        var book = new Book();
        book.setIsbn("123456");
        book.setPublisher("Publisher");
        book.setTitle("Test book");

        var savedBook = bookDao.save(book);

        assertThat(savedBook).isNotNull();
    }

    @Test
    void update() {
        var book = new Book();
        book.setIsbn("123456");
        book.setPublisher("Publisher");
        book.setTitle("T");
        book.setAuthorId(1L);

        var savedBook = bookDao.save(book);

        savedBook.setTitle("Test book");

        var updatedBook = bookDao.update(savedBook);

        assertThat(updatedBook)
            .isNotNull()
            .extracting(Book::getTitle)
            .isEqualTo("Test book");
    }

    @Test
    void getByTitle() {
        var book = new Book();
        book.setIsbn("123456");
        book.setPublisher("Publisher");
        book.setTitle("Test book");

        var savedBook = bookDao.save(book);

        assertThat(savedBook).isNotNull();

        var found = bookDao.getByTitle(savedBook.getTitle());

        assertThat(found)
            .isNotNull()
            .usingRecursiveComparison()
            .ignoringFields("id", "authorId") // db is not cleaned up after tests
            .isEqualTo(savedBook);
    }

    @Test
    void getById() {
        var found = bookDao.getById(1L);

        assertThat(found).isNotNull();
    }

    @Test
    void deleteById() {
        var book = new Book();
        book.setIsbn("123456");
        book.setPublisher("Publisher");
        book.setTitle("Test book");
        var savedBook = bookDao.save(book);

        bookDao.deleteById(savedBook.getId());

        var found = bookDao.getById(savedBook.getId());

        assertThat(found).isNull();
    }
}