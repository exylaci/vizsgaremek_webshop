package webshop.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderCommand {
    @NotNull
    @Schema(example = "1", description = "The id of the customer.")
    private Long customer_id;
}