package com.example.sdjpajdbc.dao;

import com.example.sdjpajdbc.domain.Book;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class BookDaoImpl implements BookDao {

    private final DataSource dataSource;
    private final AuthorDao authorDao;

    public BookDaoImpl(DataSource dataSource, AuthorDao authorDao) {
        this.dataSource = dataSource;
        this.authorDao = authorDao;
    }

    @Override
    public Book save(Book book) {
        ResultSet resultSet = null;

        try (var connection = dataSource.getConnection();
             var preparedStatement = connection.prepareStatement("insert into book (isbn, publisher, title, author_id) values (?, ?, ?, ?)");
             var statement = connection.createStatement();
        ) {
            setBookFields(book, preparedStatement);
            preparedStatement.execute();

            resultSet = statement.executeQuery("select LAST_INSERT_ID()");

            if (resultSet.next()) {
                var savedId = resultSet.getLong(1);
                return this.getById(savedId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResultSet(resultSet);
        }

        return null;
    }

    private Book getBookFromResultSet(ResultSet resultSet) throws SQLException {
        var book = new Book();
        book.setId(resultSet.getLong("id"));
        book.setTitle(resultSet.getString("title"));
        book.setPublisher(resultSet.getString("publisher"));
        book.setIsbn(resultSet.getString("isbn"));
        book.setAuthor(authorDao.getById(resultSet.getLong("author_id")));
        return book;
    }

    private void closeResultSet(ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public Book update(Book book) {
        try (var connection = dataSource.getConnection();
             var preparedStatement = connection.prepareStatement("update book set isbn = ?, publisher = ?, title = ?, author_id = ? where book.id = ?");
        ) {
            setBookFields(book, preparedStatement);
            preparedStatement.setLong(5, book.getId());
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return this.getById(book.getId());
    }

    private void setBookFields(Book book, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, book.getIsbn());
        preparedStatement.setString(2, book.getPublisher());
        preparedStatement.setString(3, book.getTitle());
        if (book.getAuthor() != null) {
            preparedStatement.setLong(4, book.getAuthor().getId());
        } else {
            preparedStatement.setNull(4, -5);
        }
    }

    @Override
    public Book getByTitle(String title) {
        ResultSet resultSet = null;

        try (var connection = dataSource.getConnection();
             var preparedStatement = connection.prepareStatement("select * from book where title = ?");
        ) {
            preparedStatement.setString(1, title);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return getBookFromResultSet(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResultSet(resultSet);
        }

        return null;
    }

    @Override
    public Book getById(Long id) {
        ResultSet resultSet = null;

        try (var connection = dataSource.getConnection();
             var preparedStatement = connection.prepareStatement("select * from book where id = ?");
        ) {
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return getBookFromResultSet(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResultSet(resultSet);
        }

        return null;
    }

    @Override
    public void deleteById(Long id) {
        try (var connection = dataSource.getConnection();
             var preparedStatement = connection.prepareStatement("delete from book where book.id = ?");
        ) {
            preparedStatement.setLong(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
