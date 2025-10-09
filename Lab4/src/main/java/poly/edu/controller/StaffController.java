package poly.edu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import poly.edu.entity.Staff;
import jakarta.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class StaffController {

    //Link: http://localhost:8080/staff/create/form

    @RequestMapping("/staff/create/form")
    public String createForm(Model model, @ModelAttribute("staff") Staff staff) {
        model.addAttribute("message", "Vui lòng nhập thông tin nhân viên!");
        return "staff-validate";
    }

    // Xử lý submit & validate
    @RequestMapping("/staff/create/save")
    public String createSave(Model model,
                             @RequestPart(value = "photo_file", required = false) MultipartFile photoFile,
                             @Valid @ModelAttribute("staff") Staff staff, Errors errors) {

        // Xử lý file upload (nếu có) — lưu tạm vào resources/static/uploads
        if (photoFile != null && !photoFile.isEmpty()) {
            try {
                String filename = photoFile.getOriginalFilename();
                Path uploadDir = Paths.get("src/main/resources/static/uploads");
                Files.createDirectories(uploadDir);
                Path filePath = uploadDir.resolve(filename);
                photoFile.transferTo(filePath.toFile());
                staff.setPhoto("uploads/" + filename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (errors.hasErrors()) {
            model.addAttribute("message", "Vui lòng sửa các lỗi sau!");
        } else {
            model.addAttribute("message", "Dữ liệu đã nhập đúng!");
        }

        model.addAttribute("staff", staff);
        return "staff-validate";
    }
}
