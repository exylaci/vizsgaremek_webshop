package webshop.customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import webshop.address.AddressDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {
    private Long id;
    private String name;
    private String email;
    private AddressDto deliveryAddressId;
    private AddressDto invoiceAddressId;
    private String comment;
}