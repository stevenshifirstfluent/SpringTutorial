package sg.iss.nus.spring.tutorial.transactional.demo.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;

@Service
public class FileStorageService {
	public static final String UPLOAD_DIR = "uploads";
    private final Path uploadDir = Paths.get(UPLOAD_DIR);

    @PostConstruct
    public void init() {
        if (!Files.exists(uploadDir)) {
            try {
                Files.createDirectories(uploadDir);
                System.out.println("Upload directory created at: " + uploadDir.toAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Path getUploadDir() {
        return uploadDir;
    }
}