package webshop.address;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUpdateAddressCommand {
    @NotNull
    @NotBlank
    @Schema(example = "Budapest", description = "Name of the city.")
    private String city;

    @IsValidZipCode
    @NotNull
    @NotBlank
    @Schema(example = "1000", description = "ZIP code.")
    private String zipCode;

    @NotNull
    @NotBlank
    @Schema(example = "Main street 1, 1st floor 4th door.", description = "Detailed address. Name of the street and house number.")
    private String streetHouse;

    @Schema(example = "doorbell: 14", description = "Comment related to the address.")
    private String comment;
}
