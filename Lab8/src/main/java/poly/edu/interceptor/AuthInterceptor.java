package poly.edu.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import poly.edu.entity.Account;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private HttpSession session;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String uri = request.getRequestURI();

        // Lưu URI muốn truy cập nếu chưa login
        session.setAttribute("securityUri", uri);

        Account user = (Account) session.getAttribute("user");

        // Nếu chưa login
        if (user == null) {
            session.setAttribute("message", "Bạn cần đăng nhập để truy cập trang này!");
            response.sendRedirect("/auth/login");
            return false;
        }

        // Nếu URL bắt đầu bằng /admin và user không phải admin
        if (uri.startsWith("/admin") && !user.isAdmin()) {
            session.setAttribute("message", "Bạn không có quyền truy cập trang này!");
            response.sendRedirect("/auth/login");
            return false;
        }

        // Nếu mọi thứ OK, cho phép request tiếp tục
        return true;
    }
}
