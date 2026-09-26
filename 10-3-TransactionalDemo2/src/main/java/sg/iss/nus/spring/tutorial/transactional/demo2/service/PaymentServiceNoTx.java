package sg.iss.nus.spring.tutorial.transactional.demo2.service;

import org.springframework.stereotype.Service;

import sg.iss.nus.spring.tutorial.transactional.demo2.model.Account;
import sg.iss.nus.spring.tutorial.transactional.demo2.repository.AccountRepository;

@Service
public class PaymentServiceNoTx {
  private final AccountRepository accounts;
  public PaymentServiceNoTx(AccountRepository accounts){ this.accounts = accounts; }

  public void transferWithoutTx(long fromId, long toId, double amount, boolean isFailed){
    Account from = accounts.findById(fromId).orElseThrow();
    Account to   = accounts.findById(toId).orElseThrow();
    from.withdraw(amount); 
    accounts.save(from); // auto-committed
    if(isFailed) {
    		throw new RuntimeException("Simulated failure");
    }
    to.deposit(amount);    
    accounts.save(to);   // auto-committed
  }
}

