package sg.edu.nus.session.tutorial;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import org.springframework.web.bind.support.SessionStatus;


@Controller

/*
 * Any model attribute named "pet" is automatically
 * stored in the HTTP session.
 *
 * The attribute remains available across subsequent
 * requests handled by this controller.
 */
@SessionAttributes("pet")
public class EditPetForm {


    /*
     * Creates the "pet" model attribute when one
     * does not already exist in the session.
     *
     * Because the controller has:
     *
     * @SessionAttributes("pet")
     *
     * Spring also promotes this model attribute
     * into the HTTP session.
     */
    @ModelAttribute("pet")
    public Pet pet() {

        return new Pet(
                1L,
                "Milo",
                "Cat",
                3
        );
    }


    /*
     * Step 1:
     *
     * Display the form.
     *
     * The Pet was created by the @ModelAttribute
     * method above and is available in the model.
     *
     * @SessionAttributes causes it to also be
     * stored in the session.
     */
    @GetMapping("/pets/form")
    public String editForm(

            @ModelAttribute("pet")
            Pet pet) {


        return "pet-form";
    }


    /*
     * Step 2:
     *
     * Handle the submitted form.
     *
     * Notice that we do NOT need to manually
     * read the Pet from HttpSession.
     *
     * Spring restores the "pet" session attribute
     * back into the model before binding the
     * submitted form values.
     */
    @PostMapping("/pets/{id}")
    public String handle(

            @PathVariable
            Long id,

            @Valid
            @ModelAttribute("pet")
            Pet pet,

            BindingResult errors) {


        /*
         * Validation failed.
         *
         * The same Pet remains in the model/session
         * and the form is rendered again.
         */
        if (errors.hasErrors()) {

            return "pet-form";
        }


        /*
         * Keep the path ID associated with
         * the submitted Pet.
         */
        pet.setId(id);


        /*
         * Redirect to another request.
         *
         * The Pet survives the redirect because
         * it is stored as a session attribute.
         */
        return "redirect:/pets/review";
    }


    /*
     * Step 3:
     *
     * This is a NEW HTTP request.
     *
     * The Pet is still available because
     * @SessionAttributes stored it in the session.
     */
    @GetMapping("/pets/review")
    public String review(

            @ModelAttribute("pet")
            Pet pet,

            Model model) {


        model.addAttribute(
                "message",
                "Pet was restored from the session."
        );


        return "pet-review";
    }


    /*
     * Step 4:
     *
     * Tell Spring that the conversational
     * session work is complete.
     *
     * setComplete() clears attributes managed
     * by @SessionAttributes for this controller.
     *
     * It does NOT invalidate the entire HttpSession.
     */
    @PostMapping("/pets/complete")
    public String complete(

            SessionStatus status) {


        status.setComplete();


        return "redirect:/pets/done";
    }


    /*
     * Step 5:
     *
     * Display a completion page.
     *
     * The previous "pet" session attribute
     * managed by @SessionAttributes has now
     * been cleared.
     */
    @GetMapping("/pets/done")
    public String done() {

        return "pet-complete";
    }


    /*
     * Optional demo endpoint.
     *
     * @SessionAttribute reads directly from
     * the HTTP session to the method parameter.
     *
     * Use:
     *
     * GET /pets/session-check
     *
     * before status.setComplete().
     */
    @GetMapping("/pets/session-check")
    public String sessionCheck(

            @SessionAttribute("pet")
            Pet pet,

            Model model) {


        model.addAttribute(
                "sessionPet",
                pet
        );


        return "pet-review";
    }
}