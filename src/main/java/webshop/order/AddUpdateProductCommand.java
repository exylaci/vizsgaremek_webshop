package webshop.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddUpdateProductCommand {
    @NotNull
    @Schema(example = "1", description = "The id of the current order basket.")
    private Long Order_id;

    @NotNull
    @Schema(example = "1", description = "The id of the selected product.")
    private Long Product_id;

    @Positive
    @Schema(example = "4", description = "Requested pieces of the selected product.")
    private int pieces;
}