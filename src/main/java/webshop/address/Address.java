package webshop.address;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import webshop.customer.Customer;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "addresses")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "city")
    private String city;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "street_house")
    private String streetHouse;

    @Column(name = "comment")
    private String comment;

    @Column(name = "customer_id")
    @OneToMany
    @JsonIgnore
    private Set<Customer> customers = new HashSet<>();

    public Address(String city, String zipCode, String streetHouse, String comment) {
        this.city = city;
        this.zipCode = zipCode;
        this.streetHouse = streetHouse;
        this.comment = comment;
    }
}