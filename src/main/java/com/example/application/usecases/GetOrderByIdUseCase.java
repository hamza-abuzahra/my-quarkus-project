package com.example.application.usecases;

import java.util.Optional;

import com.example.domain.Order;

public interface GetOrderByIdUseCase {
    Optional<Order> getOrderById(Long id);
}
