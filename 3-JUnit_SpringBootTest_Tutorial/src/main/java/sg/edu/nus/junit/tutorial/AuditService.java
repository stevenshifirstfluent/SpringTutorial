package sg.edu.nus.junit.tutorial;

import org.springframework.stereotype.Service;

@Service
public class AuditService {

    public void recordUserCreated(User user) {

        System.out.println(
                "Audit: User created - " + user.getName()
        );
    }
}