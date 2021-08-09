package webshop.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderedProducts WHERE o.id=:id")
    List<Order> findOrderWithProducts(@Param("id") Long id);
}