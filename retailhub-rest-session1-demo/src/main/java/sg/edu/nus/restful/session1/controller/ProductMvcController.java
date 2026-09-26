package sg.edu.nus.restful.session1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import sg.edu.nus.restful.session1.service.ProductService;

/**
 * Comparison endpoint for the Session 1 slide:
 * @Controller normally resolves a view instead of serialising the return value as JSON.
 */
@Controller
public class ProductMvcController {

    private final ProductService productService;

    public ProductMvcController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/mvc/products")
    public String products(Model model) {
        model.addAttribute("products", productService.findAllActive(null, null));
        return "mvc-products";
    }
}
