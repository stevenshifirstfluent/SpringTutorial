package sg.iss.nus.spring.tutorial.transactional.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import sg.iss.nus.spring.tutorial.transactional.demo.model.Course;
import sg.iss.nus.spring.tutorial.transactional.demo.service.CourseService;

@Controller
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/list")
    public String listCourses(Model model) {
        model.addAttribute("courses", courseService.findAllCourses());
        return "courses";
    }

    @PostMapping("/create")
    public String createCourse(@RequestParam("name") String name) {
        Course course = new Course();
        course.setName(name);
        courseService.createCourse(course);
        return "redirect:/courses/list";
    }
}
