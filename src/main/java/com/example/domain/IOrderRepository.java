package com.example.domain;

import java.util.List;
// import java.util.Optional;

public interface IOrderRepository {
    public List<Order> allOrders();
    // public Optional<Order> getOrderById(Long id);
    // public Optional<Order> update(Order order);
    public void createOrder(Order order);
    // public boolean deleteOrderById(Long id);
    // public void deleteAllOrders();
}
