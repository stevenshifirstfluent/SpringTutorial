package sg.edu.nus.thmeleaf.tutorial;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;


@Controller
public class ResourceDemoController {


    @GetMapping("/resource-demo")
    public String resourceDemo(
            Model model) throws Exception {


        /*
         * ClassPathResource represents a resource
         * stored on the application's classpath.
         *
         * The file is located at:
         *
         * src/main/resources/demo/sample.txt
         */
        Resource resource =
                new ClassPathResource(
                        "demo/sample.txt"
                );


        /*
         * exists()
         *
         * Checks whether the resource exists.
         */
        model.addAttribute(
                "resourceExists",
                resource.exists()
        );


        /*
         * isOpen()
         *
         * Checks whether this resource currently
         * represents an open stream.
         *
         * ClassPathResource normally returns false.
         */
        model.addAttribute(
                "resourceOpen",
                resource.isOpen()
        );


        /*
         * getDescription()
         *
         * Returns a human-readable description
         * of the resource.
         */
        model.addAttribute(
                "resourceDescription",
                resource.getDescription()
        );


        /*
         * getInputStream()
         *
         * Opens the resource and returns an
         * InputStream that can be used to
         * read its contents.
         */
        StringBuilder content =
                new StringBuilder();


        try (
                BufferedReader reader =
                        new BufferedReader(
                                new InputStreamReader(
                                        resource.getInputStream(),
                                        StandardCharsets.UTF_8
                                )
                        )
        ) {

            String line;

            while ((line = reader.readLine()) != null) {

                content.append(line)
                        .append(System.lineSeparator());
            }
        }


        model.addAttribute(
                "resourceContent",
                content.toString()
        );


        return "resource-demo";
    }
}
