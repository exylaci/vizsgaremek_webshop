package webshop.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import webshop.product.ProductDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderedProductDto {
    private ProductDto product;
    private int piece;
}