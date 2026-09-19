package sg.edu.nus.validation.tutorial;

import jakarta.validation.constraints.Min;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;


/*
 * @Service:
 *
 * Registers this class as a Spring-managed bean.
 *
 * This is important because Spring needs to manage
 * the object in order to apply method validation.
 */
@Service


/*
 * @Validated:
 *
 * Enables validation for method parameters
 * and return values on this Spring bean.
 *
 * The validation annotations such as @Min,
 * @NotBlank and @Size only define validation rules.
 *
 * @Validated tells Spring to actually check
 * those rules when the service method is called.
 *
 * Example:
 *
 * validationDemoService.checkUserId(0L);
 *
 * Spring checks @Min(1) before the method
 * is allowed to complete normally.
 *
 * If validation fails, a
 * ConstraintViolationException is raised.
 */
@Validated
public class ValidationDemoService {


    /*
     * @Min defines the validation rule:
     *
     * id must be greater than or equal to 1.
     *
     * Because this class is annotated with
     * @Validated, Spring performs method-level
     * validation when this method is invoked
     * through the Spring-managed bean.
     */
    public String checkUserId(

            @Min(
                    value = 1,
                    message =
                            "Service user id must be at least 1"
            )
            Long id) {


        /*
         * This line is reached only when
         * method validation passes.
         *
         * For example:
         *
         * id = 1
         *     -> validation passes
         *     -> method continues
         *
         * id = 0
         *     -> validation fails
         *     -> ConstraintViolationException
         *     -> method does not continue normally
         */
        return "Service accepted user id = "
                + id;
    }
}