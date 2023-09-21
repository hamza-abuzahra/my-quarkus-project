package com.example.application.usecases.order;

import com.example.domain.Order;

public interface CreateOrderUseCase {
    boolean createOrder(Order order);
}
