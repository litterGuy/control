package com.xinshang.control.service;

import com.alibaba.fastjson.JSON;
import com.xinshang.control.dao.IpaddressDao;
import com.xinshang.control.dao.OperationDao;
import com.xinshang.control.interceptor.JudgeInterceptor;
import com.xinshang.control.model.Ipaddress;
import com.xinshang.control.model.Operation;
import com.xinshang.control.utils.BeanConvertUtil;
import com.xinshang.control.utils.DateUtils;
import com.xinshang.control.utils.RulesUtil;
import com.xinshang.control.utils.constant.ControlConstant;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class OperationService {

    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private OperationDao dao;
    @Resource
    private IpaddressDao ipaddressDao;
    @Resource
    private SitelocationService sitelocationService;

    public void setLastIpAndTime(Operation operation) {
        //6分钟过期
        redisTemplate.opsForValue().set(operation.getAccountId() + ":ip", operation.getIp(), 360, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(operation.getAccountId() + ":" + operation.getIp(), operation.getOperateTime(), 360, TimeUnit.SECONDS);
    }

    public long getLastIpTime(Operation operation) {
        String ipKey = operation.getAccountId() + ":ip";
        String time = "0";
        if (redisTemplate.hasKey(ipKey)) {
            String ip = redisTemplate.opsForValue().get(ipKey).toString();
            time = redisTemplate.opsForValue().get(operation.getAccountId() + ":" + ip).toString();
        }
        return Long.parseLong(time);
    }

    public void setIpcTime(Operation operation) {
        //一小时过期
        redisTemplate.opsForList().rightPushAll(operation.getAccountId() + ":ipc", Arrays.asList(operation.getOperateTime()), 3600, TimeUnit.SECONDS);
    }

    public boolean judgeIpcNum(Operation operation) {
        int ipcNum = JudgeInterceptor.base.getIpcnum();
        long ipctime = JudgeInterceptor.base.getIpctime();
        List<String> list = redisTemplate.opsForList().range(operation.getAccountId() + ":ipc", 0, -1);
        long nowtime = Long.parseLong(operation.getOperateTime());
        int num = 0;
        for (int i = 0; i < list.size(); i++) {
            if (nowtime - Long.parseLong(list.get(i)) < ipctime * 1000) {
                num++;
            }
        }
        if (num > ipcNum) {
            return true;
        }
        return false;
    }

    public boolean judgeIpNew(Operation operation) {
        long ipntime = JudgeInterceptor.base.getIpntime();
        long lastIpTime = getLastIpTime(operation);
        long nowtime = Long.parseLong(operation.getOperateTime());
        if (nowtime - lastIpTime < ipntime * 1000) {
            return true;
        }
        return false;
    }

    public boolean insert(Operation operation) {
        if (operation == null) {
            return false;
        }
        dao.save(operation);
        return true;
    }

    /**
     * 判断距离是否过远
     *
     * @param operation
     * @return
     */
    public boolean isFar(Operation operation) {
        int distance = JudgeInterceptor.base.getDistance();
        int oftensite = JudgeInterceptor.base.getOftensite();

        List<String> ips = dao.getOftenIpNum(operation.getAccountId(), oftensite);
        if (CollectionUtils.isEmpty(ips)) {
            return false;
        }
        List<Ipaddress> ipaddressList = ipaddressDao.getByIpIn(ips);
        for (Ipaddress ipaddress : ipaddressList) {
            boolean flag = sitelocationService.getAddrIsFar(operation.getSite(), ipaddress.getSite(), distance);
            if (!flag) {
                return false;
            }
        }
        return true;
    }

    public boolean isNewAddr(Operation operation, String json) {
        Operation param = JSON.parseObject(json, Operation.class);
        //如果请求参数中获取不到，则不进行条件过滤
        if (BeanConvertUtil.allFieldIsNULL(param)) {
            return false;
        }
        param.setAccountId(operation.getAccountId());
        //查询该用户总条数，过滤掉新用户操作的情况
        int total = dao.countByAccountId(operation.getAccountId());
        if (total <= 0) {
            return false;
        }
        long size = this.countByOption(param);
        return size > 0 ? false : true;
    }

    public long countByOption(Operation operation) {
        Example<Operation> example = Example.of(operation);
        return dao.count(example);
    }

    //time 间隔时间，单位为秒
    public boolean judgeFailNum(Operation operation) {
        int loginfailnum = JudgeInterceptor.base.getLoginfailnum();
        long loginfailtime = JudgeInterceptor.base.getLoginfailtime();
        List<String> list = redisTemplate.opsForList().range(operation.getAccountId() + "fail", 0, -1);
        long nowtime = Long.parseLong(operation.getOperateTime());
        int num = 0;
        for (int i = 0; i < list.size(); i++) {
            if (nowtime - Long.parseLong(list.get(i)) < loginfailtime * 1000) {
                num++;
            }
        }
        if (num > loginfailnum) {
            return true;
        }
        return false;
    }

    public void setFailTime(Operation operation) {//1小时过期
        redisTemplate.opsForList().rightPushAll(operation.getAccountId() + "fail", Arrays.asList(operation.getOperateTime()), 3600, TimeUnit.SECONDS);
    }

    public boolean judgeOftenIp(Operation operation) {
        int oftenip = JudgeInterceptor.base.getIpntime();
        List<String> ips = dao.getOftenIpNum(operation.getAccountId(), oftenip);
        for (String str : ips) {
            if (str.equals(operation.getIp())) {
                return true;
            }
        }
        return false;
    }

    public String findOftenDevice(String accountId) {
        String deviceType = dao.getOftenDevice(accountId);
        if (StringUtils.isEmpty(deviceType)) {
            return "";
        }
        return deviceType;
    }

    /**
     * 对行为进行校验
     *
     * @param operation
     * @return
     */
    public boolean validOperation(Operation operation) {
        boolean flag = true;
        switch (operation.getOperationType()) {
            case ControlConstant.OPERATOR_COIN:
                //是否超过最大金额
                if (StringUtils.isNotEmpty(operation.getDollar())) {
                    Double dollar = Double.parseDouble(operation.getDollar());
                    if (JudgeInterceptor.base.getDollerlimit() > 0 && dollar > JudgeInterceptor.base.getDollerlimit()) {
                        RulesUtil.addScore(operation, 100);
                    }
                    //是否操作次数超标
                    if (this.validOperaterNum(operation.getAccountId(), operation.getOperationType())) {
                        RulesUtil.addScore(operation, 100);
                    }
                }
                break;
            case ControlConstant.OPERATOR_WITHDRAW:
                //是否超过最大金额
                if (StringUtils.isNotEmpty(operation.getDollar())) {
                    Double dollar = Double.parseDouble(operation.getDollar());
                    if (JudgeInterceptor.base.getWithdrawdollerlimit() > 0 && dollar > JudgeInterceptor.base.getWithdrawdollerlimit()) {
                        RulesUtil.addScore(operation, 100);
                    }
                    //是否操作次数超标
                    if (this.validOperaterNum(operation.getAccountId(), operation.getOperationType())) {
                        RulesUtil.addScore(operation, 100);
                    }
                }
                break;
            case ControlConstant.OPERATOR_DEAL:
                break;
            default:
                flag = false;
        }
        return flag;
    }

    /**
     * 验证单位时间内某操作类型次数是否超过限制
     *
     * @param accountId
     * @param operationType
     * @return
     */
    private boolean validOperaterNum(String accountId, String operationType) {
        if (operationType.equals(ControlConstant.OPERATOR_COIN) && JudgeInterceptor.base.getCoinnumlimit() <= 0) {
            return false;
        }
        if (operationType.equals(ControlConstant.OPERATOR_WITHDRAW) && JudgeInterceptor.base.getWithdrawnumlimit() <= 0) {
            return false;
        }
        Date now = new Date();
        String start = DateUtils.getStringDate(DateUtils.getStartTime(now));
        String end = DateUtils.getStringDate(DateUtils.getEndTime(now));
        int num = dao.countByAccountIdAndOperationTypeAndOperateTimeBetween(accountId, operationType, start, end);
        //提币校验
        if (operationType.equals(ControlConstant.OPERATOR_COIN)) {
            if (num > JudgeInterceptor.base.getCoinnumlimit()) {
                return true;
            }
        }
        if (operationType.equals(ControlConstant.OPERATOR_WITHDRAW)) {
            if (num > JudgeInterceptor.base.getWithdrawnumlimit()) {
                return true;
            }
        }
        return false;
    }
}
