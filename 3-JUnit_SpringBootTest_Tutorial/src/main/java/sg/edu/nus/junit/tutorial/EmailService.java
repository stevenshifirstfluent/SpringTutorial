package sg.edu.nus.junit.tutorial;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public void sendWelcomeEmail(User user) {

        System.out.println(
                "Sending welcome email to " + user.getName()
        );
    }
}