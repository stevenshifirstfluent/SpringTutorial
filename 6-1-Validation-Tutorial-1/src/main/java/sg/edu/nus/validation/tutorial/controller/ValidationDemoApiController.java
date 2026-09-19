package sg.edu.nus.validation.tutorial.controller;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.nus.validation.tutorial.ValidationDemoService;


@RestController
@RequestMapping("/api/validation")
public class ValidationDemoApiController {


    private final ValidationDemoService
            validationDemoService;


    public ValidationDemoApiController(

            ValidationDemoService
                    validationDemoService) {

        this.validationDemoService =
                validationDemoService;
    }


    /*
     * Demonstrates:
     *
     * ConstraintViolationException
     *
     * Validation occurs on a
     * @Validated service.
     */
    @GetMapping("/service/{id}")
    public ResponseEntity<String>
            serviceValidation(

            @org.springframework.web.bind.annotation.PathVariable
            Long id) {


        return ResponseEntity.ok(

                validationDemoService
                        .checkUserId(id)

        );
    }


    /*
     * Demonstrates:
     *
     * BindException
     *
     * The UI deliberately sends:
     *
     * age = abc
     *
     * which cannot be converted
     * into Integer.
     */
    @PostMapping("/bind")
    public ResponseEntity<String>
            bindingDemo(

            @RequestParam
            String age)

            throws BindException {


        AgeForm form =
                new AgeForm();


        DataBinder binder =
                new DataBinder(
                        form
                );


        MutablePropertyValues values =
                new MutablePropertyValues();


        values.add(
                "age",
                age
        );


        binder.bind(
                values
        );


        if (
                binder
                        .getBindingResult()
                        .hasErrors()
        ) {

            throw new BindException(
                    binder.getBindingResult()
            );
        }


        return ResponseEntity.ok(

                "Age successfully bound = "
                        + form.getAge()

        );
    }



    public static class AgeForm {


        private Integer age;


        public Integer getAge() {

            return age;
        }


        public void setAge(
                Integer age) {

            this.age = age;
        }
    }
}