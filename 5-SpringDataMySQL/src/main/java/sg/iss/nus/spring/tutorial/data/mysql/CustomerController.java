package sg.iss.nus.spring.tutorial.data.mysql;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
public class CustomerController {
  private static final Logger log = LoggerFactory.getLogger(CustomerController.class);
  private final CustomerRepository repo;

  public CustomerController(CustomerRepository repo) { this.repo = repo; }

  // --- Creation: hard-code a record and save it -----------------------------
  // GET is okay for the exercise; in real apps, POST with a body is preferred.
  @GetMapping("/create-sample")
  public Customer createSample() {
    Customer c = new Customer("Alice Tan", "91234567", "123 Orchard Road");
    Customer saved = repo.save(c);
    log.info("Created: {}", saved);
    return saved;
  }

  // --- SearchByName: query and print results to console (logs) --------------
  // Example: /customers/search?name=alice
  @GetMapping("/search")
  public List<Customer> searchByName(@RequestParam("name") String name) {
    List<Customer> results = repo.findByNameContainingIgnoreCase(name);
    results.forEach(r -> log.info("Search hit: {}", r));  // printed on console
    return results;
  }
}