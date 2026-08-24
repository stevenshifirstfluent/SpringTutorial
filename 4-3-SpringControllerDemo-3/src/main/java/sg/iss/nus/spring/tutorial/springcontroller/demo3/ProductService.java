package sg.iss.nus.spring.tutorial.springcontroller.demo3;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class ProductService {
  private final List<Product> db = List.of(
      new Product(1L, "laptop", 1699),
      new Product(2L, "laptop stand", 39),
      new Product(3L, "usb-c hub", 59),
      new Product(4L, "mouse", 29),
      new Product(5L, "laptop pro", 2499),
      new Product(6L, "keyboard", 89)
  );

  public List<Product> search(String name, int limit) {
    String q = Optional.ofNullable(name).orElse("").toLowerCase();
    return db.stream()
        .filter(p -> p.getName().toLowerCase().contains(q))
        .limit(limit > 0 ? limit : db.size())
        .collect(Collectors.toList());
  }
}
