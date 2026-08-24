package sg.iss.nus.spring.tutorial.springcontroller.demo3;

public class Product {
	  private Long id;
	  private String name;
	  private double price;

	  public Product(Long id, String name, double price) {
	    this.id = id; this.name = name; this.price = price;
	  }
	  public Long getId() { return id; }
	  public String getName() { return name; }
	  public double getPrice() { return price; }
	}
