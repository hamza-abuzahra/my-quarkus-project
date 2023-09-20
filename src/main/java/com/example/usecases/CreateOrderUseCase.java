package com.example.usecases;

import com.example.domain.Order;

public interface CreateOrderUseCase {
    boolean createOrder(Order order);
}
