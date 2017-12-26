package com.jazasoft.mtdbapp.restcontroller;

import com.jazasoft.mtdb.IApiUrls;
import com.jazasoft.mtdbapp.AppApiUrl;
import com.jazasoft.mtdbapp.entity.Product;
import com.jazasoft.mtdbapp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by mdzahidraza on 28/06/17.
 */
@RestController
@RequestMapping(AppApiUrl.ROOT_URL_PRODUCTS)
public class ProductRestController {

    private ProductService productService;

    @Autowired
    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<?> getProducts(){
        List<Product> products = productService.findAll();
        return ResponseEntity.ok(products);
    }


    @GetMapping(IApiUrls.URL_USERS_USER)
    public ResponseEntity<?> getProduct(@PathVariable("productId") Long productId){
        Product product = productService.findOne(productId);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<?> saveProduct(@RequestBody Product product){
        product = productService.save(product);
        return ResponseEntity.ok(product);
    }
}
