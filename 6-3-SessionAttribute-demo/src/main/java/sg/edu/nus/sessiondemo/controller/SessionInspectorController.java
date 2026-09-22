package sg.edu.nus.sessiondemo.controller;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/session")
public class SessionInspectorController {

    @GetMapping
    public String inspect(
            HttpSession session,
            Model model) {

        /*
         * CLASSROOM-ONLY INSPECTOR
         * ------------------------
         * This controller is not required for normal application logic.
         * It exists so students can see the actual HttpSession state.
         *
         * Important distinction:
         *
         * preferredLanguage
         *   -> explicitly stored by LanguagePreferenceController
         *   -> later READ using method-parameter @SessionAttribute
         *
         * applicationDraft
         *   -> started as an MVC Model attribute
         *   -> temporarily stored between requests because
         *      JobApplicationController declares @SessionAttributes
         */
        Object preferredLanguage =
                session.getAttribute("preferredLanguage");

        Object applicationDraft =
                session.getAttribute("applicationDraft");

        model.addAttribute("inspectedSessionId", session.getId());
        model.addAttribute("inspectedPreferredLanguage", preferredLanguage);
        model.addAttribute("inspectedApplicationDraft", applicationDraft);

        /*
         * If the servlet container or another part of the application adds
         * extra attributes, list them separately so they do not distract
         * from the two teaching values above.
         */
        List<SessionItem> otherItems = new ArrayList<>();
        Enumeration<String> names = session.getAttributeNames();

        while (names.hasMoreElements()) {
            String name = names.nextElement();

            if ("preferredLanguage".equals(name)
                    || "applicationDraft".equals(name)) {
                continue;
            }

            Object value = session.getAttribute(name);
            otherItems.add(
                    new SessionItem(
                            name,
                            describeValue(value)
                    )
            );
        }

        model.addAttribute("otherItems", otherItems);

        return "session-inspector";
    }

    private String describeValue(Object value) {
        if (value == null) {
            return "null";
        }

        if (value instanceof String text) {
            return text;
        }

        return value.getClass().getSimpleName();
    }

    public record SessionItem(
            String name,
            String value) {
    }
}
