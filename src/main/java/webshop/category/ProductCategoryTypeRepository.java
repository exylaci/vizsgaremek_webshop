package webshop.category;

import org.springframework.data.jpa.repository.JpaRepository;
import webshop.customer.Customer;

public interface ProductCategoryTypeRepository extends JpaRepository<ProductCategoryType, Long> {
}