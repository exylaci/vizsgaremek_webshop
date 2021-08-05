package webshop.address;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@Tag(name = "Operations on address")
public class AddressController {
    private final AddressService service;

    public AddressController(AddressService service) {
        this.service = service;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "get addresses", description = "Create a list from the all addresses.")
    public List<AddressDto> getAddresses() {
        return service.getAddresses();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "find an address", description = "Find one address in the addresses datatable.")
    @ApiResponse(responseCode = "404", description = "Address not found")
    public AddressDto findAddress(
            @PathVariable("id") long id) {
        return service.findAddress(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "create new address", description = "Create a new address and store it in the addresses datatable.")
    @ApiResponse(responseCode = "281", description = "Address has been created successfully.")
    public AddressDto createAddress(
            @Valid @RequestBody CreateUpdateAddressCommand command) {
        return service.createAddress(command);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "modify an address", description = "Modify the details of an existing address.")
    @ApiResponse(responseCode = "404", description = "Address not found")
    public AddressDto updateAddress(
            @PathVariable("id") long id,
            @Valid @RequestBody CreateUpdateAddressCommand command) {
        return service.updateAddress(id, command);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete an address", description = "Delete an existing address from the addresses datatable.")
    @ApiResponse(responseCode = "404", description = "Address not found")
    public void deleteAddress(
            @PathVariable("id") long id) {
        service.deleteAddress(id);
    }
}