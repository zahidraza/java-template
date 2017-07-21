package com.jazasoft.mtdbapp.service;

import com.jazasoft.mtdbapp.entity.Product;
import com.jazasoft.mtdbapp.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by mdzahidraza on 28/06/17.
 */
@Service
@Transactional(value = "tenantTransactionManager")
public class ProductService {


    ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product findOne(Long id){
        return productRepository.findOne(id);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public long count() {
        return productRepository.count();
    }
}
