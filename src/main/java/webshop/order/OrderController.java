package webshop.order;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Operations on product")
public class OrderController {
    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "get orders list", description = "Create a list from oll of the orders. (The ordered items are excluded from this list.)")
    public List<OrderDto> getOrders() {
        return service.getOrders();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "find an order", description = "Find one order in the orders datatable. (Ordered items list is included.)")
    @ApiResponse(responseCode = "404", description = "Order not found")
    public OrderDto findOrder(
            @PathVariable("id") long id) {
        return service.findOrder(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "create new order", description = "Create a new order.")
    @ApiResponse(responseCode = "281", description = "Order has been created successfully.")
    public OrderDto createProduct(
            @Valid @RequestBody CreateOrderCommand command) {
        return service.createOrder(command);
    }

    @PostMapping("/{id}/products")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "add product", description = "Add the selected product to the order.")
    @ApiResponse(responseCode = "281", description = "Products has been added successfully to the product list of this order.")
    public OrderDto addProduct(
            @PathVariable("id") long id,
            @Valid @RequestBody AddUpdateProductCommand command) {
        return service.addProduct(id, command);
    }

    @DeleteMapping("/{orderid}/products/{productis}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "delete product", description = "Delete the selected product from the ordered products list.")
    @ApiResponse(responseCode = "281", description = "Products has been deleted successfully from the product list of this order.")
    public OrderDto deleteProduct(
            @PathVariable("orderid") long orderId,
            @PathVariable("orderid") long productId) {
        return service.deleteProduct(orderId, productId);
    }

    @PutMapping("/{id}/products")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "modify product", description = "Modify the pieces of the selected product in the product list of the order.")
    @ApiResponse(responseCode = "281", description = "Products has been added successfully to the product list of this order.")
    public OrderDto updateProduct(
            @PathVariable("id") long id,
            @Valid @RequestBody AddUpdateProductCommand command) {
        return service.updateProduct(id, command);
    }

    @PutMapping("/{id}/submit")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "close an order", description = "Finalizing the order. No further product can be added to (or deleted from) this order bucket.")
    @ApiResponse(responseCode = "404", description = "Product not found")
    public OrderDto closeOrder(
            @PathVariable("id") long id) {
        return service.closeOrder(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "cancel an order", description = "Cancel (delete) a not closed order.")
    @ApiResponse(responseCode = "404", description = "Order is not found")
    public void deleteOrder(
            @PathVariable("id") long id) {
        service.deleteOrder(id);
    }
}