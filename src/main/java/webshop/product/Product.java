package webshop.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import webshop.category.ProductCategoryType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name")
    private String name;

    @Column(name = "unit_price")
    private int unitPrice;

    @Column(name = "piece")
    private int piece;

    @OneToOne
    @Column(name = "category_id")
    private ProductCategoryType category;

    @ElementCollection
    @CollectionTable(name = "ratings", joinColumns = @JoinColumn(name = "product_id"))
//    @Column(name = "rating")
    private List<Integer> ratings = new ArrayList<>();

    @Column(name = "product_description")
    private String description;

    public Product(String name, int unitPrice, int piece, ProductCategoryType category, String description) {
        this.name = name;
        this.unitPrice = unitPrice;
        this.piece = piece;
        this.category = category;
        this.description = description;
    }
}