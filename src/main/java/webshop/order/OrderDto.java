package webshop.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import webshop.customer.Customer;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Long id;
    private LocalDate orderDate;
    private Customer customer;
    private List<OrderedProductDto> orderedProducts;
}