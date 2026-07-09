package com.ust.sdet.factory;

import com.ust.sdet.builder.OrderBuilder;
import com.ust.sdet.repository.OrderRepository;

public class OrderFactory {
    private final OrderRepository repo;

    public OrderFactory(OrderRepository repo) {
        this.repo = repo;
    }

    public long persisted(OrderBuilder builder) {
        return repo.save(builder.build());
    }
}
 