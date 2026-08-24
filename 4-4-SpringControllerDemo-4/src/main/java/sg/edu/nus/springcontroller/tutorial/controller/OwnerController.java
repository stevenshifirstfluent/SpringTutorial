package sg.edu.nus.springcontroller.tutorial.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import sg.edu.nus.springcontroller.tutorial.model.Owner;
import sg.edu.nus.springcontroller.tutorial.service.OwnerService;

@Controller
@RequestMapping("/owners")
public class OwnerController {

    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    // Slide example 1
    // Get http://localhost:8081/owners/owners/101
    @RequestMapping(
            value = "/owners/{ownerId}",
            method = RequestMethod.GET
    )
    public String findOwner(
            @PathVariable Long ownerId,
            @ModelAttribute Owner owner) {

        Owner foundOwner =
                ownerService.findOwner(ownerId);

        owner.setId(foundOwner.getId());
        owner.setName(foundOwner.getName());

        return "displayOwner";
    }
    
    
    // Slide example 2
    // http://localhost:8081/owners/spring-web/spring-mvc-6.2.1.jar
    @RequestMapping(
            value = "/spring-web/"
                    + "{symbolicName:[a-z-]+}-"
                    + "{version:\\d+\\.\\d+\\.\\d+}"
                    + "{extension:\\.[a-z]+}",
            method = RequestMethod.GET
    )
    @ResponseBody
    public String handle(
            @PathVariable String symbolicName,
            @PathVariable String version,
            @PathVariable String extension) {

        return "symbolicName = " + symbolicName
                + ", version = " + version
                + ", extension = " + extension;
    }
    
    // 1. Path variable
    @GetMapping("/{ownerId}")
    public String getOwner(
            @PathVariable Long ownerId) {

        return "Owner ID: " + ownerId;
    }


    // 2. Multiple path variables
    @GetMapping("/{ownerId}/pets/{petId}")
    public String getPet(
            @PathVariable Long ownerId,
            @PathVariable Long petId) {

        return "Owner: " + ownerId
                + ", Pet: " + petId;
    }


    // 3. Query parameter
    @GetMapping
    public String searchOwner(
            @RequestParam String name) {

        return "Searching owner: " + name;
    }


    // 4. HTTP method + request body
    @PostMapping
    public String createOwner(
            @RequestBody Owner owner) {

        return "Created owner: "
                + owner.getName();
    }
}
