package com.ust.sdet.tests;

import com.ust.sdet.factory.OrderFactory;
import com.ust.sdet.repository.OrderRepository;
import com.ust.sdet.support.AbstractPostgresIntegrationTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.ust.sdet.builder.OrderBuilder.anOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("Order Management")
@Feature("Repository Persistence")
@Story("Persist and count orders")
@Owner("QA Team")
public class OrderTest extends AbstractPostgresIntegrationTest {
    private OrderRepository repo;
    private OrderFactory factory;

    @BeforeEach
    void setup() {
        repo = new OrderRepository();
        factory = new OrderFactory(repo);
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Create and persist an order")
    @DisplayName("should persist an order with default values")
    void createsOrder() {
        long generatedId = factory.persisted(anOrder().withQty(3));
        assertEquals(1, repo.count());
        assertTrue(generatedId > 0);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Story("Count stored orders")
    @DisplayName("should count persisted orders")
    void countsOrders() {
        factory.persisted(anOrder().withSku("SKU-2"));
        assertEquals(1, repo.count());
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Story("Create multiple orders")
    @DisplayName("should persist and count multiple orders")
    void countsMultipleOrders() {
        factory.persisted(anOrder().withSku("SKU-3").withQty(2));
        factory.persisted(anOrder().withSku("SKU-4").withQty(4));
        assertEquals(2, repo.count());
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Persist custom order data")
    @DisplayName("should persist an order with custom attributes")
    void createsOrderWithCustomAttributes() {
        factory.persisted(anOrder().withSku("SKU-9").withQty(7).withPrice(199.99).shipped());
        assertEquals(1, repo.count());
    }
}
 