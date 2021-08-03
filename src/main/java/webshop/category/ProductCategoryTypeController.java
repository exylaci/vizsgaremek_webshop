package webshop.category;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productcategorytypes")
@Tag(name = "Operations on product category type")
public class ProductCategoryTypeController {
    private final ProductCategoryTypeService service;

    public ProductCategoryTypeController(ProductCategoryTypeService service) {
        this.service = service;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "get ProductCategoryTypes", description = "Create a list from the all product category types.")
    public List<ProductCategoryTypeDto> getCustomers() {
        return service.getProductCategoryTypes();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "find a ProductCategoryType", description = "Find one customer in the product_category_types datatable.")
    @ApiResponse(responseCode = "404", description = "ProductCategoryType not found")
    public ProductCategoryTypeDto findProductCategoryType(@PathVariable("id") long id) {
        return service.findProductCategoryType(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "create new ProductCategoryType", description = "Create a new ProductCategoryType and store it in the product_category_type datatable.")
    @ApiResponse(responseCode = "281", description = "ProductCategoryType has been created successfully.")
    public ProductCategoryTypeDto createProductCategoryType(@RequestBody CreateUpdateProductCategoryTypeCommand command) {
        return service.createProductCategoryType(command);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "modify a productCategoryType", description = "Modify the details of an existing product category type.")
    @ApiResponse(responseCode = "404", description = "ProductCategoryType not found")
    public ProductCategoryTypeDto updateProductCategoryType(
            @PathVariable("id") long id,
            @RequestBody CreateUpdateProductCategoryTypeCommand command) {
        return service.updateProductCategoryType(id, command);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete a productCategoryType", description = "Delete an existing ProductCategoryType from the product_category_type datatable.")
    @ApiResponse(responseCode = "404", description = "ProductCategoryType not found")
    public void deleteProductCategoryType(
            @PathVariable("id") long id) {
        service.deleteProductCategoryType(id);
    }
}