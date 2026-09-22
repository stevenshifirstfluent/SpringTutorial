package sg.edu.nus.sessiondemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import sg.edu.nus.sessiondemo.service.ApplicationService;

@Controller
@RequestMapping("/submitted-applications")
public class SubmittedApplicationController {

    private final ApplicationService applicationService;

    public SubmittedApplicationController(
            ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute(
                "applications",
                applicationService.findAll()
        );

        return "submitted-applications";
    }
}
