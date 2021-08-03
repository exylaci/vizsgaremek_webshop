package webshop.customer;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import webshop.address.Address;
import webshop.address.AddressDto;
import webshop.address.AddressRepository;
import webshop.address.CreateUpdateAddressCommand;

import javax.transaction.Transactional;
import java.lang.reflect.Type;
import java.util.List;

@Service
@AllArgsConstructor
public class CustomerService {
    private CustomerRepository customerRepository;
    private AddressRepository addressRepository;
    private ModelMapper modelMapper;

    public List<CustomerDto> getCustomers() {
        Type targetListType = new TypeToken<List<CustomerDto>>() {
        }.getType();
        List<Customer> customers = customerRepository.findAll();
        return modelMapper.map(customers, targetListType);
    }

    public CustomerDto findCustomer(long id) {
        Customer customer = customerRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("There is no customer with this id: " + id));
        return modelMapper.map(customer, CustomerDto.class);
    }

    public CustomerDto createCustomer(CreateUpdateCustomerCommand command) {
        Address deliveryAddress = addressRepository.findById( command.getDeliveryAddressId()).get();
        Address invoiceAddress = addressRepository.findById( command.getInvoiceAddressId()).get();
        Customer customer = new Customer(
                command.getName(),
                command.getEmail(),
                deliveryAddress,
                invoiceAddress,
                command.getComment());

        customerRepository.save(customer);
//        deliveryAddress.addCustomer(customer);
        return modelMapper.map(customer, CustomerDto.class);
    }

    @Transactional
    public CustomerDto updateCustomer(long id, CreateUpdateCustomerCommand command) {
        Customer customer = customerRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("There is no customer with this id: " + id));
        Address deliveryAddress = addressRepository.findById( command.getDeliveryAddressId()).get();
        Address invoiceAddress = addressRepository.findById( command.getInvoiceAddressId()).get();

        customer.setName(command.getName());
        customer.setEmail(command.getEmail());
        customer.setDeliveryAddressId(deliveryAddress);
        customer.setInvoiceAddressId(invoiceAddress);
        customer.setComment(command.getComment());

        return modelMapper.map(customer, CustomerDto.class);
    }

    public void deleteCustomer(long id) {
        customerRepository.deleteById(id);
    }
}