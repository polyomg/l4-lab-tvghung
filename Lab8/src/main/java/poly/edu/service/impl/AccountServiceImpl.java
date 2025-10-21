package poly.edu.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import poly.edu.dao.AccountDAO;
import poly.edu.entity.Account;
import poly.edu.service.AccountService;

import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountDAO accountDAO;

    @Override
    public Account findById(String username) {
        Optional<Account> optional = accountDAO.findById(username);
        return optional.orElse(null);
    }
}
