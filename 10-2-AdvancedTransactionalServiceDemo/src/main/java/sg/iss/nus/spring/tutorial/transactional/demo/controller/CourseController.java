package sg.iss.nus.spring.tutorial.transactional.demo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import sg.iss.nus.spring.tutorial.transactional.demo.model.Course;
import sg.iss.nus.spring.tutorial.transactional.demo.service.CourseService;
import sg.iss.nus.spring.tutorial.transactional.demo.service.FileStorageService;

@Controller
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private FileStorageService fileStorageService;  // Inject the file storage service

    @Autowired
    private CourseService courseService;
    
    @GetMapping("/list")
    public String listCourses(Model model) {
    	model.addAttribute("courses", courseService.findAllCourses());
        return "courses";
    }

    @PostMapping("/create")
    public String createCourse(@RequestParam("name") String name,
    							   @RequestParam("image") MultipartFile imageFile, 
    							   RedirectAttributes ra) {
        Path uploadDir = fileStorageService.getUploadDir();  // Get the upload directory
        Course course = new Course();
        course.setName(name);
        
        if (!imageFile.isEmpty()) {
            try {
                byte[] bytes = imageFile.getBytes();
                Path path = Paths.get(uploadDir.toString(), imageFile.getOriginalFilename());
                Files.write(path, bytes);
                
             // Set the image path in the course
                course.setImagePath("/" + fileStorageService.UPLOAD_DIR + "/"+ imageFile.getOriginalFilename());
                courseService.createCourse(course);
                // Your logic to save course with the image path
                ra.addFlashAttribute("success", "Course created successfully!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        

        return "redirect:/courses/list";
    }
}