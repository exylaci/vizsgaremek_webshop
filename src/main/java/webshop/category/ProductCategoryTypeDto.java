package webshop.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCategoryTypeDto {
    private Long id;
    private String productCategoryType;
    private String description;
}