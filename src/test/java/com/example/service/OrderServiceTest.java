package com.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.example.application.usecases.order.CreateOrderUseCase;
import com.example.application.usecases.order.GetOrderByIdUseCase;
import com.example.application.usecases.order.GetOrdersUseCase;
import com.example.application.usecases.order.OrderCountUseCase;
import com.example.domain.IOrderRepository;
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
        User user = new User(1L, "Hamza", "whatever", "example@good");
        User user2 = new User(2L, "Ahmed", "whatever", "example2@good");
        Product product = new Product("table", "dinning table", 2333f);

        Order order1 = new Order(user, List.of(product));
        Order order2 = new Order(user2, List.of(product));

        List<Order> returnedList = new ArrayList<Order>();
        returnedList.add(order1);
        returnedList.add(order2);
        when(orderRepository.allOrders(0, 2)).thenReturn(returnedList);        
        when(orderRepository.allOrders(0, 4)).thenReturn(new ArrayList<Order>());
        when(orderRepository.getOrderById(1L)).thenReturn(Optional.of(order1));
        when(orderRepository.getOrderById(2L)).thenReturn(Optional.of(order2));
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
        assertEquals("Hamza", getOrderByIdUseCase.getOrderById(1L).get().getUser().getFname());

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
    public void testNewOrderOk() {
        User user = new User(1L, "Hamza", "whatever", "example@good");
        Product product = new Product("table", "dinning table", 2333f);
        Order order4 = new Order(user, List.of(product));  

        assertTrue(createOrderUseCase.createOrder(order4));

        verify(orderRepository).createOrder(orderArgumentCaptor.capture());
        Order capturedOrder = orderArgumentCaptor.getValue();
        assertEquals(capturedOrder, order4);
    }
}
