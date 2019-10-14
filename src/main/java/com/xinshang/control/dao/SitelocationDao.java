package com.xinshang.control.dao;

import com.xinshang.control.model.Sitelocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SitelocationDao extends JpaRepository<Sitelocation, Integer> {
    Sitelocation getBySite(String site);
}
