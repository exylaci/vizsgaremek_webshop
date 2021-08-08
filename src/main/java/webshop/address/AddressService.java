package webshop.address;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import webshop.customer.Customer;
import webshop.exception.NotFindException;

import javax.transaction.Transactional;
import java.lang.reflect.Type;
import java.util.List;

@Service
@AllArgsConstructor
public class AddressService {
    private AddressRepository repository;
    private ModelMapper modelMapper;

    public List<AddressDto> getAddresses() {
        Type targetListType = new TypeToken<List<AddressDto>>() {
        }.getType();
        List<Address> addresses = repository.findAll();
        return modelMapper.map(addresses, targetListType);
    }

    public AddressDto findAddress(long id) {
        Address address = repository
                .findById(id)
                .orElseThrow(() -> new NotFindException("/api/addresses", "There is no address with this id: " + id));
        return modelMapper.map(address, AddressDto.class);
    }

    public AddressDto createAddress(CreateUpdateAddressCommand command) {
        Address address = new Address(
                command.getCity(),
                command.getZipCode(),
                command.getStreetHouse(),
                command.getComment());
        repository.save(address);
        return modelMapper.map(address, AddressDto.class);
    }

    @Transactional
    public AddressDto updateAddress(long id, CreateUpdateAddressCommand command) {
        Address address = repository
                .findById(id)
                .orElseThrow(() -> new NotFindException("/api/addresses", "There is no address with this id: " + id));

        address.setCity(command.getCity());
        address.setZipCode(command.getZipCode());
        address.setStreetHouse(command.getStreetHouse());
        address.setComment(command.getComment());

        return modelMapper.map(address, AddressDto.class);
    }

    @Transactional
    public void addCustomer(Customer customer) {
        Long id = customer.getId();
        ;
        Address address = repository
                .findById(id)
                .orElseThrow(() -> new NotFindException("/api/addresses", "There is no address with this id: " + id));

        address.getCustomers().add(customer);
    }

    public void deleteAddress(long id) {
        repository.deleteById(id);
    }

    public boolean codeValidator(String code) {
        if (code.length() != 4) {
            return false;
        }
        for (int i = 0; i < 4; ++i) {
            if (!Character.isDigit(code.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}