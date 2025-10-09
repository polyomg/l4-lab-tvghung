package poly.edu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

    //Link: http://localhost:8080/home/index

    @GetMapping("/index")
    public String index(Model model) {
        return "home/index";
    }

    @GetMapping("/about")
    public String about(Model model) {
        return "home/about";
    }
}

