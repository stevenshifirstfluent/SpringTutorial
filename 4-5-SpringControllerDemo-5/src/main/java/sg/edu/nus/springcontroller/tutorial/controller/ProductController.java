package sg.edu.nus.springcontroller.tutorial.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import sg.edu.nus.springcontroller.tutorial.model.Product;
import sg.edu.nus.springcontroller.tutorial.repository.ProductRepository;

/*
 * Handles web requests related to Product.
 *
 * @Controller tells Spring that this class
 * contains MVC request-handling methods.
 *
 * @RequestMapping("/product") defines the
 * common URL prefix for all methods.
 */
@Controller
@RequestMapping("/product")
public class ProductController {

    /*
     * Spring injects the ProductRepository.
     *
     * The repository is used to access
     * Product data in the database.
     */
    @Autowired
    ProductRepository pRepo;


    /*
     * Displays all products.
     *
     * Request:
     * GET /product/list
     */
    @GetMapping("/list")
    public String listAll(Model model) {

        /*
         * Retrieve all Product records
         * from the database.
         *
         * The result is added to the Model
         * using the name "products".
         */
        model.addAttribute(
                "products",
                pRepo.findAll()
        );

        /*
         * Return the Thymeleaf view name.
         *
         * Spring will render:
         * templates/products.html
         */
        return "products";
    }


    /*
     * Displays the form used to create
     * a new Product.
     *
     * Request:
     * GET /product/add
     */
    @GetMapping("/add")
    public String showAddForm(Model model) {

        /*
         * Create an empty Product object.
         *
         * The HTML form will bind its
         * input fields to this object.
         */
        Product product = new Product();

        /*
         * Add the Product object to the Model.
         *
         * Thymeleaf will access it using:
         *
         * ${product}
         */
        model.addAttribute(
                "product",
                product
        );

        /*
         * Render:
         * templates/productform.html
         */
        return "productform";
    }


    /*
     * Saves a Product submitted from
     * the HTML form.
     *
     * Request:
     * POST /product/save
     */
    @PostMapping("/save")
    public String saveProduct(
            @ModelAttribute("product") Product product,
            Model model) {

        /*
         * @ModelAttribute binds submitted
         * form values to the Product object.
         *
         * pRepo.save() performs either:
         *
         * INSERT - when it is a new Product
         *
         * or
         *
         * UPDATE - when the Product already
         * has an existing ID.
         */
        pRepo.save(product);

        /*
         * Redirect the request internally
         * to /product/list.
         */
        return "redirect:/product/list";
    }


    /*
     * Displays the form for editing
     * an existing Product.
     *
     * Example request:
     * GET /product/edit/1
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(
            Model model,
            @PathVariable("id") Integer id) {

        /*
         * Read the Product ID from the URL.
         *
         * Example:
         *
         * /product/edit/1
         *
         * id = 1
         */
        Product product =
                pRepo.findById(id).get();

        /*
         * Add the existing Product to
         * the Model.
         *
         * productform.html can use the
         * same form for both Add and Edit.
         */
        model.addAttribute(
                "product",
                product
        );

        return "productform";
    }


    /*
     * Deletes an existing Product.
     *
     * Example request:
     * GET /product/delete/1
     */
    @GetMapping("/delete/{id}")
    public String deleteMethod(
            Model model,
            @PathVariable("id") Integer id) {

        /*
         * Find the Product using its ID.
         */
        Product product =
                pRepo.findById(id).get();

        /*
         * Delete the Product from
         * the database.
         */
        pRepo.delete(product);

        /*
         * Forward back to the product list.
         */
        return "forward:/product/list";
    }
}
