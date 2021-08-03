package webshop.customer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Operations on customer")
public class CustomerController {
    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "get customers", description = "Create a list from the all customers.")
    public List<CustomerDto> getCustomers() {
        return service.getCustomers();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "find a customer", description = "Find one customer in the customers datatable.")
    @ApiResponse(responseCode = "404", description = "Customer not found")
    public CustomerDto findCustomer(@PathVariable("id") long id) {
        return service.findCustomer(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "create new customer", description = "Create a new customer and store it in the customers datatable.")
    @ApiResponse(responseCode = "281", description = "Customer has been created successfully.")
    public CustomerDto createCustomer(@RequestBody CreateUpdateCustomerCommand command) {
        return service.createCustomer(command);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "modify an customer", description = "Modify the details of an existing customer.")
    @ApiResponse(responseCode = "404", description = "Customer not found")
    public CustomerDto updateCustomer(
            @PathVariable("id") long id,
            @RequestBody CreateUpdateCustomerCommand command) {
        return service.updateCustomer(id, command);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete an customer", description = "Delete an existing customer from the customer datatable.")
    @ApiResponse(responseCode = "404", description = "Customer not found")
    public void deleteCustomer(
            @PathVariable("id") long id) {
        service.deleteCustomer(id);
    }
}