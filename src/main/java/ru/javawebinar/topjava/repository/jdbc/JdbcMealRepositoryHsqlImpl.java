package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Repository
@Profile({"hsqldb"})
public class JdbcMealRepositoryHsqlImpl extends AbstractJdbcMealRepository {

    private final DateTimeFormatter HSQL_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public JdbcMealRepositoryHsqlImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Override
    protected String getMealDate(LocalDateTime dateTime) {
        return dateTime.format(HSQL_DATE_FORMATTER);
    }
}
