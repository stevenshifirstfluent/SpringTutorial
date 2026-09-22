package sg.edu.nus.sessiondemo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import sg.edu.nus.sessiondemo.model.ApplicationDraft;
import sg.edu.nus.sessiondemo.model.SubmittedApplication;
import sg.edu.nus.sessiondemo.repository.SubmittedApplicationRepository;

@Service
public class ApplicationService {

    private final SubmittedApplicationRepository repository;

    public ApplicationService(SubmittedApplicationRepository repository) {
        this.repository = repository;
    }

    public SubmittedApplication submit(ApplicationDraft draft) {

        /*
         * The MVC workflow object is intentionally separate from
         * the JPA entity.
         *
         * This conversion happens only after the user clicks Submit.
         * Before this method runs, the draft has not been persisted.
         */
        SubmittedApplication application = new SubmittedApplication();

        application.setFullName(draft.getFullName());
        application.setEmail(draft.getEmail());
        application.setHighestEducation(draft.getHighestEducation());
        application.setCurrentRole(draft.getCurrentRole());
        application.setYearsOfExperience(draft.getYearsOfExperience());
        application.setSubmittedAt(LocalDateTime.now());

        /*
         * This is the point where the completed application becomes
         * durable database data.
         */
        return repository.save(application);
    }

    public List<SubmittedApplication> findAll() {
        return repository.findAll();
    }

    public SubmittedApplication findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Submitted application not found: " + id
                        )
                );
    }
}
