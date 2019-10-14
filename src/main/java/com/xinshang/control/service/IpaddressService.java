package com.xinshang.control.service;

import com.xinshang.control.dao.IpaddressDao;
import com.xinshang.control.model.Ipaddress;
import com.xinshang.control.utils.IpUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class IpaddressService {

    @Resource
    private IpaddressDao dao;

    public String getAddr(String ip) {
        if (StringUtils.isEmpty(ip)) {
            return null;
        }

        Ipaddress ipaddress = dao.getByIp(ip);
        if (ipaddress != null) {
            return ipaddress.getSite();
        }

        String site = IpUtils.getAddress(ip);
        Ipaddress bornData = new Ipaddress();
        bornData.setIp(ip);
        bornData.setSite(site);
        dao.save(bornData);
        return site;
    }

}
