package sg.edu.nus.restful.session1.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.nus.restful.session1.dto.ProductResponse;
import sg.edu.nus.restful.session1.service.DemoDataService;

/**
 * Classroom helper endpoint. It is deliberately separated from the Product API
 * so students do not confuse data reset with a REST business operation.
 */
@RestController
@RequestMapping("/api/demo")
public class DemoController {

    private final DemoDataService demoDataService;

    public DemoController(DemoDataService demoDataService) {
        this.demoDataService = demoDataService;
    }

    @PostMapping("/reset")
    public List<ProductResponse> reset() {
        return demoDataService.reset();
    }
}
