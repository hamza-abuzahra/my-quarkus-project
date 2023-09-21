package com.example.db;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.domain.IOrderRepository;
import com.example.domain.Order;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JpaOrderRepository implements IOrderRepository, PanacheRepository<JpaOrder> {

    @Override
    public void createOrder(Order order) {
        persist(mapDomainToJpa(order));
    }

    @Override
    public List<Order> allOrders() {
        List<JpaOrder> jpaOrders = findAll(Sort.descending("id")).list();
        return mapJpaListToDomainList(jpaOrders);
    }

    @Override
    public Optional<Order> getOrderById(Long id) {
        JpaOrder order = findById(id);
        if (order == null){
            return Optional.empty();
        }
        return Optional.of(mapJpaToDomain(order));
    }

    private static JpaOrder mapDomainToJpa(Order order){
        JpaOrder jpaOrder = new JpaOrder();
        jpaOrder.setUserId(order.getUserId());
        jpaOrder.setProductId(order.getProductId());
        return jpaOrder;
    }
    
    private static Order mapJpaToDomain(JpaOrder jpaOrder){
        Order order = new Order();
        order.setUserId(jpaOrder.getUserId());
        order.setProductId(jpaOrder.getProductId());
        order.setId(jpaOrder.getId());
        return order;
    }
        
    private List<Order> mapJpaListToDomainList(List<JpaOrder> jpaOrders) {
        return jpaOrders.stream()
            .map(JpaOrderRepository::mapJpaToDomain)
            .collect(Collectors.toList());
    }

}
