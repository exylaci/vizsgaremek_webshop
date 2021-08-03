package webshop.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "product_category_types")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCategoryType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_category_type")
    private String productCategoryType;

    @Column(name = "category_description")
    private String description;

    public ProductCategoryType(String productCategoryType, String description) {
        this.productCategoryType = productCategoryType;
        this.description = description;
    }
}