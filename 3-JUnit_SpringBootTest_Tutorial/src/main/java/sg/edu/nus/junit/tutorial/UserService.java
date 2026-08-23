package sg.edu.nus.junit.tutorial;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final AuditService auditService;

    public UserService(
            UserRepository userRepository,
            EmailService emailService,
            AuditService auditService) {

        this.userRepository = userRepository;
        this.emailService = emailService;
        this.auditService = auditService;
    }

    @Transactional
    public User create(User user) {

        User savedUser =
                userRepository.save(user);

        emailService.sendWelcomeEmail(savedUser);

        auditService.recordUserCreated(savedUser);

        return savedUser;
    }
}