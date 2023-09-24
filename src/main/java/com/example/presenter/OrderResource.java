package com.example.presenter;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import com.example.application.usecases.order.CreateOrderUseCase;
import com.example.application.usecases.order.GetOrderByIdUseCase;
import com.example.application.usecases.order.GetOrdersUseCase;
import com.example.domain.Order;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

@Path("api/orders")
public class OrderResource {
    
    @Inject
    private CreateOrderUseCase createOrderUseCase;
    @Inject
    private GetOrdersUseCase getOrdersUseCase;
    @Inject GetOrderByIdUseCase getOrderByIdUseCase;
    
    Logger logger = Logger.getLogger(OrderResource.class.getName());

    @GET
    public Response getOrders(@PathParam("offset") @DefaultValue("0") int offset, @PathParam("size")  @DefaultValue("5") int size){
        try {
            List<Order> orderList = getOrdersUseCase.getOrders(offset, size);
            if (orderList.size() == 0) {
                return Response.status(Response.Status.NOT_FOUND).entity(Map.of("message", "No orders found")).build();
            }
            return Response.ok(orderList).build();
        } catch (Exception e) {
            logger.info("I am here for " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getOrderById(@PathParam("id") Long id){
        try{            
            Optional<Order> resOrder = getOrderByIdUseCase.getOrderById(id);
            if (resOrder.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).entity(Map.of("message", "Order not found")).build(); 
            }
            return Response.ok(resOrder.get()).build();
        } catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("message", e.getMessage())).build();
        }
    }

    @POST
    public Response createOrder(@Valid Order order){
        try {
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
