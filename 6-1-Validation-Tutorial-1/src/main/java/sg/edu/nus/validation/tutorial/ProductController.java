package sg.edu.nus.validation.tutorial;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class ProductController {

    private final ProductRepository productRepository;


    public ProductController(
            ProductRepository productRepository) {

        this.productRepository =
                productRepository;
    }


    /*
     * Display all products.
     */
    @GetMapping("/")
    public String showProducts(
            Model model) {

        model.addAttribute(
                "products",
                productRepository.findAll()
        );

        return "index";
    }


    /*
     * Display the form for creating
     * a new product.
     */
    @GetMapping("/products/new")
    public String showCreateProductForm(
            Model model) {

        model.addAttribute(
                "product",
                new Product()
        );

        /*
         * Used by edit.html to determine
         * whether the form is in create
         * or edit mode.
         */
        model.addAttribute(
                "formMode",
                "create"
        );

        return "edit";
    }


    /*
     * Create a new product.
     */
    @RequestMapping(
            path = "/products",
            method = RequestMethod.POST
    )
    public String saveProduct(

            @Valid
            Product product,

            BindingResult bindingResult,

            Model model,

            RedirectAttributes redirectAttributes) {


        /*
         * Step 1:
         * Bean Validation.
         *
         * Validation annotations declared on
         * Product are evaluated because of
         * @Valid.
         */
        if (bindingResult.hasErrors()) {

            model.addAttribute(
                    "formMode",
                    "create"
            );

            return "edit";
        }


        /*
         * Step 2:
         * Business rule validation.
         *
         * Product name must be unique.
         */
        if (productRepository
                .existsByNameIgnoreCase(
                        product.getName()
                )) {

            /*
             * Add the business-rule error
             * to the same BindingResult.
             *
             * Thymeleaf th:errors="*{name}"
             * can display this error.
             */
            bindingResult.rejectValue(
                    "name",
                    "duplicate.name",
                    "Product name already exists"
            );

            model.addAttribute(
                    "formMode",
                    "create"
            );

            return "edit";
        }


        /*
         * Step 3:
         * Save only after both validation
         * stages have passed.
         */
        productRepository.save(
                product
        );


        /*
         * Flash attribute survives the redirect
         * and is displayed on index.html.
         */
        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Product \"" +
                        product.getName() +
                        "\" created successfully."
        );


        return "redirect:/";
    }


    /*
     * Display the form for editing
     * an existing product.
     */
    @GetMapping("/products/{id}/edit")
    public String showEditProductForm(

            @PathVariable
            String id,

            Model model) {


        Product product =
                productRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "Product not found: " + id
                                        )
                        );


        model.addAttribute(
                "product",
                product
        );


        model.addAttribute(
                "formMode",
                "edit"
        );


        return "edit";
    }


    /*
     * Update an existing product.
     */
    @RequestMapping(
            path = "/products/{id}",
            method = RequestMethod.POST
    )
    public String updateProduct(

            @PathVariable
            String id,

            @Valid
            Product product,

            BindingResult bindingResult,

            Model model,

            RedirectAttributes redirectAttributes) {


        /*
         * Make sure the ID remains attached
         * to the submitted object.
         */
        product.setId(id);


        /*
         * Step 1:
         * Bean Validation.
         */
        if (bindingResult.hasErrors()) {

            model.addAttribute(
                    "formMode",
                    "edit"
            );

            return "edit";
        }


        /*
         * Make sure the product still exists.
         */
        productRepository
                .findById(id)
                .orElseThrow(
                        () ->
                                new IllegalArgumentException(
                                        "Product not found: " + id
                                )
                );


        /*
         * Step 2:
         * Business rule validation.
         *
         * During update, the current product
         * must not be treated as a duplicate
         * of itself.
         */
        if (productRepository
                .existsByNameIgnoreCaseAndIdNot(
                        product.getName(),
                        id
                )) {

            bindingResult.rejectValue(
                    "name",
                    "duplicate.name",
                    "Product name already exists"
            );

            model.addAttribute(
                    "formMode",
                    "edit"
            );

            return "edit";
        }


        /*
         * Step 3:
         * Save the updated entity.
         */
        productRepository.save(
                product
        );


        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Product \"" +
                        product.getName() +
                        "\" updated successfully."
        );


        return "redirect:/";
    }
}