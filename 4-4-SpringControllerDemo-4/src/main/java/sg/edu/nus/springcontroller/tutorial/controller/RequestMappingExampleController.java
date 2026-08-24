package sg.edu.nus.springcontroller.tutorial.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RequestMappingExampleController {

    // Slide Example 4
    @RequestMapping(
            value = "/something",
            method = RequestMethod.PUT
    )
    @ResponseBody
    public String helloWorld() {
        return "Hello World";
    }


    // ? matches exactly one character
    //
    // Examples:
    // /file1
    // /fileA
    //
    // Does not match:
    // /file12
    @RequestMapping(
            value = "/file?",
            method = RequestMethod.GET
    )
    @ResponseBody
    public String questionMarkPattern() {
        return "? pattern matched";
    }


    // * matches zero or more characters
    // within a single path segment
    //
    // Examples:
    // /document
    // /document1
    // /documentABC
    @RequestMapping(
            value = "/document*",
            method = RequestMethod.GET
    )
    @ResponseBody
    public String singleStarPattern() {
        return "* pattern matched";
    }


    // ** matches zero or more path segments
    //
    // Examples:
    // /files
    // /files/java
    // /files/java/spring
    @RequestMapping(
            value = "/files/**",
            method = RequestMethod.GET
    )
    @ResponseBody
    public String doubleStarPattern() {
        return "** pattern matched";
    }
}