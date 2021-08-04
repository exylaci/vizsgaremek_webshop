package webshop.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import webshop.category.ProductCategoryType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private int unitPrice;
    private int piece;
    private ProductCategoryType category;
    private Double rating;
    private String description;

    public ProductDto(String name, int unitPrice, int piece, ProductCategoryType category, Double rating, String description) {
        this.name = name;
        this.unitPrice = unitPrice;
        this.piece = piece;
        this.category = category;
        this.rating = rating;
        this.description = description;
    }
}