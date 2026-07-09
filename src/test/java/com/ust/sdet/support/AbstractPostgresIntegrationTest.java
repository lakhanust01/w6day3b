package com.ust.sdet.support;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.postgresql.ds.PGSimpleDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class AbstractPostgresIntegrationTest {

    @Container
    protected static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine");

    @BeforeAll
    static void migrateDatabase() {
        Flyway.configure()
                .dataSource(createDataSource())
                .locations("classpath:db/migration")
                .load()
                .migrate();
    }

    @BeforeEach
    void resetDatabase() {
        try (Connection connection = createDataSource().getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("TRUNCATE TABLE public.orders RESTART IDENTITY CASCADE");
        } catch (SQLException exception) {
            throw new IllegalStateException("Unable to reset mutable database tables", exception);
        }
    }

    public static DataSource createDataSource() {
        if (!POSTGRES.isRunning()) {
            POSTGRES.start();
        }

        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(POSTGRES.getJdbcUrl());
        dataSource.setUser(POSTGRES.getUsername());
        dataSource.setPassword(POSTGRES.getPassword());
        return dataSource;
    }

    protected static String jdbcUrl() {
        return POSTGRES.getJdbcUrl();
    }

    protected static String username() {
        return POSTGRES.getUsername();
    }

    protected static String password() {
        return POSTGRES.getPassword();
    }
}
