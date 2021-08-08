package webshop.address;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = ZipCodeValidator.class)
public @interface IsValidZipCode {
    String message() default "invalid zip code";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}