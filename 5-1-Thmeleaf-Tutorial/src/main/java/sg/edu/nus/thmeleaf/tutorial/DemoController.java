package sg.edu.nus.thmeleaf.tutorial;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class DemoController {


    @GetMapping("/thymeleaf-demo")
    public String showDemo(

            /*
             * paymentType controls the dynamic
             * radio-button options.
             *
             * Examples:
             *
             * /thymeleaf-demo
             *
             * /thymeleaf-demo?paymentType=local
             *
             * /thymeleaf-demo?paymentType=international
             */
            @RequestParam(
                    defaultValue = "local"
            ) String paymentType,

            Model model,

            HttpSession session) {


        /*
         * ==================================================
         * USER
         * ==================================================
         */

        Address address =
                new Address(
                        "Singapore"
                );


        User user =
                new User(
                        3L,
                        "Sebastian",
                        "Pepper",
                        "Singaporean",
                        27,
                        address
                );


        /*
         * Used by the th:switch demonstration.
         */
        user.setRole(
                "admin"
        );


        /*
         * Store the User inside the HTTP session.
         *
         * This allows expressions such as:
         *
         * ${session.user}
         * ${session.user.name}
         * ${session.user.role}
         */
        session.setAttribute(
                "user",
                user
        );


        /*
         * ==================================================
         * PRODUCTS
         * ==================================================
         */

        List<Product> products =
                new ArrayList<>();


        Product product1 =
                new Product(
                        1L,
                        "Onions",
                        "Fresh red onions",
                        "Vegetable",
                        "Fresh Food",
                        new BigDecimal("2.41"),
                        true
                );


        /*
         * Product 1 contains comments.
         *
         * This allows us to demonstrate:
         *
         * th:if
         * th:unless
         * #lists.isEmpty(...)
         */
        product1.getComments().add(
                "Fresh and good quality."
        );

        product1.getComments().add(
                "Recommended."
        );


        Product product2 =
                new Product(
                        2L,
                        "Apple",
                        "Fresh red apple",
                        "Fruit",
                        "Fresh Food",
                        new BigDecimal("3.50"),
                        true
                );


        /*
         * Product 2 intentionally contains
         * no comments.
         */


        Product product3 =
                new Product(
                        3L,
                        "Milk",
                        "Fresh whole milk",
                        "Dairy",
                        "Beverage",
                        new BigDecimal("4.20"),
                        false
                );


        product3.getComments().add(
                "Good taste."
        );


        products.add(
                product1
        );

        products.add(
                product2
        );

        products.add(
                product3
        );


        /*
         * ==================================================
         * BASIC THYMELEAF DATA
         * ==================================================
         */

        model.addAttribute(
                "today",
                LocalDate.now()
        );


        model.addAttribute(
                "year",
                2026
        );


        model.addAttribute(
                "prodStatCount",
                5
        );


        model.addAttribute(
                "execMode",
                "dev"
        );


        model.addAttribute(
                "lastAccessDate",
                LocalDateTime.now()
        );


        /*
         * Used by #temporals.
         */
        model.addAttribute(
                "now",
                LocalDateTime.now()
        );


        /*
         * Used to demonstrate:
         *
         * th:text
         * vs
         * th:utext
         */
        model.addAttribute(
                "htmlMessage",
                "<strong>Welcome to Spring Boot!</strong>"
        );


        model.addAttribute(
                "products",
                products
        );


        /*
         * ==================================================
         * MESSAGE EXPRESSION PARAMETERS
         * ==================================================
         *
         * Used by:
         *
         * #{languagepage.message(
         *      ${data1},
         *      ${data2}
         * )}
         */

        model.addAttribute(
                "data1",
                "Sebastian"
        );


        model.addAttribute(
                "data2",
                "REF-1001"
        );


        /*
         * ==================================================
         * CUSTOMER FORM
         * ==================================================
         */

        CustomerForm customerForm =
                new CustomerForm();


        customerForm.setId(
                1001L
        );


        customerForm.setFirstName(
                "Sebastian"
        );


        customerForm.setLastName(
                "Pepper"
        );


        customerForm.setGender(
                "Male"
        );


        customerForm.setPaymentMethod(
                "Credit Card"
        );


        customerForm.setBalance(
                1000.00
        );


        model.addAttribute(
                "customerForm",
                customerForm
        );


        /*
         * Radio-button options for CustomerForm.
         */
        model.addAttribute(
                "genders",
                List.of(
                        "Male",
                        "Female"
                )
        );


        /*
         * Select options for CustomerForm.
         */
        model.addAttribute(
                "paymentMethods",
                List.of(
                        "Credit Card",
                        "PayNow",
                        "Bitcoin"
                )
        );


        /*
         * ==================================================
         * INPUT FORM
         * ==================================================
         */

        InputForm inputForm =
                new InputForm();


        inputForm.setTextValue(
                "Spring Boot"
        );


        inputForm.setNumberValue(
                100
        );


        inputForm.setPasswordValue(
                "secret"
        );


        inputForm.setHiddenValue(
                "HIDDEN-1001"
        );


        /*
         * Default selected value for the
         * static radio-button example.
         */
        inputForm.setRadioValue(
                "Radio Value 1"
        );


        /*
         * ==================================================
         * DYNAMIC RADIO-BUTTON OPTIONS
         * ==================================================
         *
         * The HTML remains unchanged.
         *
         * The Controller determines which options
         * should be displayed.
         */

        if ("international".equals(
                paymentType
        )) {

            /*
             * International payment options.
             */
            model.addAttribute(
                    "dynamicRadioOptions",
                    List.of(
                            "Credit Card",
                            "PayPal",
                            "International Transfer"
                    )
            );


            /*
             * Default selected value.
             */
            inputForm.setDynamicRadioValue(
                    "PayPal"
            );

        } else {

            /*
             * Local payment options.
             */
            model.addAttribute(
                    "dynamicRadioOptions",
                    List.of(
                            "PayNow",
                            "Credit Card",
                            "Bank Transfer"
                    )
            );


            /*
             * Default selected value.
             */
            inputForm.setDynamicRadioValue(
                    "PayNow"
            );
        }


        /*
         * Display the currently selected
         * payment option group.
         */
        model.addAttribute(
                "paymentType",
                paymentType
        );


        /*
         * Add the form-backing object AFTER
         * configuring its default values.
         */
        model.addAttribute(
                "inputForm",
                inputForm
        );


        return "demo";
    }


    /*
     * ======================================================
     * URL EXPRESSION DEMO
     * ======================================================
     */


    /*
     * Handles:
     *
     * /order/details?orderId=3
     */
    @GetMapping("/order/details")
    @ResponseBody
    public String orderDetails(
            @RequestParam Long orderId) {

        return "Order ID = "
                + orderId;
    }


    /*
     * Handles:
     *
     * /order/3/details
     */
    @GetMapping("/order/{orderId}/details")
    @ResponseBody
    public String orderDetailsByPath(
            @PathVariable Long orderId) {

        return "Order ID = "
                + orderId;
    }


    /*
     * ======================================================
     * SUBSCRIBE FORM
     * ======================================================
     */

    @PostMapping("/subscribe")
    @ResponseBody
    public String subscribe(
            @RequestParam String email) {

        return "Subscribed: "
                + email;
    }


    /*
     * ======================================================
     * PRODUCT COMMENT DEMO
     * ======================================================
     */

    @GetMapping("/product/comments")
    @ResponseBody
    public String productComments(
            @RequestParam Long prodId) {

        return "Comments for Product ID = "
                + prodId;
    }


    /*
     * ======================================================
     * CUSTOMER FORM BINDING
     * ======================================================
     */

    @PostMapping("/customer/save")
    @ResponseBody
    public String saveCustomer(

            @ModelAttribute
            CustomerForm customerForm) {

        return "Saved Customer: "
                + customerForm.getFirstName()
                + " "
                + customerForm.getLastName()
                + ", Gender = "
                + customerForm.getGender()
                + ", Payment = "
                + customerForm.getPaymentMethod()
                + ", Balance = "
                + customerForm.getBalance();
    }


    /*
     * ======================================================
     * INPUT FORM BINDING
     * ======================================================
     */

    @PostMapping("/input/save")
    @ResponseBody
    public String saveInputForm(

            @ModelAttribute
            InputForm inputForm) {

        return "Text = "
                + inputForm.getTextValue()

                + ", Number = "
                + inputForm.getNumberValue()

                + ", Hidden = "
                + inputForm.getHiddenValue()

                + ", Radio = "
                + inputForm.getRadioValue()

                + ", Dynamic Radio = "
                + inputForm.getDynamicRadioValue();
    }
}