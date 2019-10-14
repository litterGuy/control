package com.xinshang.control.dao;

import com.xinshang.control.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Integer> {
    User getByAccountId(String accountId);
}
