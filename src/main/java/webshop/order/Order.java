package webshop.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import webshop.customer.Customer;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_date")
    private LocalDate orderDate;

    @OneToOne
    private Customer customer;

    @ElementCollection
    @CollectionTable(name = "ordered_products")
    private List<OrderedProduct> orderedProducts = new ArrayList<>();

    public Order(Customer customer) {
        this.customer = customer;
    }
}