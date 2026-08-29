package sg.edu.nus.validation.tutorial;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class PhoneNumberValidator
        implements ConstraintValidator<
                ValidPhone,
                String> {


    private String regexp;


    /*
     * Receives the annotation instance.
     *
     * This allows the validator to read
     * configuration supplied through
     * @ValidPhone.
     */
    @Override
    public void initialize(
            ValidPhone ann) {

        this.regexp =
                ann.regexp();
    }


    /*
     * Core validation logic.
     *
     * true  = validation passed
     * false = validation failed
     */
    @Override
    public boolean isValid(

            String value,

            ConstraintValidatorContext ctx) {


        /*
         * Null is treated as valid here.
         *
         * Presence should normally be handled
         * separately by @NotNull/@NotBlank.
         */
        if (value == null) {

            return true;
        }


        return value.matches(
                regexp
        );
    }
}
