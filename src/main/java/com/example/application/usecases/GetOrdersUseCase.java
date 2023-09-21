package com.example.application.usecases;

import java.util.List;

import com.example.domain.Order;

public interface GetOrdersUseCase {
    List<Order> getOrders();
}
