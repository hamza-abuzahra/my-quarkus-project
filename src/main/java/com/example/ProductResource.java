package com.example;

import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {
    private final IProductService productService;
    public ProductResource(IProductService productService){
        this.productService = productService;
    }

    @GET
    public Response getAllProdcuts(@QueryParam("offset") @DefaultValue("0") int offset, @QueryParam("size") @DefaultValue("5") int size){
        try {
            List<Product> resList = productService.getProducts(offset, size);
            if (resList.size() == 0) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(productService.getProducts(offset, size)).build();
            
        } catch (Exception e){
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("message", e.getMessage())).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getProductById(@PathParam("id") Long id){
        try{
            Optional<Product> resOptional = productService.getProductById(id);
            if (resOptional.isEmpty()){
                return Response.status(Response.Status.NOT_FOUND).build();    
            }
            return Response.ok(productService.getProductById(id).get()).build();
        } catch (Exception e){
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("message", e.getMessage())).build();
        }
    }

    @POST
    public Response createProduct(Product product){
        try{
            productService.newProduct(product);
            return Response.ok().build();
        }
        catch (Exception e){
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("message", e.getMessage())).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateProduct(@PathParam("id") Long id, Product product){
        try {
            return Response.ok(productService.udpateProduct(id, product)).build();
        }
        catch (Exception e){
            if (e instanceof InvalidParameterException){
                return Response.status(Response.Status.NOT_FOUND).entity(Map.of("message", e.getMessage())).build();
            }
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("message", e.getMessage())).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteProduct(@PathParam("id") Long id){
        try{
            boolean isDeleted = productService.deleteProduct(id);
            if (isDeleted){
                return Response.ok().build();
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("message", e.getMessage())).build();
        }

    }
}
