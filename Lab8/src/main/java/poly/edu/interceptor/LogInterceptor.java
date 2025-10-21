package poly.edu.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import poly.edu.dao.LogDAO;
import poly.edu.entity.Account;
import poly.edu.entity.Log;

import java.util.Date;

@Component
public class LogInterceptor implements HandlerInterceptor {

    @Autowired
    private LogDAO dao;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

        Account user = (Account) request.getSession().getAttribute("user");

        if(user != null) {
            Log log = new Log(user.getUsername(), request.getRequestURI(), new Date());
            dao.save(log);
        }

        return true;
    }
}
