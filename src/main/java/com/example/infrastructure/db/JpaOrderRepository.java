package com.example.infrastructure.db;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.example.domain.IOrderRepository;
import com.example.domain.Order;
import com.example.domain.Product;
import com.example.domain.User;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class JpaOrderRepository implements IOrderRepository, PanacheRepository<JpaOrder> {

    Logger logger = Logger.getLogger(JpaOrderRepository.class.getName());

    @Override
    public List<Order> allOrders(int offset, int size) {
        List<JpaOrder> jpaOrders = findAll(Sort.ascending("id")).page(Page.of(offset, size)).list();
        return mapJpaListToDomainList(jpaOrders);
    }
    
    @Override
    public int allOrderCount() {
        return findAll().list().size();
    }

    @Override
    public Optional<Order> getOrderById(Long id) {
        JpaOrder order = findById(id);
        if (order == null){
            return Optional.empty();
        }
        return Optional.of(mapJpaToDomain(order));
    }

    @Override
    @Transactional
    public void createOrder(Order order) {
        persist(mapDomainToJpa(order));
    }

    @Override
    public void deleteAllOrders() {
        deleteAll();
        String sql = "Select setval('jpaorder_seq', 1)";
        list(sql);
    }
    private static JpaOrder mapDomainToJpa(Order order){
        JpaOrder jpaOrder = new JpaOrder();
        User user = order.getUser();
        JpaUser jpaUser = new JpaUser(user.getId(), user.getFname(), user.getLname(), user.getEmail());
        jpaOrder.setUser(jpaUser);
        List<JpaProduct> products = order.getProducts().stream().map((product) -> {
            JpaProduct jpaProduct = new JpaProduct(product.getId(), product.getName(), product.getDescribtion(), product.getPrice(), product.getImageIds());
            return jpaProduct;
        }).collect(Collectors.toList());
        jpaOrder.setProducts(products);
        jpaOrder.setId(order.getId());
        return jpaOrder;
    }
    
    private static Order mapJpaToDomain(JpaOrder jpaOrder){
        Order order = new Order();
        JpaUser jpaUser = jpaOrder.getUser();
        User user = new User(jpaUser.getId(), jpaUser.getFname(), jpaUser.getLname(), jpaUser.getEmail());
        order.setUser(user);
        List<Product> products = jpaOrder.getProducts().stream().map((jpaProduct) -> {
            Product product = new Product(jpaProduct.getId(), jpaProduct.getName(), jpaProduct.getDescribtion(), jpaProduct.getPrice(), jpaProduct.getImageIds());
            return product;
        }).collect(Collectors.toList());
        order.setProducts(products);
        order.setId(jpaOrder.getId());
        return order;
    }
        
    private List<Order> mapJpaListToDomainList(List<JpaOrder> jpaOrders) {
        return jpaOrders.stream()
            .map(JpaOrderRepository::mapJpaToDomain)
            .collect(Collectors.toList());
    }

}
