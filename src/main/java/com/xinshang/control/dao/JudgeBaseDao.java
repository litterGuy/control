package com.xinshang.control.dao;

import com.xinshang.control.model.JudgeBase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JudgeBaseDao extends JpaRepository<JudgeBase, Integer> {
    JudgeBase getFirstByOrderById();
}
