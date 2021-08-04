package webshop.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import webshop.customer.CreateUpdateCustomerCommand;
import webshop.customer.CustomerDto;
import webshop.customer.CustomerService;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Operations on customer")
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "get products", description = "Create a list from the all products.")
    public List<ProductDto> getProducts() {
        return service.getProducts();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "find a product", description = "Find one product in the products datatable.")
    @ApiResponse(responseCode = "404", description = "Product not found")
    public ProductDto findProduct(@PathVariable("id") long id) {
        return service.findProduct(id);
    }

    //todo prepare further queries: list by category, order by price, ratings, etc

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "create new product", description = "Create a new product and store it in the products datatable.")
    @ApiResponse(responseCode = "281", description = "Product has been created successfully.")
    public ProductDto createProduct(@RequestBody CreateUpdateProductCommand command) {
        return service.createProduct(command);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "modify a product", description = "Modify the details of an existing product.")
    @ApiResponse(responseCode = "404", description = "Product not found")
    public ProductDto updateProduct(
            @PathVariable("id") long id,
            @RequestBody CreateUpdateProductCommand command) {
        return service.updateProduct(id, command);
    }

    @PostMapping("/{id}/ratings")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "add new rating", description = "Add further rating to a product.")
    @ApiResponse(responseCode = "281", description = "Further rating has been added successfully.")
    public ProductDto addRating(
            @PathVariable("id") long id,
            @RequestBody AddRatingCommand command) {
        return service.addRating(id,command);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete an product", description = "Delete an existing product from the products datatable.")
    @ApiResponse(responseCode = "404", description = "Product not found")
    public void deleteProduct(
            @PathVariable("id") long id) {
        service.deleteProduct(id);
    }
}