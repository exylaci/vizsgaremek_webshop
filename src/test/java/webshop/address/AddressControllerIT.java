package webshop.address;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(statements = "delete from addresses")
public class AddressControllerIT {
    @Autowired
    TestRestTemplate template;

    Long id;

    @BeforeEach
    void init() {
        AddressDto addressDto = template.postForObject("/api/addresses",
                new CreateUpdateAddressCommand("Budapest", "1000", "Main street 1, 1st floor 4th door.", "doorbell: 14"),
                AddressDto.class);
        id = addressDto.getId();

        template.postForObject("/api/addresses",
                new CreateUpdateAddressCommand("Győr", "9000", "Main street 2", null),
                AddressDto.class);
    }

    @Test
    void testGetAddresses() {
        List<AddressDto> expected = template.exchange(
                "/api/addresses",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<AddressDto>>() {
                }).getBody();

        assertThat(expected)
                .extracting(AddressDto::getCity)
                .containsExactly("Budapest", "Győr");
    }

    @Test
    void testGetAddress() {
        AddressDto expected = template.exchange(
                "/api/addresses/" + id,
                HttpMethod.GET,
                null,
                AddressDto.class)
                .getBody();

        assertEquals("Budapest", expected.getCity());
        assertEquals("1000", expected.getZipCode());
        assertEquals("Main street 1, 1st floor 4th door.", expected.getStreetHouse());
        assertEquals("doorbell: 14", expected.getComment());
    }

    @Test
    void testUpdateAddress() {
        template.put(
                "/api/addresses/" + id,
                new CreateUpdateAddressCommand(
                        "Pécs",
                        "6000",
                        "Uránváros, Csile street 3.", "doorbell: 2"),
                AddressDto.class);

        AddressDto expected = template.exchange(
                "/api/addresses/" + id,
                HttpMethod.GET,
                null,
                AddressDto.class).getBody();

        assertEquals("Pécs", expected.getCity());
        assertEquals("6000", expected.getZipCode());
        assertEquals("Uránváros, Csile street 3.", expected.getStreetHouse());
        assertEquals("doorbell: 2", expected.getComment());
    }

    @Test
    void testDeleteAddress() {
        template.delete("/api/addresses/" + id);

        List<AddressDto> expected = template.exchange(
                "/api/addresses",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<AddressDto>>() {
                }).getBody();

        assertThat(expected)
                .extracting(AddressDto::getId)
                .doesNotContain(id);
    }
}