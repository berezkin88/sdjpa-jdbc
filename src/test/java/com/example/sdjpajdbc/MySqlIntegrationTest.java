package com.example.sdjpajdbc;

import com.example.sdjpajdbc.repositories.AuthorRepository;
import com.example.sdjpajdbc.repositories.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MySqlIntegrationTest {

    @Autowired
    BookRepository bookRepository;
    @Autowired
    AuthorRepository authorRepository;

    @Test
    void testInitScript() {
        var totalBooksCount = bookRepository.count();
        assertThat(totalBooksCount).isEqualTo(5);

        var totalAuthorCount = authorRepository.count();
        assertThat(totalAuthorCount).isEqualTo(3);
    }
}
