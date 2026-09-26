package sg.iss.nus.spring.tutorial.transactional.demo2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import sg.iss.nus.spring.tutorial.transactional.demo2.repository.AccountRepository;
import sg.iss.nus.spring.tutorial.transactional.demo2.service.PaymentService;
import sg.iss.nus.spring.tutorial.transactional.demo2.service.PaymentServiceNoTx;

@Controller
@RequestMapping("/")
public class DemoMvcController {

  @Autowired
  private PaymentService tx;
  @Autowired
  private PaymentServiceNoTx noTx;
  @Autowired
  private AccountRepository accounts;

  public DemoMvcController() {}

  @GetMapping
  public String home(Model model,
                     @RequestParam(value = "msg", required = false) String msg,
                     @RequestParam(value = "err", required = false) String err) {
    var a = accounts.findById(1L).orElseThrow();
    var b = accounts.findById(2L).orElseThrow();
    model.addAttribute("alice", a);
    model.addAttribute("bob", b);
    model.addAttribute("msg", msg);
    model.addAttribute("err", err);
    return "home";
  }

  @PostMapping("/tx")
  public String doTx(@RequestParam(defaultValue = "false") boolean fail,
                     RedirectAttributes ra) {
    try {
      tx.transfer(1L, 2L, 100, fail);
      ra.addAttribute("msg", fail ? "TX failed → rolled back (balances unchanged)." :
                                    "TX success → atomic commit.")
        ;
    } catch (Exception e) {
      ra.addAttribute("err", "TX threw: " + e.getMessage() + " → rollback happened.");
    }
    return "redirect:/";
  }

  @PostMapping("/no-tx")
  public String doNoTx(@RequestParam(defaultValue = "false") boolean fail,
                       RedirectAttributes ra) {
    try {
      noTx.transferWithoutTx(1L, 2L, 100, fail);
      ra.addAttribute("msg", fail ? "No TX failed (but partial commits occurred!)." :
                                    "No TX success (all saved).");
    } catch (Exception e) {
      ra.addAttribute("err", "No TX threw: " + e.getMessage() + " → partial commits possible!");
    }
    return "redirect:/";
  }
}
