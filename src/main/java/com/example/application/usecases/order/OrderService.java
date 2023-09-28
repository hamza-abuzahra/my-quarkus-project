package com.example.application.usecases.order;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;


import com.example.domain.IOrderRepository;
import com.example.domain.Order;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class OrderService implements CreateOrderUseCase, GetOrdersUseCase, GetOrderByIdUseCase, OrderCountUseCase{

    @Inject
    private IOrderRepository orderRepository;

    Logger logger = Logger.getLogger(OrderService.class.getName());

    @Override
    public List<Order> getOrders(int offset, int size) {
        return orderRepository.allOrders(offset, size);
    }

    @Override
    public int orderCount() {
        return orderRepository.allOrderCount();
    }

    @Override
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.getOrderById(id);
    }

    @Override
    @Transactional
    public boolean createOrder(Order order) {
        try {
            orderRepository.createOrder(order);
            return true;
        } catch (Exception e){
            return false;
        }
    }
}