package sg.edu.nus.thmeleaf.tutorial;

public class CustomerForm {

    private Long id;

    private String firstName;

    private String lastName;

    private String gender;

    private String paymentMethod;

    private Double balance;


    public CustomerForm() {
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getFirstName() {
        return firstName;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public String getLastName() {
        return lastName;
    }


    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getGender() {
        return gender;
    }


    public void setGender(String gender) {
        this.gender = gender;
    }


    public String getPaymentMethod() {
        return paymentMethod;
    }


    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }


    public Double getBalance() {
        return balance;
    }


    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
