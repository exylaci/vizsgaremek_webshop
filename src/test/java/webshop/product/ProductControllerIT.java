package webshop.product;

import io.swagger.v3.oas.annotations.media.Schema;
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
import webshop.category.ProductCategoryType;
import webshop.category.ProductCategoryTypeDto;

import javax.validation.constraints.NotNull;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(statements = "delete from ratings")
@Sql(statements = "delete from products")
public class ProductControllerIT {
    @Autowired
    TestRestTemplate template;

    Long id;
    Long categoryId;

    @BeforeEach
    void init() {
        ProductCategoryTypeDto productCategoryTypeDto = template.postForObject(
                "/api/productcategorytypes",
                new CreateUpdateProductCategoryTypeCommand("entertainment electronics", "Electronic equipment that used for entertainment at home."),
                ProductCategoryTypeDto.class);
        long tempId = productCategoryTypeDto.getId();
        productCategoryTypeDto = template.postForObject(
                "/api/productcategorytypes",
                new CreateUpdateProductCategoryTypeCommand("office materials", "Goods that need for every offices."),
                ProductCategoryTypeDto.class);
        categoryId = productCategoryTypeDto.getId();

        try {
            ProductDto productDto = template.postForObject(
                    "/api/products",
                    new CreateUpdateProductCommand("TV", 1234, 17, tempId, "8Q OLED 128cm"),
                    ProductDto.class);
            id = productDto.getId();
        } catch (Exception e) {
            id = template.exchange(
                    "/api/products",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<ProductDto>>() {
                    }).getBody()
                    .stream()
                    .findAny()
                    .get()
                    .getId();
        }
        try {
            template.postForObject(
                    "/api/products",
                    new CreateUpdateProductCommand("paper", 21, 39, tempId, "A4 size 1000 pages per pack."),
                    ProductDto.class);
        } catch (Exception e) {
        }
    }

    @Test
    void testGetProducts() {
        List<ProductDto> expected = template.exchange(
                "/api/products",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ProductDto>>() {
                }).getBody();

        assertThat(expected)
                .extracting(ProductDto::getName)
                .containsExactly("TV", "paper");
    }

    @Test
    void testGetRatingNoRatings() {
        ProductDto expected = template.exchange(
                "/api/products/" + id,
                HttpMethod.GET,
                null,
                ProductDto.class)
                .getBody();

        assertEquals("TV", expected.getName());
        assertEquals(0.0, expected.getRating());
    }

    @Test
    void testAddAndGetRatings() {
        template.postForObject(
                "/api/products/" + id + "/ratings",
                new AddRatingCommand(3),
                ProductDto.class);
        template.postForObject(
                "/api/products/" + id + "/ratings",
                new AddRatingCommand(2),
                ProductDto.class);

        ProductDto expected = template.exchange(
                "/api/products/" + id,
                HttpMethod.GET,
                null,
                ProductDto.class)
                .getBody();

        assertEquals(2.5, expected.getRating(), 0.005);
    }

    @Test
    void testUpdateProduct() {
        template.put(
                "/api/products/" + id,
                new CreateUpdateProductCommand("radio", 4321, 5, categoryId, "DAB compatible"),
                ProductDto.class);
        ProductDto expected = template.exchange(
                "/api/products/" + id,
                HttpMethod.GET,
                null,
                ProductDto.class).getBody();

        assertEquals("radio", expected.getName());
        assertEquals(4321, expected.getUnitPrice());
        assertEquals(5, expected.getPiece());
        assertEquals(categoryId, expected.getCategory().getId());
        assertEquals("DAB compatible", expected.getDescription());
    }

    @Test
    void testDeleteProduct() {
        template.delete("/api/products/" + id);

        List<ProductDto> expected = template.exchange(
                "/api/products",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ProductDto>>() {
                }).getBody();

        assertThat(expected)
                .extracting(ProductDto::getId)
                .doesNotContain(id);
    }
}