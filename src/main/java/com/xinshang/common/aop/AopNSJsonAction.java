package com.xinshang.common.aop;

import com.xinshang.common.exception.CommonErrorCode;
import com.xinshang.common.exception.NSException;
import com.xinshang.common.exception.SystemInternalException;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Aspect
public class AopNSJsonAction {

    @Around("execution(* com.xinshang.control.action..*.*(..))")
    public Object doAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) {
        Map<String, Object> rst = new HashMap<>();

        int errCode = 0;
        String errMsg = "successful";
        Object data;
        try {
            data = proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            NSException e1;
            if (e instanceof NSException) {
                e1 = (NSException) e;
                errCode = e1.getCode();
                errMsg = e1.getMsg();
            } else if (StringUtils.isNotEmpty(e.getMessage())) {
                e1 = new NSException(CommonErrorCode.UNKOWN_EXCEPTION);
                errCode = e1.getCode();
                errMsg = e.getMessage();
            } else {
                e1 = new SystemInternalException();
                errCode = e1.getCode();
                errMsg = e1.getMsg();
            }
            data = null;
        }
        rst.put("errCode", errCode);
        rst.put("errMsg", errMsg);
        rst.put("data", data);
        return rst;
    }
}
