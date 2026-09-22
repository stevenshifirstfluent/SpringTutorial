package sg.edu.nus.sessiondemo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import sg.edu.nus.sessiondemo.model.ApplicationDraft;
import sg.edu.nus.sessiondemo.model.SubmittedApplication;
import sg.edu.nus.sessiondemo.service.ApplicationService;

@Controller
@RequestMapping("/application")
/*
 * CLASS-LEVEL @SessionAttributes
 * --------------------------------
 * Business problem:
 * The job application is a multi-step form.
 * The applicant enters different parts of the same draft across
 * several HTTP requests:
 *
 *   Personal Details -> Education -> Experience -> Review
 *
 * We do NOT want to save an incomplete job application to the
 * database after every page.
 *
 * Therefore, applicationDraft starts as an MVC Model attribute.
 * @SessionAttributes tells Spring MVC to keep that Model attribute
 * temporarily in HttpSession between requests in THIS controller
 * workflow.
 *
 * Think of it as:
 *
 *   Model -> temporary session storage -> Model -> next request
 *
 * The important point is that applicationDraft is still conceptually
 * MVC workflow data. HttpSession is being used by Spring MVC as the
 * temporary backing store between requests.
 */
@SessionAttributes("applicationDraft")
public class JobApplicationController {

    private final ApplicationService applicationService;

    public JobApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping("/start")
    public String start(
            /*
             * METHOD-PARAMETER @SessionAttribute
             * ----------------------------------
             * preferredLanguage must already exist before this workflow.
             * We read it here only to make sure the classroom demo follows
             * the intended order: preference first, workflow second.
             */
            @SessionAttribute(
                    name = "preferredLanguage",
                    required = false
            )
            String preferredLanguage,
            Model model) {

        if (preferredLanguage == null) {
            return "redirect:/preferences";
        }

        /*
         * START OF THE MVC WORKFLOW
         * -------------------------
         * A brand-new draft is created when the applicant starts
         * the job application.
         *
         * Notice that we add it to Model, NOT directly to HttpSession.
         */
        ApplicationDraft applicationDraft = new ApplicationDraft();

        model.addAttribute(
                "applicationDraft",
                applicationDraft
        );

        /*
         * There is deliberately NO code such as:
         *
         * session.setAttribute("applicationDraft", applicationDraft);
         *
         * Because the class has:
         *
         * @SessionAttributes("applicationDraft")
         *
         * Spring MVC automatically keeps this matching Model attribute
         * between requests for the duration of this controller workflow.
         */
        return "redirect:/application/personal";
    }

    @GetMapping("/personal")
    public String showPersonalStep(
            /*
             * METHOD-LEVEL SESSION ACCESS:
             * The language was already in HttpSession before this workflow.
             * @SessionAttribute only reads it for this request.
             */
            @SessionAttribute("preferredLanguage")
            String preferredLanguage,

            /*
             * WORKFLOW MODEL ACCESS:
             * applicationDraft is the Model attribute maintained across
             * requests by the class-level @SessionAttributes declaration.
             */
            @ModelAttribute("applicationDraft")
            ApplicationDraft applicationDraft,

            HttpSession session,
            Model model) {

        addSessionInspection(
                session,
                preferredLanguage,
                model
        );

        return "application-personal";
    }

    @PostMapping("/personal")
    public String savePersonalStep(
            @ModelAttribute("applicationDraft")
            ApplicationDraft applicationDraft) {

        /*
         * Spring MVC binds the submitted personal-detail fields into
         * the SAME applicationDraft object.
         *
         * We do NOT save to the database here.
         * After this redirect, @SessionAttributes keeps the updated draft
         * available for the next HTTP request.
         */
        return "redirect:/application/education";
    }

    @GetMapping("/education")
    public String showEducationStep(
            @SessionAttribute("preferredLanguage")
            String preferredLanguage,
            @ModelAttribute("applicationDraft")
            ApplicationDraft applicationDraft,
            HttpSession session,
            Model model) {

        /*
         * This is a NEW HTTP request.
         * The inline Session State panel lets students verify two facts:
         *
         * 1. preferredLanguage is still existing session context.
         * 2. applicationDraft now contains the personal details entered
         *    on the previous request.
         */
        addSessionInspection(
                session,
                preferredLanguage,
                model
        );

        return "application-education";
    }

    @PostMapping("/education")
    public String saveEducationStep(
            @ModelAttribute("applicationDraft")
            ApplicationDraft applicationDraft) {

        /*
         * Education information is added to the existing draft.
         * The draft is still temporary workflow state and is still
         * NOT persisted to H2.
         */
        return "redirect:/application/experience";
    }

    @GetMapping("/experience")
    public String showExperienceStep(
            @SessionAttribute("preferredLanguage")
            String preferredLanguage,
            @ModelAttribute("applicationDraft")
            ApplicationDraft applicationDraft,
            HttpSession session,
            Model model) {

        addSessionInspection(
                session,
                preferredLanguage,
                model
        );

        return "application-experience";
    }

    @PostMapping("/experience")
    public String saveExperienceStep(
            @ModelAttribute("applicationDraft")
            ApplicationDraft applicationDraft) {

        /*
         * At this point the same draft contains data collected from
         * several separate HTTP requests.
         */
        return "redirect:/application/review";
    }

    @GetMapping("/review")
    public String review(
            /*
             * @SessionAttribute:
             * "Read this already-existing value from HttpSession and
             *  give it to this method parameter."
             *
             * It does NOT create the value and does NOT manage its lifecycle.
             */
            @SessionAttribute("preferredLanguage")
            String preferredLanguage,

            /*
             * This is NOT read with @SessionAttribute.
             * It is the MVC workflow Model attribute that is being kept
             * between requests because this controller declares:
             *
             * @SessionAttributes("applicationDraft")
             */
            @ModelAttribute("applicationDraft")
            ApplicationDraft applicationDraft,

            HttpSession session,
            Model model) {

        addSessionInspection(
                session,
                preferredLanguage,
                model
        );

        return "application-review";
    }

    @PostMapping("/submit")
    public String submit(
            @ModelAttribute("applicationDraft")
            ApplicationDraft applicationDraft,
            SessionStatus sessionStatus) {

        /*
         * DATABASE PERSISTENCE HAPPENS ONLY AT FINAL SUBMIT
         * -------------------------------------------------
         * Until this point, applicationDraft was temporary workflow data.
         * Now we convert it into the durable JPA entity and save it to H2.
         */
        SubmittedApplication submittedApplication =
                applicationService.submit(applicationDraft);

        /*
         * END OF THE @SessionAttributes WORKFLOW
         * --------------------------------------
         * setComplete() removes the controller-managed applicationDraft
         * from session-backed workflow state.
         *
         * It does NOT invalidate HttpSession, so preferredLanguage remains.
         */
        sessionStatus.setComplete();

        /*
         * Redirect to a NEW request before showing the success page.
         * This is intentional for the demo: the inline inspector on the
         * success page then shows the real AFTER state, after Spring has
         * completed cleanup of applicationDraft.
         */
        return "redirect:/application/success?id="
                + submittedApplication.getId();
    }

    @GetMapping("/success")
    public String success(
            @RequestParam Long id,
            @SessionAttribute("preferredLanguage")
            String preferredLanguage,
            HttpSession session,
            Model model) {

        SubmittedApplication submittedApplication =
                applicationService.findById(id);

        model.addAttribute(
                "submittedApplication",
                submittedApplication
        );

        addSessionInspection(
                session,
                preferredLanguage,
                model
        );

        return "application-success";
    }

    @PostMapping("/cancel")
    public String cancel(SessionStatus sessionStatus) {

        /*
         * Cancelling also ends the temporary application workflow.
         * There is no completed application to save to the database,
         * so we only clear the state managed by @SessionAttributes.
         */
        sessionStatus.setComplete();

        return "redirect:/";
    }

    /*
     * CLASSROOM-ONLY SESSION INSPECTOR SUPPORT
     * ----------------------------------------
     * This helper does NOT implement the business workflow.
     * It only exposes the raw HttpSession state to Thymeleaf so that
     * every wizard page can display the two mechanisms side-by-side.
     *
     * We deliberately inspect both values:
     *
     * preferredLanguage
     *   -> normal existing HttpSession data
     *   -> read in controller methods with @SessionAttribute
     *
     * applicationDraft
     *   -> MVC Model workflow data
     *   -> temporarily stored in HttpSession by @SessionAttributes
     *
     * This makes the lifecycle visible to students on every request.
     */
    private void addSessionInspection(
            HttpSession session,
            String preferredLanguage,
            Model model) {

        model.addAttribute(
                "inspectedSessionId",
                session.getId()
        );

        model.addAttribute(
                "inspectedPreferredLanguage",
                preferredLanguage
        );

        model.addAttribute(
                "inspectedApplicationDraft",
                session.getAttribute("applicationDraft")
        );
    }
}
