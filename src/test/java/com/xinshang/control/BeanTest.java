package com.xinshang.control;

import com.xinshang.control.model.User;
import com.xinshang.control.utils.BeanConvertUtil;
import org.junit.Test;

public class BeanTest {

    @Test
    public void bean() {
        User user = new User();
        System.out.println(BeanConvertUtil.allFieldIsNULL(user));
        user.setAge(18);
        System.out.println(BeanConvertUtil.allFieldIsNULL(user));
        User u2 = null;
        System.out.println(BeanConvertUtil.allFieldIsNULL(u2));
    }
}
