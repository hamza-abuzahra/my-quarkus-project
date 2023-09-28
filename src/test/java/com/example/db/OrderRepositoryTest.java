package com.example.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.example.domain.IOrderRepository;
import com.example.domain.IProductRepository;
import com.example.domain.IUserRepository;
import com.example.domain.Order;
import com.example.domain.Product;
import com.example.domain.User;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.transaction.annotations.Rollback;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@QuarkusTest
@Transactional
@Rollback(true)
@TestInstance(Lifecycle.PER_CLASS)
public class OrderRepositoryTest {
    
    @Inject
    private IOrderRepository orderRepository;
    @Inject
    private IUserRepository userRepository;
    @Inject 
    private IProductRepository productRepository;

    Logger logger = Logger.getLogger(OrderRepositoryTest.class.getName());
    private Product product = new Product("laptop", "predator gaming laptop", 2311.5f);
    private Product product2 = new Product("car", "fast car", 2311.5f);
    private Product product3 = new Product("apple", "red apple", 2311.5f);
    private User user = new User("first name", "last name", "good@email.com");
    private User user2 = new User("first name2", "last name3", "good2@email.com");

    @BeforeAll
    @Transactional
    void setup() {
        userRepository.createUser(user);        
        userRepository.createUser(user2);
        productRepository.createProduct(product);
        productRepository.createProduct(product2);
        productRepository.createProduct(product3);
    }

    @AfterEach
    @Transactional
    void tearDown() {
        orderRepository.deleteAllOrders();
    }
    
    @AfterAll
    @Transactional
    void end() {
        userRepository.deleteAllUsers();
        productRepository.deleteAllProducts();
    }
    @Test
    @Transactional
    public void testGetAllOrders() {
        // given
        User user = userRepository.getUserById(1L).get();
        User user2 = userRepository.getUserById(2L).get();
        Product product = productRepository.getProductById(1L).get();
        Product product2 = productRepository.getProductById(2L).get();
        Product product3 = productRepository.getProductById(3L).get();

        Order order = new Order(user, List.of(product, product2));
        Order order2 = new Order(user2, List.of(product2));
        Order order3 = new Order(user, List.of(product3, product));
        orderRepository.createOrder(order);        
        orderRepository.createOrder(order2);
        orderRepository.createOrder(order3);

        // when
        List<Order> res = orderRepository.allOrders(0, 2);

        // then
        assertEquals(res.get(0).getUser().getFname(), "first name");
        assertEquals(res.get(1).getProducts().get(0).getName(), "car");
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
        userRepository.createUser(user);        
        userRepository.createUser(user2);
        productRepository.createProduct(product);
        productRepository.createProduct(product2);
        User user = userRepository.getUserById(1L).get();
        User user2 = userRepository.getUserById(2L).get();
        Product product = productRepository.getProductById(1L).get();
        Product product2 = productRepository.getProductById(2L).get();

        Order order = new Order(user, List.of(product, product2));
        Order order2 = new Order(user2, List.of(product2));
        Order order3 = new Order(user, List.of(product));
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
        userRepository.createUser(user);        
        productRepository.createProduct(product);
        User user = userRepository.getUserById(1L).get();
        Product product = productRepository.getProductById(1L).get();
        
        Order order = new Order(user, List.of(product));
        orderRepository.createOrder(order);        

        // when 
        Optional<Order> resOptional = orderRepository.getOrderById(5L);
        
        // then
        assertFalse(resOptional.isEmpty());
        Order order2 = resOptional.get();
        assertEquals(order2.getUser().getFname(), "first name");
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
        userRepository.createUser(user);        
        productRepository.createProduct(product);
        User user = userRepository.getUserById(1L).get();
        Product product = productRepository.getProductById(1L).get();

        
        
        
        // when
        Order order = new Order(user, List.of(product));
        orderRepository.createOrder(order); 

        
        // then
        assertEquals(orderRepository.getOrderById(4L).get().getUser().getFname(), "first name");
    }
}
