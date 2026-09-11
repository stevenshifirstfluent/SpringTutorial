package sg.iss.nus.spring.tutorial.data.mysql;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "customer")
public class Customer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(length = 35, nullable = false)
  private String name;

  // store as String (leading zeros & formatting safe), length 8 per spec
  @Column(name = "hand_phone_number", length = 8)
  private String handPhoneNumber;

  @Column(length = 35)
  private String address;

  public Customer() {}

  public Customer(String name, String handPhoneNumber, String address) {
    this.name = name;
    this.handPhoneNumber = handPhoneNumber;
    this.address = address;
  }

  // getters/setters
  public Integer getId() { return id; }
  public void setId(Integer id) { this.id = id; }
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
  public String getHandPhoneNumber() { return handPhoneNumber; }
  public void setHandPhoneNumber(String handPhoneNumber) { this.handPhoneNumber = handPhoneNumber; }
  public String getAddress() { return address; }
  public void setAddress(String address) { this.address = address; }

  @Override public String toString() {
    return "Customer{id=%d, name='%s', phone='%s', address='%s'}"
        .formatted(id, name, handPhoneNumber, address);
  }
}
