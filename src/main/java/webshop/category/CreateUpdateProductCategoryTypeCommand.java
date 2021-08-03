package webshop.category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUpdateProductCategoryTypeCommand {
    @NotNull
    @NotBlank
    @Schema(example = "entertainment electronics", description = "Product category type.")
    private String productCategoryType;

    @Schema(example = "Electronic equipment that used for entertainment at home.", description = "Description related to the product category type.")
    private String description;
}