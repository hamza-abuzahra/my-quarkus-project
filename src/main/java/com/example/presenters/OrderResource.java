package com.example.presenters;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.example.domain.Order;
import com.example.usecases.CreateOrderUseCase;
import com.example.usecases.GetOrdersUseCase;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("api/orders")
public class OrderResource {
    
    @Inject
    private CreateOrderUseCase createOrderUseCase;
    @Inject
    private GetOrdersUseCase getOrdersUseCase;
    
    Logger logger = Logger.getLogger(OrderResource.class.getName());

    @GET
    public Response getOrders(){
        try {
            List<Order> orderList = getOrdersUseCase.getOrders();
            if (orderList.size() == 0) {
                return Response.status(Response.Status.NOT_FOUND).entity(Map.of("message", "No orders found")).build();
            }
            return Response.ok(orderList).build();
        } catch (Exception e) {
            logger.info("I am here for " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    @POST
    public Response createOrder(@Valid Order order){
        try {
            if (order.getId() != null) {
                return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("viloations", "id must be null")).build();
            }
            if (!createOrderUseCase.createOrder(order)) {
                return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("viloations", "one of the given fields do not exist, either user or one of the products")).build();
            } 
            return Response.ok("order added successfully").build();
            
        } catch (Exception e) {
            logger.info("I am here for " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        
    }
}
