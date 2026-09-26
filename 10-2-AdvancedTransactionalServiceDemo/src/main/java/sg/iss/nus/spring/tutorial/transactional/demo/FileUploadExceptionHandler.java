package sg.iss.nus.spring.tutorial.transactional.demo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class FileUploadExceptionHandler {

	// Inject value from application.properties
    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;

	
 // Triggered when uploaded part exceeds spring.servlet.multipart.max-file-size
    @ExceptionHandler(org.springframework.web.multipart.MaxUploadSizeExceededException.class)
    public String handleTooLarge(MaxUploadSizeExceededException ex,
                                 RedirectAttributes ra) {
        ra.addFlashAttribute("error",
            "Image too large. Max allowed: " + maxFileSize);
        return "redirect:/courses/list";
    }

    // (Optional) catch malformed/oversized multipart requests generically
    @ExceptionHandler(org.springframework.web.multipart.MultipartException.class)
    public String handleMultipart(MultipartException ex, RedirectAttributes ra) {
        ra.addFlashAttribute("error", "Upload failed: " + ex.getMostSpecificCause().getMessage());
        return "redirect:/courses/list";
    }
}
