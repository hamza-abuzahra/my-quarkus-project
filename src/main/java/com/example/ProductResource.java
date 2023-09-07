package com.example;

import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;

import java.util.Map;
// import java.util.logging.Logger;

import javax.naming.directory.InvalidAttributesException;

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
import lombok.AllArgsConstructor;
@AllArgsConstructor
@Path("/api/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {
    private final ProductService productService;
    // private final static Logger LOGGER = Logger.getLogger(ProductResource.class.getName());

    @GET
    public Response getAllProdcuts(@QueryParam("offset") @DefaultValue("0") int offset, @QueryParam("size") @DefaultValue("5") int size){
        try {
            return Response.ok(productService.getProducts(offset, size)).build();
        } catch (Exception e){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getProductById(@PathParam("id") Long id){
        try{
            return Response.ok(productService.getProductById(id).get()).build();
        } catch (Exception e){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
    @POST
    public Response createProduct(Product product){
        try{
            productService.newProduct(product);
            return Response.ok().build();
        }
        catch (Exception e){
            if (e instanceof InvalidAttributesException) {
                return Response.status(Response.Status.CONFLICT).entity(Map.of("message", e.getMessage())).build();
            }

            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
    @PUT
    @Path("/{id}")
    public Response updateProduct(@PathParam("id") Long id, Product product){
        try {
            return Response.ok(productService.udpate(id, product)).build();
        }
        catch (Exception e){
            // LOGGER.info(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("message", e.getMessage())).build();
        }
    }
    @DELETE
    @Path("/{id}")
    public Response deleteProduct(@PathParam("id") Long id){
        boolean isDeleted = productService.deleteProduct(id);
        if (isDeleted){
            return Response.ok().build();
        }
        return Response.notModified().build();
    }
}
