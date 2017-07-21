package com.jazasoft.mtdbapp.repository;

import com.jazasoft.mtdbapp.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by mdzahidraza on 26/06/17.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {
}
