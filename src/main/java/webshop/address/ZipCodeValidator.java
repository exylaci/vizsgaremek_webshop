package webshop.address;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ZipCodeValidator implements ConstraintValidator<IsValidZipCode, String> {

    private AddressService service;

    public ZipCodeValidator(AddressService service) {
        this.service = service;
    }

    @Override
    public boolean isValid(String code, ConstraintValidatorContext context) {
        return service.codeValidator(code);
    }
}
