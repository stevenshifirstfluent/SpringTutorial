package sg.edu.nus.sessiondemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.edu.nus.sessiondemo.model.SubmittedApplication;

public interface SubmittedApplicationRepository
        extends JpaRepository<SubmittedApplication, Long> {
}
