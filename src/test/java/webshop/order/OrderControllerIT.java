package webshop.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;
import webshop.address.AddressDto;
import webshop.address.CreateUpdateAddressCommand;
import webshop.category.CreateUpdateProductCategoryTypeCommand;
import webshop.category.ProductCategoryTypeDto;
import webshop.customer.CreateUpdateCustomerCommand;
import webshop.customer.CustomerDto;
import webshop.product.CreateUpdateProductCommand;
import webshop.product.ProductDto;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(statements = "delete from addresses_customers")
@Sql(statements = "delete from ordered_products")
@Sql(statements = "delete from orders")
@Sql(statements = "delete from customers")
@Sql(statements = "delete from addresses")
@Sql(statements = "delete from ratings")
@Sql(statements = "delete from products")
@Sql(statements = "delete from product_category_types")
public class OrderControllerIT {
    @Autowired
    TestRestTemplate template;

    ProductCategoryTypeDto categoryElectronics;
    ProductCategoryTypeDto categoryOffice;
    ProductDto productTv;
    ProductDto productPaper;
    AddressDto address;
    CustomerDto customer;
    OrderDto order;

    @BeforeEach
    void init() {
        categoryElectronics = template.postForObject(
                "/api/productcategorytypes",
                new CreateUpdateProductCategoryTypeCommand("entertainment electronics", "Electronic equipment that used for entertainment at home."),
                ProductCategoryTypeDto.class);
        categoryOffice = template.postForObject(
                "/api/productcategorytypes",
                new CreateUpdateProductCategoryTypeCommand("office materials", "Goods that need for every offices."),
                ProductCategoryTypeDto.class);
        productTv = template.postForObject(
                "/api/products",
                new CreateUpdateProductCommand("TV", 1234, 17, categoryElectronics.getId(), "8Q OLED 128cm"),
                ProductDto.class);
        productPaper = template.postForObject(
                "/api/products",
                new CreateUpdateProductCommand("paper", 21, 39, categoryOffice.getId(), "A4 size 1000 pages per pack."),
                ProductDto.class);
        address = template.postForObject(
                "/api/addresses",
                new CreateUpdateAddressCommand("Budapest", "1000", "Main street 1, 1st floor 4th door.", "doorbell: 14"),
                AddressDto.class);
        customer = template.postForObject(
                "/api/customers",
                new CreateUpdateCustomerCommand("John Doe", "john.doe@mailserver.hu", address.getId(), address.getId(), "Just call me Jonny!"),
                CustomerDto.class);
        order = template.postForObject(
                "/api/orders",
                new CreateOrderCommand(customer.getId()),
                OrderDto.class);
    }

    @Test
    void testGetOrders() {
        OrderDto order2 = template.postForObject(
                "/api/orders",
                new CreateOrderCommand(customer.getId()),
                OrderDto.class);

        List<OrderDto> expected = template.exchange(
                "/api/orders",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<OrderDto>>() {
                }).getBody();

        assertThat(expected)
                .extracting(OrderDto::getId)
                .containsExactlyInAnyOrder(order.getId(), order2.getId());
    }

    @Test
    void testFindOrder() {
        OrderDto expected = template.exchange(
                "/api/orders/" + order.getId(),
                HttpMethod.GET,
                null,
                OrderDto.class)
                .getBody();

        assertEquals(order.getId(), expected.getId());
    }

    @Test
    void testAddProduct() {
        order = template.postForObject(
                "/api/orders/" + order.getId() + "/products",
                new AddUpdateProductCommand(order.getId(), productTv.getId(), 5),
                OrderDto.class);

        OrderDto expectedOrder = template.exchange(
                "/api/orders/" + order.getId(),
                HttpMethod.GET,
                null,
                OrderDto.class)
                .getBody();
        assertEquals(productTv, expectedOrder.getOrderedProducts().get(0).getProduct());

        ProductDto expectedProduct = template.exchange(
                "/api/products/" + productTv.getId(),
                HttpMethod.GET,
                null,
                ProductDto.class)
                .getBody();
        assertEquals(17, expectedProduct.getPiece());
    }

    @Test
    void testModifyProduct() {
        order = template.postForObject(
                "/api/orders/" + order.getId() + "/products",
                new AddUpdateProductCommand(order.getId(), productTv.getId(), 5),
                OrderDto.class);

        order = template.postForObject(
                "/api/orders/" + order.getId() + "/products",
                new AddUpdateProductCommand(order.getId(), productTv.getId(), 5),
                OrderDto.class);

        template.put(
                "/api/orders/" + order.getId() + "/products",
                new AddUpdateProductCommand(order.getId(), productTv.getId(), 8),
                OrderDto.class);

        OrderDto expectedOrder = template.exchange(
                "/api/orders/" + order.getId(),
                HttpMethod.GET,
                null,
                OrderDto.class)
                .getBody();
        assertEquals(8, expectedOrder.getOrderedProducts().get(0).getPiece());

        ProductDto expectedProduct = template.exchange(
                "/api/products/" + productTv.getId(),
                HttpMethod.GET,
                null,
                ProductDto.class)
                .getBody();
        assertEquals(17, expectedProduct.getPiece());
    }

    @Test
    void testSubmit() {
        order = template.postForObject(
                "/api/orders/" + order.getId() + "/products",
                new AddUpdateProductCommand(order.getId(), productTv.getId(), 5),
                OrderDto.class);
        template.put(
                "/api/orders/{id}/submit",
                null,
                order.getId());

        OrderDto expectedOrder = template.exchange(
                "/api/orders/" + order.getId(),
                HttpMethod.GET,
                null,
                OrderDto.class)
                .getBody();
        assertEquals(LocalDate.now(), expectedOrder.getOrderDate());

        ProductDto expectedProduct = template.exchange(
                "/api/products/" + productTv.getId(),
                HttpMethod.GET,
                null,
                ProductDto.class)
                .getBody();
        assertEquals(17 - 5, expectedProduct.getPiece());
    }

    @Test
    void testSubmitRequestWitInsufficientStorage() {
        order = template.postForObject(
                "/api/orders/" + order.getId() + "/products",
                new AddUpdateProductCommand(order.getId(), productTv.getId(), 5),
                OrderDto.class);
        order = template.postForObject(
                "/api/orders/" + order.getId() + "/products",
                new AddUpdateProductCommand(order.getId(), productPaper.getId(), 40),
                OrderDto.class);
        template.put(
                "/api/orders/{id}/submit",
                null,
                order.getId());

        OrderDto expectedOrder = template.exchange(
                "/api/orders/" + order.getId(),
                HttpMethod.GET,
                null,
                OrderDto.class)
                .getBody();
        assertNull(expectedOrder.getOrderDate());

        ProductDto expectedProduct = template.exchange(
                "/api/products/" + productTv.getId(),
                HttpMethod.GET,
                null,
                ProductDto.class)
                .getBody();
        assertEquals(17, expectedProduct.getPiece());
        expectedProduct = template.exchange(
                "/api/products/" + productPaper.getId(),
                HttpMethod.GET,
                null,
                ProductDto.class)
                .getBody();
        assertEquals(39, expectedProduct.getPiece());
    }

    @Test
    void testSubmitEmptyOrder() {
        template.put(
                "/api/orders/" + order.getId() + "/submit",
                null,
                order.getId());

        OrderDto expectedOrder = template.exchange(
                "/api/orders/" + order.getId(),
                HttpMethod.GET,
                null,
                OrderDto.class)
                .getBody();
        assertNull(expectedOrder.getOrderDate());
    }

    @Test
    void testDeleteProduct() {
        order = template.postForObject(
                "/api/orders/" + order.getId() + "/products",
                new AddUpdateProductCommand(order.getId(), productTv.getId(), 5),
                OrderDto.class);
        order = template.postForObject(
                "/api/orders/" + order.getId() + "/products",
                new AddUpdateProductCommand(order.getId(), productPaper.getId(), 7),
                OrderDto.class);

        template.delete("/api/orders/" + order.getId() + "/products/" + productTv.getId());

        OrderDto expectedOrder = template.exchange(
                "/api/orders/" + order.getId(),
                HttpMethod.GET,
                null,
                OrderDto.class)
                .getBody();

        assertEquals(1, expectedOrder.getOrderedProducts().size());
        assertEquals(productPaper.getName(), expectedOrder.getOrderedProducts().get(0).getProduct().getName());
    }

    @Test
    void testDeleteDifferentProduct() {
        order = template.postForObject(
                "/api/orders/" + order.getId() + "/products",
                new AddUpdateProductCommand(order.getId(), productTv.getId(), 5),
                OrderDto.class);

        template.delete("/api/orders/" + order.getId() + "/products/" + productPaper.getId());

        OrderDto expectedOrder = template.exchange(
                "/api/orders/" + order.getId(),
                HttpMethod.GET,
                null,
                OrderDto.class)
                .getBody();

        assertEquals(1, expectedOrder.getOrderedProducts().size());
        assertEquals(productTv.getName(), expectedOrder.getOrderedProducts().get(0).getProduct().getName());
    }


    @Test
    void testDeleteNotExistingProduct() {
        order = template.postForObject(
                "/api/orders/" + order.getId() + "/products",
                new AddUpdateProductCommand(order.getId(), productTv.getId(), 5),
                OrderDto.class);

        template.delete("/api/orders/" + order.getId() + "/products/0");

        OrderDto expectedOrder = template.exchange(
                "/api/orders/" + order.getId(),
                HttpMethod.GET,
                null,
                OrderDto.class)
                .getBody();

        assertEquals(1, expectedOrder.getOrderedProducts().size());
    }

    @Test
    void testDeleteFromEmptyProductsList() {
        template.delete("/api/orders/" + order.getId() + "/products/" + productTv.getId());

        OrderDto expectedOrder = template.exchange(
                "/api/orders/" + order.getId(),
                HttpMethod.GET,
                null,
                OrderDto.class)
                .getBody();

        assertEquals(0, expectedOrder.getOrderedProducts().size());
    }

    @Test
    void testDeleteSubmittedOrder() {
        order = template.postForObject(
                "/api/orders/" + order.getId() + "/products",
                new AddUpdateProductCommand(order.getId(), productTv.getId(), 5),
                OrderDto.class);
        template.put(
                "/api/orders/{id}/submit",
                null,
                order.getId());

        template.delete("/api/orders/" + order.getId());

        List<OrderDto> expected = template.exchange(
                "/api/orders",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<OrderDto>>() {
                })
                .getBody();
        assertEquals(1, expected.size());
    }

    @Test
    void testDeleteNotSubmittedOrder() {
        order = template.postForObject(
                "/api/orders/" + order.getId() + "/products",
                new AddUpdateProductCommand(order.getId(), productTv.getId(), 5),
                OrderDto.class);

        template.delete("/api/orders/" + order.getId());

        List<OrderDto> expected = template.exchange(
                "/api/orders",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<OrderDto>>() {
                })
                .getBody();
        assertEquals(0, expected.size());
    }

    @Test
    void testDeleteNotExistingOrder() {
        template.delete("/api/orders/0");

        List<OrderDto> expected = template.exchange(
                "/api/orders",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<OrderDto>>() {
                })
                .getBody();
        assertEquals(1, expected.size());
    }
}