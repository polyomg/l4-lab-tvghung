package poly.edu.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import poly.edu.entity.Account;
import poly.edu.service.AccountService;

@Controller
@RequestMapping("/auth")
public class AuthController {

    //Bai5: http://localhost:8080/auth/login

    @Autowired
    private AccountService accountService;

    @Autowired
    private HttpSession session;

    // Hiển thị form login
    @GetMapping("/login")
    public String loginForm(Model model) {
        return "auth/login";
    }

    // Xử lý POST login
    @PostMapping("/login")
    public String loginProcess(Model model,
                               @RequestParam("username") String username,
                               @RequestParam("password") String password) {

        Account user = accountService.findById(username);

        if (user == null) {
            model.addAttribute("message", "Invalid username!");
        } else if (!user.getPassword().equals(password)) {
            model.addAttribute("message", "Invalid password!");
        } else {
            session.setAttribute("user", user);
            model.addAttribute("message", "Login successfully!");

            // Nếu trước đó user truy cập URI bảo mật
            String securityUri = (String) session.getAttribute("securityUri");
            if (securityUri != null) {
                session.removeAttribute("securityUri");
                return "redirect:" + securityUri;
            }
        }


        return "auth/login";
    }
}
