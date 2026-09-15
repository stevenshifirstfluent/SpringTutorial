package sg.edu.nus.springcontroller.tutorial.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import sg.edu.nus.springcontroller.tutorial.model.Pet;

@Controller
@RequestMapping("/owners/{ownerId}")
public class RelativePathUriTemplateController {

    /*
     * Slide Example 3
     *
     * Class-level mapping:
     * /owners/{ownerId}
     *
     * Method-level mapping:
     * /pets/{petId}
     *
     * Required query parameter:
     * myParam=myValue
     *
     * Complete request:
     *
     * GET /owners/100/pets/10?myParam=myValue
     */
    @RequestMapping(
            value = "/pets/{petId}",
            method = RequestMethod.GET,
            params = "myParam=myValue"
    )
    @ResponseBody
    public String findPet(
            @PathVariable String ownerId,
            @PathVariable String petId,
            @ModelAttribute Pet pet) {

        // Populate the @ModelAttribute object
        pet.setId(Long.valueOf(petId));
        pet.setName("Lucky");
        pet.setOwnerId(Long.valueOf(ownerId));

        return "Owner ID: " + ownerId
                + ", Pet ID: " + petId
                + ", Pet Name: " + pet.getName();
    }
}