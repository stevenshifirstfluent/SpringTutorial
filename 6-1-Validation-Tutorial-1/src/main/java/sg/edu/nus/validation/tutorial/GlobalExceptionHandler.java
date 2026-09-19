package sg.edu.nus.validation.tutorial;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;


/*
 * @RestControllerAdvice
 *
 * Defines a global exception handler for
 * REST controllers in the application.
 *
 * It combines:
 *
 * @ControllerAdvice
 *      -> applies exception handling
 *         across multiple controllers
 *
 * and
 *
 * @ResponseBody
 *      -> writes the returned Java object
 *         directly into the HTTP response body.
 *
 * Therefore, methods in this class can return
 * Map<String, Object>, and Spring converts the
 * Map into JSON automatically.
 *
 * Example:
 *
 * {
 *     "status": 400,
 *     "errors": {...}
 * }
 */
@RestControllerAdvice
public class GlobalExceptionHandler {


    /*
     * @ExceptionHandler(
     *     MethodArgumentNotValidException.class
     * )
     *
     * Tells Spring:
     *
     * "If a MethodArgumentNotValidException
     *  occurs in a controller, call this method."
     *
     *
     * MethodArgumentNotValidException commonly
     * occurs when:
     *
     *     @Valid
     *     @RequestBody
     *     User user
     *
     * fails Bean Validation.
     *
     *
     * Example:
     *
     * @PostMapping("/register")
     * public ResponseEntity<String> register(
     *
     *         @Valid
     *         @RequestBody
     *         User user) {
     *
     *     ...
     * }
     *
     *
     * If User contains:
     *
     * @Email
     * private String email;
     *
     * and the client sends:
     *
     * "email": "wrong-email"
     *
     * validation fails and Spring may throw
     * MethodArgumentNotValidException.
     */
    @ExceptionHandler(
            MethodArgumentNotValidException.class
    )
    public ResponseEntity<Map<String, Object>>
            handleValidation(

            MethodArgumentNotValidException ex,

            /*
             * HttpServletRequest
             *
             * Gives access to information about
             * the current HTTP request.
             *
             * Here it is used to obtain:
             *
             * req.getRequestURI()
             *
             * so the error response can show
             * which endpoint failed.
             */
            HttpServletRequest req) {


        /*
         * Store validation errors by field.
         *
         * Example:
         *
         * {
         *     "email": [
         *         "Must be a valid email address"
         *     ],
         *
         *     "password": [
         *         "Password must be at least 8 chars",
         *         "Password needs uppercase and digit"
         *     ]
         * }
         *
         * A List<String> is used because one field
         * may fail more than one validation rule.
         */
        Map<String, List<String>> fieldErrors =
                new LinkedHashMap<>();


        /*
         * getBindingResult()
         *
         * Returns the validation/binding result
         * produced while Spring was processing
         * the request body.
         *
         * getFieldErrors()
         *
         * Returns validation errors associated
         * with specific fields.
         */
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(

                        fe ->

                                fieldErrors
                                        .computeIfAbsent(

                                                /*
                                                 * fe.getField()
                                                 *
                                                 * Example:
                                                 *
                                                 * "email"
                                                 * "password"
                                                 * "phoneNumber"
                                                 */
                                                fe.getField(),

                                                k ->
                                                        new ArrayList<>()
                                        )

                                        /*
                                         * getDefaultMessage()
                                         *
                                         * Returns the message declared
                                         * in the validation annotation.
                                         *
                                         * Example:
                                         *
                                         * @Email(
                                         *     message =
                                         *       "Must be a valid email address"
                                         * )
                                         */
                                        .add(
                                                fe.getDefaultMessage()
                                        )
                );


        /*
         * ResponseEntity.badRequest()
         *
         * Creates an HTTP response with:
         *
         * status code = 400 Bad Request
         *
         *
         * body(...)
         *
         * Defines the JSON response body.
         */
        return ResponseEntity
                .badRequest()
                .body(

                        Map.of(

                                "timestamp",
                                Instant.now(),

                                "status",
                                400,

                                "exception",
                                "MethodArgumentNotValidException",

                                "path",
                                req.getRequestURI(),

                                "errors",
                                fieldErrors
                        )
                );
    }


    /*
     * @ExceptionHandler(
     *     ConstraintViolationException.class
     * )
     *
     * Handles method-level Bean Validation
     * failures that produce a
     * ConstraintViolationException.
     *
     *
     * A common example is validation on a
     * Spring service annotated with @Validated:
     *
     * @Service
     * @Validated
     * public class ValidationDemoService {
     *
     *     public String checkUserId(
     *
     *         @Min(1)
     *         Long id) {
     *
     *         ...
     *     }
     * }
     *
     *
     * Calling:
     *
     * checkUserId(0L)
     *
     * violates @Min(1).
     *
     * Spring's method validation can then raise
     * ConstraintViolationException.
     */
    @ExceptionHandler(
            ConstraintViolationException.class
    )
    public ResponseEntity<Map<String, Object>>
            handleConstraint(

            ConstraintViolationException ex,

            HttpServletRequest req) {


        /*
         * getConstraintViolations()
         *
         * Returns all validation rules that failed.
         *
         * Each ConstraintViolation contains:
         *
         * - property path
         * - invalid value
         * - validation message
         */
        List<String> violations =

                ex.getConstraintViolations()
                        .stream()

                        .map(

                                cv ->

                                        /*
                                         * getPropertyPath()
                                         *
                                         * Describes where the validation
                                         * failure occurred.
                                         *
                                         * Example:
                                         *
                                         * checkUserId.id
                                         *
                                         *
                                         * getMessage()
                                         *
                                         * Returns the message from:
                                         *
                                         * @Min(
                                         *     value = 1,
                                         *     message =
                                         *       "Service user id must be at least 1"
                                         * )
                                         */
                                        cv.getPropertyPath()
                                                + ": "
                                                + cv.getMessage()
                        )

                        .toList();


        return ResponseEntity
                .badRequest()
                .body(

                        Map.of(

                                "timestamp",
                                Instant.now(),

                                "status",
                                400,

                                "exception",
                                "ConstraintViolationException",

                                "path",
                                req.getRequestURI(),

                                "violations",
                                violations
                        )
                );
    }


    /*
     * @ExceptionHandler(
     *     BindException.class
     * )
     *
     * Handles Spring data-binding failures
     * represented by BindException.
     *
     *
     * Data binding means converting incoming
     * request values into Java object properties.
     *
     * Example:
     *
     * Incoming value:
     *
     * age = "abc"
     *
     * Java property:
     *
     * private Integer age;
     *
     *
     * Spring cannot convert:
     *
     * "abc"
     *
     * into:
     *
     * Integer
     *
     * so a binding error is created.
     *
     *
     * Important:
     *
     * Binding errors are not always the same as
     * Bean Validation errors.
     *
     * Example:
     *
     * @Min(18)
     *
     * checks whether a valid Integer is large enough.
     *
     * BindException may happen earlier if Spring
     * cannot even convert the input into an Integer.
     */
    @ExceptionHandler(
            BindException.class
    )
    public ResponseEntity<Map<String, Object>>
            handleBindException(

            BindException ex,
            HttpServletRequest req) {


        Map<String, List<String>> fieldErrors =
                new LinkedHashMap<>();


        /*
         * BindException also contains a
         * BindingResult.
         *
         * Therefore, field-level binding errors
         * can be extracted in a very similar way
         * to MethodArgumentNotValidException.
         */
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(

                        fe ->

                                fieldErrors
                                        .computeIfAbsent(
                                                fe.getField(),
                                                k ->
                                                        new ArrayList<>()
                                        )
                                        .add(
                                                fe.getDefaultMessage()
                                        )
                );


        return ResponseEntity
                .badRequest()
                .body(

                        Map.of(

                                "timestamp",
                                Instant.now(),

                                "status",
                                400,

                                "exception",
                                "BindException",

                                "path",
                                req.getRequestURI(),

                                "errors",
                                fieldErrors
                        )
                );
    }


    /*
     * @ExceptionHandler(
     *     HandlerMethodValidationException.class
     * )
     *
     * Handles Spring MVC method-parameter
     * validation failures.
     *
     *
     * This commonly occurs when constraints
     * are declared directly on controller
     * method parameters.
     *
     * Example:
     *
     * @GetMapping("/{id}")
     * public ResponseEntity<String> getUser(
     *
     *         @PathVariable
     *
     *         @Min(
     *             value = 1,
     *             message =
     *               "User id must be at least 1"
     *         )
     *
     *         Long id) {
     *
     *     ...
     * }
     *
     *
     * Request:
     *
     * GET /api/users/0
     *
     *
     * Since:
     *
     * 0 < 1
     *
     * validation fails before the controller
     * method completes normally.
     */
    @ExceptionHandler(
            HandlerMethodValidationException.class
    )
    public ResponseEntity<Map<String, Object>>
            handleMethodValidation(

            HandlerMethodValidationException ex,
            HttpServletRequest req) {


        List<String> violations =
                new ArrayList<>();


        /*
         * getParameterValidationResults()
         *
         * Returns validation results for
         * controller method parameters.
         *
         * For example:
         *
         * @PathVariable Long id
         *
         * or
         *
         * @RequestParam String name
         */
        ex.getParameterValidationResults()
                .forEach(

                        result ->

                                /*
                                 * getResolvableErrors()
                                 *
                                 * Returns the validation errors
                                 * associated with that parameter.
                                 */
                                result.getResolvableErrors()
                                        .forEach(

                                                error ->

                                                        /*
                                                         * getDefaultMessage()
                                                         *
                                                         * Returns the message
                                                         * declared in the
                                                         * validation annotation.
                                                         */
                                                        violations.add(
                                                                error.getDefaultMessage()
                                                        )
                                        )
                );


        return ResponseEntity
                .badRequest()
                .body(

                        Map.of(

                                "timestamp",
                                Instant.now(),

                                "status",
                                400,

                                "exception",
                                "HandlerMethodValidationException",

                                "path",
                                req.getRequestURI(),

                                "violations",
                                violations
                        )
                );
    }
}