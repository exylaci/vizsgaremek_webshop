package webshop.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUpdateCustomerCommand {

    @NotNull
    @NotBlank
    @Schema(example = "John Doe", description = "Name of the customer.")
    private String name;

    @NotNull
    @NotBlank
    @Schema(example = "john.doe@mailserver.hu", description = "Email address of the customer.")
    private String email;

    @NotNull
    @Schema(example = "1", description = "Where the product has to be delivered. Id from the addresses data table.")
    private Long deliveryAddressId;

    @NotNull
    @Schema(example = "1", description = "What address has to be on the invoice. Id from the addresses data table.")
    private Long invoiceAddressId;

    @Schema(example = "It's a comment about the customer.", description = "Comment related to the customer.")
    private String comment;
}