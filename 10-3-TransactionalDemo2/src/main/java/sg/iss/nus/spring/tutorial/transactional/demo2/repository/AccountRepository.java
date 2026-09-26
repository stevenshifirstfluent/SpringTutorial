package sg.iss.nus.spring.tutorial.transactional.demo2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.iss.nus.spring.tutorial.transactional.demo2.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {}

