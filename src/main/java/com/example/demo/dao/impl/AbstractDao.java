package com.example.demo.dao.impl;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

abstract class AbstractDao extends NamedParameterJdbcDaoSupport {
    NamedParameterJdbcTemplate jdbcTemplate;

    @Resource
    private DataSource dataSource;

    static <T extends Enum<T>> T toEnum(String value, Class<T> clazz) {
        return Optional.ofNullable(value).map(s -> Enum.valueOf(clazz, s)).orElse(null);
    }

    static LocalDateTime toLocalDateTime(Timestamp value) {
        return Optional.ofNullable(value).map(Timestamp::toLocalDateTime).orElse(null);
    }

    String toString(Enum value) {
        return Optional.ofNullable(value).map(Enum::name).orElse(null);
    }

    @PostConstruct
    private void init() {
        this.setDataSource(dataSource);
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }
}
