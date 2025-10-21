package poly.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import poly.edu.dao.ProductDAO;
import poly.edu.entity.Product;
import poly.edu.service.SessionService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
public class ProductController {
    // BAI 1
    //Link: http://localhost:8080/product/search

    // BAI 2
    //Link: http://localhost:8080/product/search-and-page

    // BAI 4
    //Link: http://localhost:8080/product/search-dsl

    @Autowired
    ProductDAO dao;

    @Autowired
    SessionService session;

    @GetMapping("/product/search")
    public String searchGet(Model model,
                            @RequestParam("min") Optional<Double> min,
                            @RequestParam("max") Optional<Double> max) {
        double minPrice = min.orElse(Double.MIN_VALUE);
        double maxPrice = max.orElse(Double.MAX_VALUE);
        List<Product> items = dao.findByPriceBetween(minPrice, maxPrice);
        model.addAttribute("items", items);
        return "product/search";
    }

    @PostMapping("/product/search")
    public String searchPost(Model model,
                             @RequestParam("min") Optional<Double> min,
                             @RequestParam("max") Optional<Double> max) {
        return searchGet(model, min, max);
    }

    @RequestMapping("/product/search-and-page")
    public String searchAndPage(Model model,
                                @RequestParam("keywords") Optional<String> kw,
                                @RequestParam("p") Optional<Integer> p) {
        // Lấy từ khóa từ request hoặc session
        String kwords = kw.orElse(session.get("keywords", ""));
        session.set("keywords", kwords);

        Pageable pageable = PageRequest.of(p.orElse(0), 5);
        Page<Product> page = dao.findAllByNameLike("%" + kwords + "%", pageable);

        model.addAttribute("page", page);
        model.addAttribute("keywords", kwords);
        return "product/search-and-page";
    }

    // -----------------------------
    // BAI 5 (THÊM): search + pagination using DSL (findAllByNameLike)
    // Link (GET):  /product/search-and-page-dsl
    // Link (POST): /product/search-and-page-dsl
    //
    // Logic:
    //  - Lấy keywords từ request hoặc session
    //  - Tạo PageRequest, gọi dao.findAllByNameLike("%kw%", pageable)
    //  - Đưa page + currentPage + totalPages + pageSize + keywords vào model
    // -----------------------------
    @GetMapping("/product/search-and-page-dsl")
    public String searchAndPageDsl(Model model,
                                   @RequestParam(name = "keywords", required = false) Optional<String> kw,
                                   @RequestParam(name = "p", required = false) Optional<Integer> p) {

        // Lấy từ khóa từ request hoặc session
        String kwords = kw.orElse(session.get("keywords", ""));
        session.set("keywords", kwords);

        int pageIndex = p.orElse(0);
        int pageSize = 5; // số item mỗi trang (bạn thay đổi nếu muốn)

        // chuẩn pattern cho LIKE
        String pattern = "%" + kwords.trim() + "%";

        // tạo pageable
        Pageable pageable = PageRequest.of(pageIndex, pageSize);

        // gọi repository DSL
        Page<Product> page = dao.findAllByNameLike(pattern, pageable);

        model.addAttribute("page", page);
        model.addAttribute("currentPage", pageIndex);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("keywords", kwords);

        // dùng lại view product/search-and-page (nếu bạn muốn view khác, đổi tên ở đây)
        return "product/search-and-page";
    }

    @PostMapping("/product/search-and-page-dsl")
    public String searchAndPageDslPost(Model model,
                                       @RequestParam(name = "keywords", required = false) Optional<String> kw,
                                       @RequestParam(name = "p", required = false) Optional<Integer> p) {
        return searchAndPageDsl(model, kw, p);
    }

    @GetMapping("/product/search-dsl")
    public String searchDslGet(Model model,
                               @RequestParam(name = "min", required = false) Optional<Double> min,
                               @RequestParam(name = "max", required = false) Optional<Double> max,
                               @RequestParam(name = "name", required = false) Optional<String> name) {

        List<Product> items;

        if (name.isPresent() && !name.get().trim().isEmpty()) {
            items = dao.findByNameContainingIgnoreCase(name.get().trim());
        } else if (min.isPresent() && max.isPresent()) {
            double minPrice = min.get();
            double maxPrice = max.get();
            if (minPrice > maxPrice) {
                double tmp = minPrice;
                minPrice = maxPrice;
                maxPrice = tmp;
            }
            items = dao.findByPriceBetween(minPrice, maxPrice);
        } else {
            items = dao.findAll();
        }

        model.addAttribute("items", items);
        model.addAttribute("min", min.orElse(null));
        model.addAttribute("max", max.orElse(null));
        model.addAttribute("name", name.orElse(null));
        return "product/search";
    }

    @PostMapping("/product/search-dsl")
    public String searchDslPost(Model model,
                                @RequestParam(name = "min", required = false) Optional<Double> min,
                                @RequestParam(name = "max", required = false) Optional<Double> max,
                                @RequestParam(name = "name", required = false) Optional<String> name) {
        return searchDslGet(model, min, max, name);
    }
}
