package webshop.customer;

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
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_name")
    private String name;

    @Column(name = "email_address")
    private String email;

    @ManyToOne
    @JoinColumn(name = "delivery_address_id")
    private Address deliveryAddressId;

    @ManyToOne
    @JoinColumn(name = "invoice_address_id")
    private Address invoiceAddressId;

    @Column(name = "comment")
    private String comment;

    public Customer(String name, String email, Address deliveryAddressId, Address invoiceAddressId, String comment) {
        this.name = name;
        this.email = email;
        this.deliveryAddressId = deliveryAddressId;
        this.invoiceAddressId = invoiceAddressId;
        this.comment = comment;
    }
}