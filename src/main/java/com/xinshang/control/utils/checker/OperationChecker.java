package com.xinshang.control.utils.checker;

import com.xinshang.common.BeanChecker;
import com.xinshang.common.exception.NSException;
import com.xinshang.common.exception.ParameterException;
import com.xinshang.control.model.Operation;
import com.xinshang.control.utils.IpUtils;
import com.xinshang.control.utils.constant.ControlConstant;
import org.springframework.stereotype.Component;

@Component
public class OperationChecker extends BeanChecker {

    public void checkParam(Operation operation) throws NSException {
        this.checkBase(operation);
    }

    private void checkBase(Operation req) throws NSException {
        assertIsNull(req, "对象为空");
        assertIsNull(req.getAccountId(), "用户id为空");
        assertIsEmpty(req.getIp(), "ip为空");
        assertIsEmpty(req.getOperationType(), "操作类型为空");
        assertIsEmpty(req.getDeviceType(), "设备类型为空");
        if (!IpUtils.isIp(req.getIp())) {
            throw new ParameterException("ip不合法");
        }
        if (req.getOperationType().equals(ControlConstant.OPERATOR_COIN) || req.getOperationType().equals(ControlConstant.OPERATOR_WITHDRAW)) {
            assertIsEmpty(req.getDollar(), "金额为空");
        }
    }
}
