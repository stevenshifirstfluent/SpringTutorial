package sg.edu.nus.restful.session1.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import sg.edu.nus.restful.session1.service.DemoDataService;

@Component
public class DemoDataInitializer implements ApplicationRunner {

    private final DemoDataService demoDataService;

    public DemoDataInitializer(DemoDataService demoDataService) {
        this.demoDataService = demoDataService;
    }

    @Override
    public void run(ApplicationArguments args) {
        demoDataService.reset();
    }
}
