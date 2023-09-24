package com.example.application.usecases.order;

import java.util.List;
import java.util.Optional;

import com.example.domain.IOrderRepository;
import com.example.domain.IProductRepository;
import com.example.domain.IUserRepository;
import com.example.domain.Order;
import com.example.domain.User;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class OrderService implements CreateOrderUseCase, GetOrdersUseCase, GetOrderByIdUseCase, OrderCountUseCase{

    @Inject
    private IOrderRepository orderRepository;
    @Inject
    private IProductRepository productRepository;
    @Inject
    private IUserRepository userRepository;

    @Override
    @Transactional
    public boolean createOrder(Order order) {
        // check if user exists
        Optional<User> user = userRepository.getUserById(order.getUserId());
        if (user.isEmpty()){
            return false; 
        }
        // check if added products exist
        for (Long productId : order.getProductId()){
            if (productRepository.getProductById(productId).isEmpty()){
                return false;
            }
        }
        orderRepository.createOrder(order);
        return true;
    }

    @Override
    public List<Order> getOrders(int offset, int size) {
        return orderRepository.allOrders(offset, size);
    }

    @Override
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.getOrderById(id);
    }

    @Override
    public int orderCount() {
        return orderRepository.allOrderCount();
    }
    
}
