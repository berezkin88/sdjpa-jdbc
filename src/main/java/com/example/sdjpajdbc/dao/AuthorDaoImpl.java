package com.example.sdjpajdbc.dao;

import com.example.sdjpajdbc.domain.Author;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class AuthorDaoImpl implements AuthorDao {

    private final DataSource dataSource;

    public AuthorDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Author getById(Long id) {
        ResultSet resultSet = null;

        try (var connection = dataSource.getConnection();
             var preparedStatement = connection.prepareStatement("select * from author where id = ?");
        ) {
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return getAuthorFromResultSet(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResultSet(resultSet);
        }

        return null;
    }

    @Override
    public Author getByFirstAndLastName(String firstName, String lastName) {
        ResultSet resultSet = null;

        try (var connection = dataSource.getConnection();
             var preparedStatement = connection.prepareStatement("select * from author where first_name = ? and last_name = ?");
        ) {
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return getAuthorFromResultSet(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResultSet(resultSet);
        }

        return null;
    }

    private Author getAuthorFromResultSet(ResultSet resultSet) throws SQLException {
        var author = new Author();
        author.setId(resultSet.getLong("id"));
        author.setFirstName(resultSet.getString("first_name"));
        author.setLastName(resultSet.getString("last_name"));

        return author;
    }

    private void closeResultSet(ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public Author saveNewAuthor(Author author) {
        ResultSet resultSet = null;

        try (var connection = dataSource.getConnection();
             var preparedStatement = connection.prepareStatement("insert into author (first_name, last_name) values (?, ?)");
             var statement = connection.createStatement();
        ) {
            preparedStatement.setString(1, author.getFirstName());
            preparedStatement.setString(2, author.getLastName());
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
}
