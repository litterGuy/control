package com.xinshang.control.action;

import com.xinshang.common.exception.NSException;
import com.xinshang.control.model.Operation;
import com.xinshang.control.model.User;
import com.xinshang.control.service.ControlService;
import com.xinshang.control.service.OperationService;
import com.xinshang.control.utils.BeanConvertUtil;
import com.xinshang.control.utils.checker.OperationChecker;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("control")
public class ControlAction {

    @Resource
    private OperationChecker operationChecker;
    @Resource
    private ControlService service;
    @Resource
    private OperationService operationService;

    @GetMapping("riskControl")
    public Object riskControl(HttpServletRequest request) throws NSException {
        User user = BeanConvertUtil.map2Bean(request.getParameterMap(), User.class);
        Operation operation = BeanConvertUtil.map2Bean(request.getParameterMap(), Operation.class);
        operationChecker.checkParam(operation);
        service.riskControl(user, operation);

        operation.setScore(operation.getScore() != null ? operation.getScore() : "0");
        operationService.updateScoreById(operation.getId(), operation.getScore());

        Map<String, Object> rst = new HashMap<String, Object>() {{
            put("score", operation.getScore());
            //TODO 随后确认该值是否合适
            put("risk", Integer.parseInt(operation.getScore()) > 70 ? true : false);
        }};
        return rst;
    }
}
