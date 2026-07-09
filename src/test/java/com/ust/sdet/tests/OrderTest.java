package com.ust.sdet.tests;

import com.ust.sdet.factory.OrderFactory;
import com.ust.sdet.repository.OrderRepository;
import com.ust.sdet.support.AbstractPostgresIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.ust.sdet.builder.OrderBuilder.anOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderTest extends AbstractPostgresIntegrationTest {
    private OrderRepository repo;
    private OrderFactory factory;

    @BeforeEach
    void setup(){
        repo = new OrderRepository();
        factory = new OrderFactory(repo);
    }

    @Test
    void createsOrder() {
        long generatedId = factory.persisted(anOrder().withQty(3));
        assertEquals(1, repo.count());
        assertTrue(generatedId > 0);
    }

    @Test
    void countsOrders() {
        factory.persisted(anOrder().withSku("SKU-2"));
        assertEquals(1, repo.count());
    }
}
 