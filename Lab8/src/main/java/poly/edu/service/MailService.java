package poly.edu.service;

import lombok.Builder;
import lombok.Data;

/**
 * MailService interface (mở rộng với push để xếp hàng đợi).
 */
public interface MailService {

    @Data
    @Builder
    public static class Mail {
        @Builder.Default
        private String from = "tvghung@gmail.com";
        private String to;
        private String cc;
        private String bcc;
        private String subject;
        private String body;
        private String filenames;
    }

    void send(Mail mail);

    default void send(String to, String subject, String body) {
        Mail mail = Mail.builder().to(to).subject(subject).body(body).build();
        this.send(mail);
    }

    void push(Mail mail);

    default void push(String to, String subject, String body) {
        this.push(Mail.builder().to(to).subject(subject).body(body).build());
    }
}
