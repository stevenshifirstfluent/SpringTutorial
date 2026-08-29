package sg.edu.nus.validation.tutorial;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import org.springframework.http.ResponseEntity;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/users")
public class UserController {


    private final UserService userService;


    public UserController(
            UserService userService) {

        this.userService =
                userService;
    }


    /*
     * @Valid:
     *
     * Jakarta Bean Validation standard.
     *
     * Executes the normal validation rules
     * defined on User.
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(

            @Valid
            @RequestBody
            User user,

            BindingResult result) {


        if (result.hasErrors()) {


            /*
             * Collect validation errors.
             */
            List<String> errors =
                    result
                            .getFieldErrors()
                            .stream()

                            .map(
                                    e ->
                                            e.getField()
                                                    + ": "
                                                    + e.getDefaultMessage()
                            )

                            .collect(
                                    Collectors.toList()
                            );


            return ResponseEntity
                    .badRequest()
                    .body(
                            errors.toString()
                    );
        }


        /*
         * All validations passed.
         */
        userService.save(
                user
        );


        return ResponseEntity.ok(
                "User registered!"
        );
    }


    /*
     * @Validated:
     *
     * Spring validation annotation.
     *
     * Allows a validation group to be
     * specified.
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> update(

            @PathVariable
            Long id,

            @Validated(Update.class)
            @RequestBody
            User user) {


        return ResponseEntity.ok(
                userService.update(
                        id,
                        user
                )
        );
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<String> getUser(

            @PathVariable
            @Min(
                    value = 1,
                    message = "User id must be at least 1"
            )
            Long id) {

        return ResponseEntity.ok(
                "User id = " + id
        );
    }
}
