package webshop.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import webshop.address.Address;

import javax.persistence.*;

@Entity
@Table(name = "customers")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Tag(name = "Operations on customer.")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_name")
    @Schema(example = "John Doe", description = "Name of the customer.")
    private String name;

    @Column(name = "email_address")
    @Schema(example = "john.doe@mailserver.hu", description = "Email address of the customer.")
    private String email;

//    @Column(name = "delivery_address_id")
//    @Schema(example = 1, description = "Where the product has to be delivered. Id from the addresses data table.")
    @ManyToOne
    @JoinColumn(name = "delivery_address_id")
    private Address deliveryAddressId;

//    @Column(name = "invoice_address_id")
//    @Schema(example = 1, description = "What address has to be on the invoice. Id from the addresses data table.")
    @ManyToOne
    @JoinColumn(name = "invoice_address_id")
    private Address invoiceAddressId;

    @Column(name = "comment")
    @Schema(example = "It's a comment about the customer.", description = "Comment related to the customer.")
    private String comment;

    public Customer(String name, String email, Address deliveryAddressId, Address invoiceAddressId, String comment) {
        this.name = name;
        this.email = email;
        this.deliveryAddressId = deliveryAddressId;
        this.invoiceAddressId = invoiceAddressId;
        this.comment = comment;
    }
}