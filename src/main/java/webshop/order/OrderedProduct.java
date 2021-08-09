package webshop.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import webshop.product.Product;

import javax.persistence.*;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderedProduct {
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private Product product;

    private int piece;
}