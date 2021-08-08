package webshop.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUpdateProductCommand {

    @NotNull
    @NotBlank
    @Schema(example = "TV", description = "The name of the product.")
    private String name;

    @Schema(example = "1234", description = "Unit price of the product.")
    private int unitPrice;

    @Schema(example = "17", description = "How many pieces are in the store.")
    private int piece;

    @NotNull
    @Schema(example = "1", description = "The product belongs to this category.")
    private Long category;

    @Schema(example = "8Q OLED 128cm", description = "Detailed description about this product.")
    private String description;
}