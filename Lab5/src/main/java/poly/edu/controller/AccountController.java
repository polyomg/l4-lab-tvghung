package poly.edu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import poly.edu.service.ParamService;
import poly.edu.service.SessionService;
import poly.edu.service.CookieService;

import java.io.File;

@Controller
@RequestMapping("/account")
public class AccountController {

    private final ParamService paramService;
    private final SessionService sessionService;
    private final CookieService cookieService;

    public AccountController(ParamService paramService, SessionService sessionService, CookieService cookieService) {
        this.paramService = paramService;
        this.sessionService = sessionService;
        this.cookieService = cookieService;
    }

    // --- Login (đã có) ---
    @GetMapping("/login")
    public String login1() {
        return "login";
    }

    @PostMapping("/login")
    public String login2(Model model) {
        String un = paramService.getString("username", "");
        String pw = paramService.getString("password", "");
        boolean rm = paramService.getBoolean("remember", false);

        if (un.equals("poly") && pw.equals("123")) {
            sessionService.set("username", un);
            if (rm) cookieService.add("user", un, 10);
            else cookieService.remove("user");
            model.addAttribute("message", "Đăng nhập thành công!");
            return "redirect:/home";
        } else {
            model.addAttribute("message", "Sai tài khoản hoặc mật khẩu!");
            return "login";
        }
    }

    // --- REGISTER ---
    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerSave(Model model,
                               @RequestParam("photo_file") MultipartFile photoFile) {
        // đọc các tham số
        String username = paramService.getString("username", "");
        String password = paramService.getString("password", "");

        // kiểm tra tối giản
        if (username.isBlank() || password.isBlank()) {
            model.addAttribute("message", "Vui lòng nhập username và password!");
            return "register";
        }

        // lưu file (thư mục "uploads" tương đối so với webroot)
        File saved = null;
        try {
            saved = paramService.save(photoFile, "uploads");
        } catch (RuntimeException ex) {
            model.addAttribute("message", "Lỗi lưu ảnh: " + ex.getMessage());
            return "register";
        }

        // nếu lưu thành công, ta có thể lưu tên file vào session (hoặc DB)
        if (saved != null) {
            sessionService.set("reg_username", username);
            sessionService.set("reg_avatar", saved.getName()); // hoặc đường dẫn
            model.addAttribute("message", "Đăng ký thành công! Vui lòng đăng nhập.");
            // chuyển hướng về login để đăng nhập
            return "redirect:login";
        } else {
            // nếu không có file, vẫn cho phép đăng ký nhưng lưu session
            sessionService.set("reg_username", username);
            model.addAttribute("message", "Đăng ký thành công (không có ảnh). Vui lòng đăng nhập.");
            return "redirect:login";
        }
    }

    @GetMapping("/logout")
    public String logout(Model model) {
        sessionService.remove("username");
        cookieService.remove("user");
        model.addAttribute("message", "Đăng xuất thành công!");
        return "login";
    }
}
