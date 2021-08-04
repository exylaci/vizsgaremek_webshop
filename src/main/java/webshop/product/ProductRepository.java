package webshop.product;

import org.springframework.data.jpa.repository.JpaRepository;
import webshop.customer.Customer;

public interface ProductRepository extends JpaRepository<Product, Long> {
}