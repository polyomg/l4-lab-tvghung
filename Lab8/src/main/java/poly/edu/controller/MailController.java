package poly.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import poly.edu.service.MailService;
import poly.edu.service.MailService.Mail;

@Controller
@RequestMapping("/mail")
public class MailController {

    //Bai3: http://localhost:8080/mail/send-form

    @Autowired
    private MailService mailService;

    // Hiển thị form gửi mail
    @GetMapping("/send-form")
    public String showForm() {
        return "send-mail"; // tên file html chứa form
    }

    // Xử lý gửi mail từ form
    @PostMapping("/send-form")
    public String sendForm(
            @RequestParam(name = "from", required = false) String from,
            @RequestParam("to") String to,
            @RequestParam(name = "cc", required = false) String cc,
            @RequestParam(name = "bcc", required = false) String bcc,
            @RequestParam("subject") String subject,
            @RequestParam("body") String body,
            @RequestParam(name = "filenames", required = false) String filenames,
            @RequestParam("mode") String mode, // "direct" hoặc "queue"
            Model model
    ) {
        Mail mail = Mail.builder()
                .from(from != null && !from.isEmpty() ? from : null)
                .to(to)
                .cc(cc)
                .bcc(bcc)
                .subject(subject)
                .body(body)
                .filenames(filenames)
                .build();

        try {
            if ("direct".equals(mode)) {
                mailService.send(mail);
                model.addAttribute("message", "Mail đã gửi trực tiếp thành công đến " + to + "!");
            } else if ("queue".equals(mode)) {
                mailService.push(mail);
                model.addAttribute("message", "Mail đã được xếp vào hàng đợi đến " + to + "!");
            } else {
                model.addAttribute("message", "Chọn chế độ gửi hợp lệ!");
            }
        } catch (Exception e) {
            model.addAttribute("message", "Lỗi khi gửi mail đến " + to + ": " + e.getMessage());
        }

        return "send-mail"; // vẫn trả về form để gửi tiếp
    }


    // Giữ lại endpoint cũ test push vào hàng đợi
    @GetMapping("/send")
    @ResponseBody
    public String sendTest() {
        mailService.push("tvghung@gmail.com", "Subject", "Body");
        return "Mail của bạn đã được xếp vào hàng đợi";
    }
}
