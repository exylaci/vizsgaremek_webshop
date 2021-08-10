package webshop.category;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestClientException;
import webshop.address.AddressDto;
import webshop.address.CreateUpdateAddressCommand;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(statements = "delete from ordered_products")
@Sql(statements = "delete from orders")
@Sql(statements = "delete from ratings")
@Sql(statements = "delete from products")
@Sql(statements = "delete from product_category_types")
public class CategoryControllerIT {
    @Autowired
    TestRestTemplate template;

    Long id;

    @BeforeEach
    void init() {
        ProductCategoryTypeDto productCategoryTypeDto = template.postForObject(
                "/api/productcategorytypes",
                new CreateUpdateProductCategoryTypeCommand("entertainment electronics", "Electronic equipment that used for entertainment at home."),
                ProductCategoryTypeDto.class);
        id = productCategoryTypeDto.getId();
        template.postForObject(
                "/api/productcategorytypes",
                new CreateUpdateProductCategoryTypeCommand("office materials", "Goods that need for every offices."),
                ProductCategoryTypeDto.class);
    }

    @Test
    void testGetProductCategoryTypes() {
        List<ProductCategoryTypeDto> expected = template.exchange(
                "/api/productcategorytypes",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ProductCategoryTypeDto>>() {
                }).getBody();

        assertThat(expected)
                .extracting(ProductCategoryTypeDto::getProductCategoryType)
                .containsExactlyInAnyOrder("entertainment electronics", "office materials");
    }

    @Test
    void testGetProductCategoryType() {
        ProductCategoryTypeDto expected = template.exchange(
                "/api/productcategorytypes/" + id,
                HttpMethod.GET,
                null,
                ProductCategoryTypeDto.class)
                .getBody();

        assertEquals("entertainment electronics", expected.getProductCategoryType());
        assertEquals("Electronic equipment that used for entertainment at home.", expected.getDescription());
    }

    @Test
    void testUpdateProductCategoryType() {
        template.put(
                "/api/productcategorytypes/" + id,
                new CreateUpdateProductCategoryTypeCommand(
                        "house keeping",
                        "cleaning materials"),
                ProductCategoryTypeDto.class);

        ProductCategoryTypeDto expected = template.exchange(
                "/api/productcategorytypes/" + id,
                HttpMethod.GET,
                null,
                ProductCategoryTypeDto.class).getBody();

        assertEquals("house keeping", expected.getProductCategoryType());
        assertEquals("cleaning materials", expected.getDescription());
    }

    @Test
    void testDeleteProductCategoryType() {
        template.delete("/api/productcategorytypes/" + id);

        List<ProductCategoryTypeDto> expected = template.exchange(
                "/api/productcategorytypes",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ProductCategoryTypeDto>>() {
                }).getBody();

        assertThat(expected)
                .extracting(ProductCategoryTypeDto::getId)
                .doesNotContain(id);
    }

    @RepeatedTest(value = 3)
    void testZipCodeValidator(RepetitionInfo info) {
        int round = info.getCurrentRepetition() - 1;
        String[] zipCodes = {"123", "12345", "abcd"};

        assertThrows(RestClientException.class, () ->
                template.postForObject(
                        "/api/productcategorytypes",
                        new CreateUpdateAddressCommand("Győr", zipCodes[round], "Main street 2", null),
                        AddressDto.class));
    }
}