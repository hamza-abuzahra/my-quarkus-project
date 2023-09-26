package com.example.presenter;

import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;

import com.example.application.usecases.product.CreateProductUseCase;
import com.example.application.usecases.product.DeleteProductUseCase;
import com.example.application.usecases.product.GetProductByIdUseCase;
import com.example.application.usecases.product.GetProductsUseCase;
import com.example.application.usecases.product.ProductCountUseCase;
import com.example.application.usecases.product.SaveImageProductUseCase;
import com.example.application.usecases.product.UpdateProductUseCase;
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
// @Consumes(MediaType.APPLICATION_JSON)    
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
    @Inject
    private ProductCountUseCase productCountUseCase;
    @Inject 
    private SaveImageProductUseCase saveImageProductUseCase;
    Logger logger = Logger.getLogger(ProductResource.class.getName());

    @GET
    public Response getAllProdcuts(@QueryParam("offset") @DefaultValue("0") int offset, @QueryParam("size") @DefaultValue("5") int size){
        try {
            List<Product> listProducts = getProductsUseCase.getProducts(offset, size);
            int pageCount = productCountUseCase.productCount();
            if (listProducts.size() == 0) {
                return Response.status(Response.Status.NOT_FOUND).header("numberOfPages", pageCount).entity(Map.of("message", "No prouducts found")).build();
            }
            return Response.ok(Map.of("products", listProducts, "numOfPages", pageCount)) .build();
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
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response createProduct(@RestForm @PartType(MediaType.APPLICATION_JSON) @Valid Product product, @RestForm("image") List<File> files){
        try{
            List<String> imageIds = saveImageProductUseCase.saveImages(files);
            product.setImageIds(imageIds);
            createProductUseCase.createProduct(product);
            return Response.ok("Product added successfully").build();
        }
        catch(Exception e) {
            logger.info("I am here for " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response updateProduct(@PathParam("id") Long id, @RestForm @PartType(MediaType.APPLICATION_JSON) @Valid Product product, @RestForm("image") List<File> files){
        try{
            List<String> imageIds = saveImageProductUseCase.saveImages(files);
            product.setImageIds(imageIds);
            Optional<Product> resOptional = updateProductUseCase.updateProduct(id, product);
            if (resOptional.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).entity(Map.of("message", "Product not found")).build();
            }
            return Response.ok(resOptional.get()).build();
        }
        catch (Exception e){
            logger.info("i am here " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("message", e.getMessage())).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteProduct(@PathParam("id") Long id){
        try{
            boolean isDeleted = deleteProductUseCase.deleteProduct(id);
            if (isDeleted){
                return Response.ok().entity(Map.of("message", "product with the id " + id + " deleted successfully")).build();
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("message", e.getMessage())).build();
        }

    }
}
