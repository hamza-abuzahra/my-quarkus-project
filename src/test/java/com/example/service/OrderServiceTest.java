package com.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.example.application.usecases.order.CreateOrderUseCase;
import com.example.application.usecases.order.GetOrderByIdUseCase;
import com.example.application.usecases.order.GetOrdersUseCase;
import com.example.application.usecases.order.OrderCountUseCase;
import com.example.domain.IOrderRepository;
import com.example.domain.IProductRepository;
import com.example.domain.IUserRepository;
import com.example.domain.Order;
import com.example.domain.Product;
import com.example.domain.User;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class OrderServiceTest {
    
    @InjectMock
    private IOrderRepository orderRepository;
    @InjectMock
    private IUserRepository userRepository;
    @InjectMock 
    private IProductRepository productRepository;
    @Inject
    private CreateOrderUseCase createOrderUseCase;
    @Inject 
    private GetOrdersUseCase getOrdersUseCase;
    @Inject 
    private GetOrderByIdUseCase getOrderByIdUseCase;
    @Inject 
    private OrderCountUseCase orderCountUseCase;

    private ArgumentCaptor<Order> orderArgumentCaptor = ArgumentCaptor.forClass(Order.class);

    @BeforeEach
    void setup() {
        // product repo mock
        when(productRepository.getProductById(2L)).thenReturn(Optional.of(new Product("table", "dinning table", 2333f)));
        when(productRepository.getProductById(4L)).thenReturn(Optional.empty());
        // user repo mock
        when(userRepository.getUserById(1L)).thenReturn(Optional.of(new User(1L, "Hamza", "whatever", "example@good")));
        when(userRepository.getUserById(5L)).thenReturn(Optional.empty());
        // order repo mock
        Order order1 = new Order(1L, 1L, List.of(1L, 2L));
        Order order2 = new Order(2L, 1L, List.of(2L));

        List<Order> returnedList = new ArrayList<Order>();
        returnedList.add(order1);
        returnedList.add(order2);
        when(orderRepository.allOrders(0, 2)).thenReturn(returnedList);        
        when(orderRepository.allOrders(0, 4)).thenReturn(new ArrayList<Order>());
        when(orderRepository.getOrderById(1L)).thenReturn(Optional.of(order1));
        when(orderRepository.getOrderById(5L)).thenReturn(Optional.empty());
        when(orderRepository.allOrderCount()).thenReturn(2);
    }       

    @Test
    public void testGetOrdersOk() {
        // when
        assertEquals(2, getOrdersUseCase.getOrders(0, 2).size());        
        // then
        verify(orderRepository).allOrders(0, 2);
    }

    @Test
    public void testGetOrdersEmpty() {
        // when
        assertEquals(0, getOrdersUseCase.getOrders(0, 4).size());        
        // then
        verify(orderRepository).allOrders(0, 4);
    }

    @Test 
    public void testOrderCount() {
        assertEquals(2, orderCountUseCase.orderCount());

        verify(orderRepository).allOrderCount();
    }

    @Test
    public void testGetOrderByIdOk() {
        // when // then
        assertEquals(1L, getOrderByIdUseCase.getOrderById(1L).get().getUserId());

        verify(orderRepository).getOrderById(1L);
    }

    @Test
    public void testGetProductByIdNotExists() {
        // when // then
        Optional<Order> resOptional = getOrderByIdUseCase.getOrderById(5L); 

        assertTrue(resOptional.isEmpty());

        verify(orderRepository).getOrderById(5L);
    }

    @Test
    public void testNewOrderProductNotValid() {
        Order order4 = new Order(4L, 1L, List.of(4L));

        assertFalse(createOrderUseCase.createOrder(order4));
        
        verify(userRepository).getUserById(1L);
        verify(productRepository).getProductById(4L);
        verify(orderRepository, never()).createOrder(Mockito.any(Order.class));
    }

    @Test
    public void testNewOrderUserNotValid() {
        Order order4 = new Order(4L, 5L, List.of(2L));

        assertFalse(createOrderUseCase.createOrder(order4));
        
        verify(userRepository).getUserById(5L);
        verify(productRepository, never()).getProductById(Mockito.any(Long.class));
        verify(orderRepository, never()).createOrder(Mockito.any(Order.class));

    }

    @Test
    public void testNewOrderOk() {
        Order order4 = new Order(4L, 1L, List.of(2L));

        assertTrue(createOrderUseCase.createOrder(order4));
        
        verify(userRepository).getUserById(1L);
        verify(productRepository).getProductById(2L);
        verify(orderRepository).createOrder(orderArgumentCaptor.capture());
        Order capturedOrder = orderArgumentCaptor.getValue();
        assertEquals(capturedOrder, order4);
    }

}
