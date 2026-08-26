package sg.edu.nus.validation.tutorial;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Documented

@Constraint(
        validatedBy =
                PhoneNumberValidator.class
)

@Target({
        ElementType.FIELD,
        ElementType.PARAMETER
})

@Retention(
        RetentionPolicy.RUNTIME
)

public @interface ValidPhone {


    String message()
            default "Invalid phone number";


    Class<?>[] groups()
            default {};


    Class<? extends Payload>[] payload()
            default {};


    /*
     * Allows each use of @ValidPhone
     * to configure its own regular expression.
     */
    String regexp()
            default "^\\+?[0-9]{7,15}$";
}
