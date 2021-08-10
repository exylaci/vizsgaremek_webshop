package webshop.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import webshop.category.ProductCategoryType;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.ratings WHERE p.id=:id")
    List<Product> findProductWithRatings(@Param("id") Long id);

    @Query("select p from Product p order by p.unitPrice ")
    List<Product> findProductOrderedByPrices();

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.ratings")
    List<Product> getProductsWithRatings();

    @Query("SELECT p FROM Product p where p.category=:category")
    List<Product> getProductsFilteredByCategory(ProductCategoryType category);
}