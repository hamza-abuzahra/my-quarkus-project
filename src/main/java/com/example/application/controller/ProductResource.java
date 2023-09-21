package com.example.application.controller;

import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import com.example.application.usecases.CreateProductUseCase;
import com.example.application.usecases.DeleteProductUseCase;
import com.example.application.usecases.GetProductByIdUseCase;
import com.example.application.usecases.GetProductsUseCase;
import com.example.application.usecases.UpdateProductUseCase;
import com.example.domain.Product;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
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
        
    @Inject
    private GetProductsUseCase getProductsUseCase;
    @Inject
    private GetProductByIdUseCase getProductByIdUseCase;
    @Inject
    private CreateProductUseCase createProductUseCase;
    @Inject
    private UpdateProductUseCase updateProductUseCase;
    @Inject
    private DeleteProductUseCase deleteProductUseCase;
    Logger logger = Logger.getLogger(ProductResource.class.getName());


    @GET
    public Response getAllProdcuts(@QueryParam("offset") @DefaultValue("0") int offset, @QueryParam("size") @DefaultValue("5") int size){
        try {
            List<Product> listProducts = getProductsUseCase.getProducts(offset, size);
            if (listProducts.size() == 0) {
                return Response.status(Response.Status.NOT_FOUND).entity(Map.of("message", "No prouducts found")).build();
            }
            return Response.ok(listProducts).build();
        } catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("message", e.getMessage())).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getProductById(@PathParam("id") Long id){
        try{            
            Optional<Product> resProduct = getProductByIdUseCase.getProductById(id);
            if (resProduct.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).entity(Map.of("message", "Product not found")).build(); 
            }
            return Response.ok(resProduct.get()).build();
        } catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("message", e.getMessage())).build();
        }
    }

    @POST
    public Response createProductEndpoint(@Valid Product product){
        try{
            if (product.getId() != null){
                logger.info("I am here for trying cus id is provided");

                return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("viloations", "id must be null")).build();
            }
            logger.info("creating alright");
            createProductUseCase.createProduct(product);
            logger.info("creating done");
            return Response.ok("Product added successfully").build();
        }
        catch(Exception e) {
            logger.info("I am here for " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateProduct(@PathParam("id") Long id, @Valid Product product){
        try{
            if (product.getId() != null){
                return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("viloations", "id must be null")).build();
            }
            Optional<Product> resOptional = updateProductUseCase.updateProduct(id, product);
            if (resOptional.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).entity(Map.of("message", "Product not found")).build();
            }   
            return Response.ok(resOptional.get()).build();
        }
        catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("message", e.getMessage())).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteProduct(@PathParam("id") Long id){
        try{
            boolean isDeleted = deleteProductUseCase.deleteProduct(id);
            if (isDeleted){
                return Response.ok().entity(Map.of("message", "product with the id" + id + " deleted successfully")).build();
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("message", e.getMessage())).build();
        }

    }
}
