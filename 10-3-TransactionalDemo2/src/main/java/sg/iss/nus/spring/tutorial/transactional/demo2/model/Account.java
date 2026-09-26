package sg.iss.nus.spring.tutorial.transactional.demo2.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity 
@Table(name="account")
public class Account {
  @Id 
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  public Long getId() {
	return id;
}
  public void setId(Long id) {
	this.id = id;
  }
  public String getOwner() {
	return owner;
  }
  public void setOwner(String owner) {
	this.owner = owner;
  }
  public double getBalance() {
	return balance;
  }
  public void setBalance(double balance) {
	this.balance = balance;
  }
  private String owner;
  private double balance;
  public void withdraw(double amt){ balance -= amt; }
  public void deposit(double amt){ balance += amt; }
}

