package com.xinshang.control.service;

import com.xinshang.control.dao.SitelocationDao;
import com.xinshang.control.model.Sitelocation;
import com.xinshang.control.utils.SiteUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;

@Service
@Slf4j
public class SitelocationService {

    @Resource
    private SitelocationDao dao;

    public boolean getAddrIsFar(String opAddr, String ipAddr, int distance) {
        if (StringUtils.isEmpty(opAddr) || StringUtils.isEmpty(ipAddr)) {
            return true;
        }
        //获取两个地址的坐标
        Object[] o1 = null, o2 = null;
        o1 = this.getCoordinate(opAddr);
        o2 = this.getCoordinate(ipAddr);
        if ((o1[0].equals("0") && o1[1].equals("0")) || (o2[0].equals("0") && o1[1].equals("0"))) {
            return true;
        }
        double dist = SiteUtils.getDistance(Double.parseDouble((String) o1[0]), Double.parseDouble(String.valueOf(o1[1])), Double.parseDouble(String.valueOf(o2[0])), Double.parseDouble(String.valueOf(o2[1])));
        if (dist < distance) {
            return false;
        }
        return true;
    }

    public Object[] getCoordinate(String addr) {
        String lng = "0";// 经度
        String lat = "0";// 纬度
        String address = null;
        if (addr == null || addr == "") {
            return new Object[]{"0", "0"};
        }
        Sitelocation sitelocation = dao.getBySite(addr);
        if (sitelocation != null) {
            return new Object[]{sitelocation.getLng(), sitelocation.getLat()};
        }
        Object[] crd = {lng, lat};
        try {
            crd = SiteUtils.getCoordinate(addr);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        Sitelocation newSite = new Sitelocation();
        newSite.setSite(addr);
        newSite.setLng(crd[0].toString());
        newSite.setLat(crd[0].toString());
        dao.save(newSite);
        return crd;
    }
}
