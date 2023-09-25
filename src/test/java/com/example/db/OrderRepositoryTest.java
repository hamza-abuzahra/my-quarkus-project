package com.example.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import com.example.domain.IOrderRepository;
import com.example.domain.Order;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.transaction.annotations.Rollback;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@QuarkusTest
@Transactional
@Rollback(true)
public class OrderRepositoryTest {
    
    @Inject
    private IOrderRepository orderRepository;

    Logger logger = Logger.getLogger(OrderRepositoryTest.class.getName());

    @AfterEach
    @Transactional
    void tearDown() {
        orderRepository.deleteAllOrders();
    }
    
    @Test
    @Transactional
    public void testGetAllOrders() {
        // given
        Order order = new Order(1L, List.of(1L, 3L));
        Order order2 = new Order(1L, List.of(4L, 3L));
        Order order3 = new Order(2L, List.of(1L));
        orderRepository.createOrder(order);        
        orderRepository.createOrder(order2);
        orderRepository.createOrder(order3);

        // when
        List<Order> res = orderRepository.allOrders(0, 2);

        // then
        assertEquals(res.get(0).getUserId(), 1L);
        assertEquals(res.get(1).getUserId(), 1L);
        assertEquals(res.size(), 2);
    }

    @Test
    public void testGetAllOrdersEmpty() {
        // given
        // empty database, no entries made beforehand 
        
        // when 
        List<Order> res = orderRepository.allOrders(0, 2);

        // then
        assertEquals(0, res.size());
    }

    @Test 
    @Transactional
    public void testGetOrderCount() {
        // given
        Order order = new Order(1L, List.of(1L, 3L));
        Order order2 = new Order(1L, List.of(4L, 3L));
        Order order3 = new Order(2L, List.of(1L));
        orderRepository.createOrder(order);        
        orderRepository.createOrder(order2);
        orderRepository.createOrder(order3);

        // when 
        int orderCount = orderRepository.allOrderCount();

        // then
        assertEquals(3, orderCount);
    }

    @Test
    public void testGetOrderCountEmpty() {
        // given
        // when 
        int orderCount = orderRepository.allOrderCount();

        // then
        assertEquals(0, orderCount);
    }

    @Test
    @Transactional
    public void testGetOrderByIdOk() {
        // given
        Order order = new Order(1L, List.of(1L, 3L));
        orderRepository.createOrder(order);
        
        // when 
        Optional<Order> resOptional = orderRepository.getOrderById(5L);

        // then
        assertFalse(resOptional.isEmpty());
        Order order2 = resOptional.get();
        assertEquals(order2.getUserId(), 1L);
    }

    @Test
    public void testGetOrderByIdNotExists() {
        // when
        Optional<Order> resOptional = orderRepository.getOrderById(1L);

        // then
        assertTrue(resOptional.isEmpty());
    }

    @Test
    @Transactional
    public void testCreateOrder() {
        // given
        Order order = new Order(1L, List.of(2L, 3L));
        
        // when
        orderRepository.createOrder(order); 

        // then
        assertEquals(orderRepository.getOrderById(4L).get().getUserId(), 1L);
    }
}
