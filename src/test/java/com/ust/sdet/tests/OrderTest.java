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
        assertEquals(4, repo.count());
    }

    @Test
    @Severity(SeverityLevel.TRIVIAL)
    @Story("Temporary skipped validation")
    @DisplayName("should be skipped for dashboard status visibility")
    void skippedForDashboardVisibility() {
        org.junit.jupiter.api.Assumptions.assumeTrue(false, "Intentional skip to show skipped status in Allure");
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Intentional failure")
    @DisplayName("should fail for dashboard failure category visibility")
    void failedForDashboardVisibility() {
        assertTrue(false, "Intentional failure to populate failed category in Allure");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Story("Intentional broken state")
    @DisplayName("should break for dashboard broken category visibility")
    void brokenForDashboardVisibility() {
        throw new IllegalStateException("Intentional broken state to populate broken category in Allure");
    }
}
 