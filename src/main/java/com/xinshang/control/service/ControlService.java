package com.xinshang.control.service;

import com.xinshang.control.dao.UserDao;
import com.xinshang.control.model.Operation;
import com.xinshang.control.model.User;
import com.xinshang.control.utils.BeanConvertUtil;
import com.xinshang.control.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.drools.core.event.DefaultAgendaEventListener;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.AgendaEventListener;
import org.kie.api.event.rule.MatchCreatedEvent;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
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
        // 2022-03-04 08:18:34 Column 'hashed_password' cannot be null
        if(StringUtils.isEmpty(operation.getHashedPassword())){
            operation.setHashedPassword("");
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

        //设置监听，在规则匹配后打印
        Collection<AgendaEventListener> eventListeners = kieSession.getAgendaEventListeners();
        if (eventListeners.size() == 0) {
            kieSession.addEventListener(new DefaultAgendaEventListener() {

                public void afterMatchFired(AfterMatchFiredEvent event) {
                    super.afterMatchFired(event);
                    log.info("use rule is: {}", event.getMatch().getRule().getName());
                }
            });
        }

        FactHandle userHandle = kieSession.insert(user);
        Operation riskOperation = new Operation();
        //检验用户是否进行过身份校验
        if (StringUtils.isNotEmpty(operation.getIdentity()) && operation.getIdentity().equals("1")) {
            operationService.setIdentityFlag(operation);
        }
        if (operationService.hasIdentityFlag(operation)) {
            //如果有身份校验，只计算交易的风控，其他不计算
            riskOperation.setAccountId(operation.getAccountId());
            riskOperation.setOperationType(operation.getOperationType());
            riskOperation.setDollar(operation.getDollar());
            riskOperation.setId(operation.getId());
        }else{
            riskOperation = operation;
        }
        FactHandle operationHandle = kieSession.insert(riskOperation);
        int ruleFiredCount = kieSession.fireAllRules();

        kieSession.delete(userHandle);
        kieSession.delete(operationHandle);
        log.info("use rule num is " + ruleFiredCount);
//        kieSession.dispose();
    }
}
