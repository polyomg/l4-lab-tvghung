package poly.edu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import poly.edu.entity.Staff;

@Controller
public class Bai1Controller {

    // Link: http://localhost:8080/staff/detail
    @RequestMapping("/staff/detail")
    public String detail(Model model) {
        Staff staff = Staff.builder()
                .id("user@gmail.com")
                .fullname("nguyễn văn user")
                .level(2)
                .build();

        model.addAttribute("staff", staff);
        return "staff-detail";
    }
}
