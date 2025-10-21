package poly.edu.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import poly.edu.service.MailService;
import jakarta.mail.internet.MimeMessage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service("mailService")
public class MailServiceImpl implements MailService {

    List<Mail> queue = new ArrayList<>();

    @Override
    public void push(Mail mail) {
        queue.add(mail);
    }

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void send(Mail mail) {
        try {
            // 1. Tạo MimeMessage
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // 2.1. Ghi thông tin người gửi
            if (!isNullOrEmpty(mail.getFrom())) {
                helper.setFrom(mail.getFrom());
                helper.setReplyTo(mail.getFrom());
            }

            // 2.2. Ghi thông tin người nhận
            if (!isNullOrEmpty(mail.getTo())) {
                helper.setTo(mail.getTo());
            } else {
                throw new RuntimeException("Missing 'to' address");
            }

            if (!isNullOrEmpty(mail.getCc())) {
                helper.setCc(mail.getCc());
            }

            if (!isNullOrEmpty(mail.getBcc())) {
                helper.setBcc(mail.getBcc());
            }

            // 2.3. Tiêu đề & nội dung (true = HTML)
            helper.setSubject(mail.getSubject() == null ? "" : mail.getSubject());
            helper.setText(mail.getBody() == null ? "" : mail.getBody(), true);

            // 2.4. Đính kèm file (nếu có)
            String filenames = mail.getFilenames();
            if (!isNullOrEmpty(filenames)) {
                // phân tách bởi dấu phẩy hoặc chấm phẩy
                String[] parts = filenames.split("[,;]+");
                for (String part : parts) {
                    String path = part.trim();
                    if (path.length() == 0) continue;
                    File file = new File(path);
                    if (file.exists() && file.isFile()) {
                        FileSystemResource resource = new FileSystemResource(file);
                        helper.addAttachment(file.getName(), resource);
                    } else {
                        // Tùy bạn: bỏ qua file không tồn tại hoặc ném lỗi. Ở đây ta bỏ qua và log.
                        System.err.println("Attachment not found: " + path);
                    }
                }
            }

            // 3. Gửi mail
            System.out.println("Sending mail to: " + mail.getTo());
            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isNullOrEmpty(String text) {
        return (text == null || text.trim().length() == 0);
    }

    @Scheduled(fixedDelay = 500)
    public void run() {
        while (!queue.isEmpty()) {
            try {
                Mail mail = queue.remove(0);
                this.send(mail);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
