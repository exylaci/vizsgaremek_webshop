package webshop.customer;

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
import webshop.order.CreateOrderCommand;
import webshop.order.OrderDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(statements = "delete from addresses_customers")
@Sql(statements = "delete from ordered_products")
@Sql(statements = "delete from orders")
@Sql(statements = "delete from customers")
@Sql(statements = "delete from addresses")
public class CustomerControllerIT {
    @Autowired
    TestRestTemplate template;

    AddressDto addressBudapest;
    AddressDto addressGyor;
    CustomerDto customerJohn;
    CustomerDto customerJane;
    Long id;

    @BeforeEach
    void init() {
        addressBudapest = template.postForObject(
                "/api/addresses",
                new CreateUpdateAddressCommand("Budapest", "1000", "Main street 1, 1st floor 4th door.", "doorbell: 14"),
                AddressDto.class);
        addressGyor=   template.postForObject("/api/addresses",
                new CreateUpdateAddressCommand("Győr", "9000", "Main street 2", null),
                AddressDto.class);
        customerJohn = template.postForObject(
                "/api/customers",
                new CreateUpdateCustomerCommand("John Doe", "john.doe@mailserver.hu", addressBudapest.getId(), addressBudapest.getId(), "Just call me Jonny!"),
                CustomerDto.class);
        customerJane = template.postForObject(
                "/api/customers",
                new CreateUpdateCustomerCommand("Jane Doe", "jane.doe@mailserver.hu", addressBudapest.getId(), addressBudapest.getId(), null),
                CustomerDto.class);
        id=customerJohn.getId();
    }

    @Test
    void testGetCustomers() {
        List<CustomerDto> expected = template.exchange(
                "/api/customers",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CustomerDto>>() {
                }).getBody();

        assertThat(expected)
                .extracting(CustomerDto::getName)
                .containsExactlyInAnyOrder("John Doe", "Jane Doe");
    }

    @Test
    void testGetCustomer() {
        CustomerDto expected = template.exchange(
                "/api/customers/" + id,
                HttpMethod.GET,
                null,
                CustomerDto.class)
                .getBody();

        assertEquals(customerJohn.getName(), expected.getName());
        assertEquals(customerJohn.getEmail(), expected.getEmail());
        assertEquals(customerJohn.getComment(), expected.getComment());
        assertEquals(customerJohn.getDeliveryAddressId(), expected.getDeliveryAddressId());
        assertEquals(customerJohn.getInvoiceAddressId(), expected.getInvoiceAddressId());
    }

    @Test
    void testUpdateCustomer() {
        template.put(
                "/api/customers/" + id,
                new CreateUpdateCustomerCommand(
                        "Mr.Nobody",
                        "a@b.cd",
                        addressGyor.getId(),
                        addressBudapest.getId(),
                        "message"),
                CustomerDto.class);

        CustomerDto expected = template.exchange(
                "/api/customers/" + id,
                HttpMethod.GET,
                null,
                CustomerDto.class).getBody();

        assertEquals("Mr.Nobody", expected.getName());
        assertEquals("a@b.cd", expected.getEmail());
        assertEquals(addressGyor.getCity(), expected.getDeliveryAddressId().getCity());
        assertEquals(addressBudapest.getZipCode(), expected.getInvoiceAddressId().getZipCode());
        assertEquals("message", expected.getComment());
    }

    @Test
    void testDeleteCustomer() {
        template.delete("/api/customers/" + id);

        List<CustomerDto> expected = template.exchange(
                "/api/customers",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CustomerDto>>() {
                }).getBody();

        assertThat(expected)
                .hasSize(1)
                .extracting(CustomerDto::getId)
                .doesNotContain(id);
    }

    @Test
    void testDeleteUsedCustomer() {
         template.postForObject(
                "/api/orders",
                new CreateOrderCommand(id),
                OrderDto.class);

        template.delete("/api/customers/" + id);

        List<CustomerDto> expected = template.exchange(
                "/api/customers",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CustomerDto>>() {
                }).getBody();

        assertEquals(2,expected.size());
    }
}