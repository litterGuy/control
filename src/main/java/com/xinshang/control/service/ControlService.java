package com.xinshang.control.service;

import com.xinshang.control.dao.UserDao;
import com.xinshang.control.model.Operation;
import com.xinshang.control.model.User;
import com.xinshang.control.utils.BeanConvertUtil;
import com.xinshang.control.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.kie.api.runtime.KieSession;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
@Slf4j
public class ControlService {

    @Resource
    private KieSession kieSession;
    @Resource
    private UserDao userDao;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private IpaddressService ipaddressService;
    @Resource
    private OperationService operationService;

    public void riskControl(User user, Operation operation) {
        //更新user信息
        User orignUser = userDao.getByAccountId(user.getAccountId());
        if (orignUser != null) {
            BeanConvertUtil.combineSydwCore(user, orignUser);
            userDao.save(orignUser);
        } else {
            userDao.save(user);
        }

        //设置操作时间
        if (StringUtils.isNotEmpty(operation.getOperateTime())) {
            if (operation.getOperateTime().length() != 13 || Long.parseLong(operation.getOperateTime()) > new Date().getTime()) {
                operation.setOperateTime(System.currentTimeMillis() + "");
            }
        } else {
            operation.setOperateTime(System.currentTimeMillis() + "");
        }

        //获取操作地点、判断是否为中国
        String site = ipaddressService.getAddr(operation.getIp());
        operation.setSite(site);
        if (IpUtils.isCh(site)) {
            operation.setIsCh("yes");
        } else {
            operation.setIsCh("no");
        }

        //最近一次登录ip存入redis
        operationService.setLastIpAndTime(operation);
        operationService.insert(operation);

        kieSession.setGlobal("operationService", operationService);
        kieSession.insert(user);
        kieSession.insert(operation);
        int ruleFiredCount = kieSession.fireAllRules();
        log.info("use rule num is " + ruleFiredCount);
//        kieSession.dispose();
    }
}
