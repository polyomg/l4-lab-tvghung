package poly.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import poly.edu.dao.ProductDAO;
import poly.edu.entity.Report;
import java.util.List;

@Controller
public class ReportController {
    //Bai3
    //Link: http://localhost:8080/report/inventory-by-category

    @Autowired
    private ProductDAO dao;

    @RequestMapping("/report/inventory-by-category")
    public String inventory(Model model) {
        List<Report> items = dao.getInventoryByCategory();
        model.addAttribute("items", items);
        return "report/inventory-by-category";
    }

}
