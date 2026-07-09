package com.ust.sdet.support;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ust.sdet.factory.OrderFactory;
import com.ust.sdet.repository.OrderRepository;
import org.junit.jupiter.api.Test;

import static com.ust.sdet.builder.OrderBuilder.anOrder;

class PostgresFlywayInfrastructureTest extends AbstractPostgresIntegrationTest {

    @Test
    void shouldUseContainerCredentialsAndApplyMigrations() {
        assertNotNull(jdbcUrl());
        assertNotNull(username());
        assertNotNull(password());

        OrderRepository repository = new OrderRepository();
        OrderFactory factory = new OrderFactory(repository);

        long generatedId = factory.persisted(anOrder().withQty(2));

        assertTrue(generatedId > 0);
        assertEquals(1L, repository.count());
    }
}
