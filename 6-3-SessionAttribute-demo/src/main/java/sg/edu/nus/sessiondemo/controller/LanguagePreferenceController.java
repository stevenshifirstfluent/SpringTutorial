package sg.edu.nus.sessiondemo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/preferences")
public class LanguagePreferenceController {

    @GetMapping
    public String showPreferencePage(
            HttpSession session,
            Model model) {

        /*
         * This controller works directly with HttpSession.
         * It is intentionally separate from JobApplicationController.
         *
         * That separation demonstrates that preferredLanguage can
         * exist BEFORE the job-application workflow begins.
         */
        Object preferredLanguage =
                session.getAttribute("preferredLanguage");

        model.addAttribute(
                "currentLanguage",
                preferredLanguage
        );

        /*
         * Classroom inspector state.
         * At this stage applicationDraft should normally be absent because
         * the multi-step job-application workflow has not started yet.
         */
        model.addAttribute("inspectedSessionId", session.getId());
        model.addAttribute("inspectedPreferredLanguage", preferredLanguage);
        model.addAttribute(
                "inspectedApplicationDraft",
                session.getAttribute("applicationDraft")
        );

        return "preferences";
    }

    @PostMapping
    public String savePreference(
            @RequestParam String preferredLanguage,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        /*
         * CREATE NORMAL HTTP SESSION DATA
         * -------------------------------
         * The language preference is explicitly written into HttpSession.
         *
         * This line is what CREATES / UPDATES the session attribute:
         */
        session.setAttribute(
                "preferredLanguage",
                preferredLanguage
        );

        /*
         * Later, JobApplicationController can READ the same value using:
         *
         * @SessionAttribute("preferredLanguage")
         *
         * This is the key distinction:
         *
         * HttpSession.setAttribute(...)
         *     -> stores the value
         *
         * @SessionAttribute(...)
         *     -> only reads an existing value into a method parameter
         */
        redirectAttributes.addFlashAttribute(
                "message",
                "Language preference stored in HttpSession."
        );

        return "redirect:/preferences";
    }
}
