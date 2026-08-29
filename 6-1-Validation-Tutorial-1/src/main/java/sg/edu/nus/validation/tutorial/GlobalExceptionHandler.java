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
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;


@RestControllerAdvice
public class GlobalExceptionHandler {


    /*
     * Handles @Valid / @Validated
     * on @RequestBody.
     *
     * Example:
     *
     * @PostMapping("/register")
     * public ResponseEntity<String> register(
     *         @Valid @RequestBody User user) {
     *     ...
     * }
     */
    @ExceptionHandler(
            MethodArgumentNotValidException.class
    )
    public ResponseEntity<Map<String, Object>>
            handleValidation(

            MethodArgumentNotValidException ex,

            HttpServletRequest req) {


        Map<String, List<String>> fieldErrors =
                new LinkedHashMap<>();


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

                                "path",
                                req.getRequestURI(),

                                "errors",
                                fieldErrors
                        )
                );
    }


    /*
     * Handles @Validated
     * on @RequestParam / @PathVariable.
     *
     * Also commonly used for
     * service-layer method validation.
     */
    @ExceptionHandler(
            ConstraintViolationException.class
    )
    public ResponseEntity<Map<String, Object>>
            handleConstraint(

            ConstraintViolationException ex) {


        List<String> violations =
                ex.getConstraintViolations()
                        .stream()

                        .map(
                                cv ->
                                        cv.getPropertyPath()
                                                + ": "
                                                + cv.getMessage()
                        )

                        .toList();


        return ResponseEntity
                .badRequest()
                .body(
                        Map.of(
                                "status",
                                400,

                                "violations",
                                violations
                        )
                );
    }


    /*
     * Handles @ModelAttribute
     * form binding failures.
     *
     * Note:
     *
     * If the controller declares BindingResult
     * immediately after the validated model,
     * the controller handles the errors directly
     * and BindException will normally not be thrown.
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

                                "path",
                                req.getRequestURI(),

                                "errors",
                                fieldErrors
                        )
                );
    }
    
    @ExceptionHandler(
            HandlerMethodValidationException.class
    )
    public ResponseEntity<Map<String, Object>>
            handleMethodValidation(

            HandlerMethodValidationException ex,

            HttpServletRequest req) {


        List<String> violations =
                new ArrayList<>();


        ex.getParameterValidationResults()
                .forEach(
                        result ->

                                result.getResolvableErrors()
                                        .forEach(
                                                error ->

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

                                "path",
                                req.getRequestURI(),

                                "violations",
                                violations
                        )
                );
    }
}
