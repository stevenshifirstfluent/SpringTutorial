package sg.edu.nus.validation.tutorial.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomValidationDemoController {

	/*
     * Loads the validation demonstration page.
     *
     * The HTML file is located at:
     *
     * src/main/resources/static/
     * validation-demo.html
     */
    @GetMapping("/validation-demo")
    public String showValidationDemo() {

        return "validation-demo";
    }
}
