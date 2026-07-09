package com.ust.sdet.repository;

import com.ust.sdet.model.Order;
import com.ust.sdet.support.AbstractPostgresIntegrationTest;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

public class OrderRepository {
    private static final String INSERT_SQL = "INSERT INTO orders (sku, qty, price, order_date, shipped) VALUES (?, ?, ?, ?, ?)";
    private static final String COUNT_SQL = "SELECT COUNT(*) FROM orders";
    private static final String RESET_SQL = "DELETE FROM orders";

    private final DataSource dataSource;

    public OrderRepository() {
        this(AbstractPostgresIntegrationTest.createDataSource());
    }

    public OrderRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public long save(Order order) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, order.sku());
            statement.setInt(2, order.qty());
            statement.setDouble(3, order.price());
            statement.setDate(4, Date.valueOf(order.orderDate()));
            statement.setBoolean(5, order.shipped());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                }
            }
            throw new IllegalStateException("No generated id returned for saved order");
        } catch (SQLException exception) {
            throw new IllegalStateException("Unable to save order", exception);
        }
    }

    public long count() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(COUNT_SQL);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getLong(1);
            }
            return 0L;
        } catch (SQLException exception) {
            throw new IllegalStateException("Unable to count orders", exception);
        }
    }

    public void reset() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(RESET_SQL)) {
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("Unable to reset orders", exception);
        }
    }
}