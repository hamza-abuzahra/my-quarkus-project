package com.example.application.usecases;

import com.example.domain.Order;

public interface CreateOrderUseCase {
    boolean createOrder(Order order);
}
