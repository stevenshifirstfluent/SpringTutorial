package sg.iss.nus.spring.tutorial.transactional.demo2.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sg.iss.nus.spring.tutorial.transactional.demo2.model.Account;
import sg.iss.nus.spring.tutorial.transactional.demo2.repository.AccountRepository;

@Service
public class PaymentService {
  private final AccountRepository accounts;
  public PaymentService(AccountRepository accounts){ this.accounts = accounts; }

  @Transactional
  public void transfer(long fromId, long toId, double amount, boolean isFailed){
    Account from = accounts.findById(fromId).orElseThrow();
    Account to   = accounts.findById(toId).orElseThrow();
    from.withdraw(amount); 
    accounts.save(from);
    if (isFailed)
    	{
    		throw new RuntimeException("Simulated failure");
    	}
    to.deposit(amount);    
    accounts.save(to);

    
  }

}
