package com.example;
 
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import lombok.AllArgsConstructor;

@ApplicationScoped
@AllArgsConstructor
public class AppInitializer {
    // private final static Logger LOGGER = Logger.getLogger(AppInitializer.class.getName());

    void onStart(@Observes StartupEvent ev){
    //     LOGGER.info("The application is starting...");
        // Product first = new Product("apple", "a green apple", 12);        
        // productResource.createProduct(first);
        // Product second = new Product("table", "a white table", 450.2f);
        // productResource.createProduct(second);
        // Product third = new Product("book", "a black book", 5.5f);
        // Product fourth = new Product("cave", "a very cold and dark cave", 19899.99f);
        // productResource.createProduct(third);        
        // productResource.createProduct(fourth);
    //     LOGGER.info("Database populated.");


    }
    void onStop(@Observes ShutdownEvent ev){
    //     LOGGER.info("The application is stopping...");
    }
}
