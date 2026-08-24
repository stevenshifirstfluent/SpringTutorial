package sg.iss.nus.spring.tutorial.springcontroller.demo3;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/products")
public class ProductController {

  private final ProductService productService;
  public ProductController(ProductService productService) { this.productService = productService; }

  // GET form page using RequestParam
  // /search?name=laptop&limit=10
  @GetMapping("/search")
  public String searchPage(
      @RequestParam(required = false, defaultValue = "") String name,
      @RequestParam(required = false, defaultValue = "10") int limit,
      Model model) {

    List<Product> results = productService.search(name, limit);
    model.addAttribute("name", name);
    model.addAttribute("limit", limit);
    model.addAttribute("products", results);
    return "productResults"; // Thymeleaf view
  }
  
  //GET form page using PathVariable
  // /search/laptop/10
  @GetMapping("/searchVariable/{name}/{limit}")
  public String searchPageWithPathVariable(@PathVariable String name, @PathVariable int limit, Model model) {

    List<Product> results = productService.search(name, limit);
    model.addAttribute("name", name);
    model.addAttribute("limit", limit);
    model.addAttribute("products", results);
    return "productResults"; // Thymeleaf view
  }

  // POST form submission (same URL but POST)
  @PostMapping("/search")
  public String searchPost(@RequestParam String name,
                           @RequestParam int limit,
                           Model model) {
    List<Product> results = productService.search(name, limit);
    model.addAttribute("name", name);
    model.addAttribute("limit", limit);
    model.addAttribute("products", results);
    return "productResults";
  }

  // (Optional) PathVariable version: /products/search/laptop/10
  @GetMapping("/search/{name}/{limit}")
  public String searchPath(@PathVariable String name,
                           @PathVariable int limit,
                           Model model) {
    List<Product> results = productService.search(name, limit);
    model.addAttribute("name", name);
    model.addAttribute("limit", limit);
    model.addAttribute("products", results);
    return "productResults";
  }
}