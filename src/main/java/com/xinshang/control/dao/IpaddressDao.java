package com.xinshang.control.dao;

import com.xinshang.control.model.Ipaddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IpaddressDao extends JpaRepository<Ipaddress, Integer> {
    Ipaddress getByIp(String ip);

    List<Ipaddress> getByIpIn(List<String> ips);
}
