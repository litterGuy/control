package com.xinshang.control.interceptor;

import com.xinshang.control.dao.JudgeBaseDao;
import com.xinshang.control.model.JudgeBase;
import com.xinshang.control.utils.BeanConvertUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JudgeInterceptor implements HandlerInterceptor {

    public static JudgeBase base;

    @Resource
    private JudgeBaseDao judgeBaseDao;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (base == null) {
            base = judgeBaseDao.getFirstByOrderById();
        }
        JudgeBase tmp = BeanConvertUtil.map2Bean(request.getParameterMap(), JudgeBase.class);
        //去掉设置id的部分
        if (tmp != null) {
            tmp.setId(null);
        }
        boolean change = BeanConvertUtil.combineSydwCore(tmp, base);
        if (change) {
            judgeBaseDao.save(base);
        }
        return true;
    }
}
