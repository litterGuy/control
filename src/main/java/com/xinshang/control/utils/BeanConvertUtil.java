package com.xinshang.control.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Map;

@Slf4j
public class BeanConvertUtil {

    private static Logger logger = LoggerFactory.getLogger(BeanConvertUtil.class);

    public static <T> T map2Bean(Map map, Class<T> beanClass) {
        try {
            //实例化传进来的类型
            T t = beanClass.newInstance();
            //将Map中的值设入bean中，其中Map中的key必须与目标对象中的属性名相同，否则不能实现拷贝
            BeanUtils.populate(t, map);
            return t;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    /**
     * @param sourceBean 被提取的对象bean
     * @param targetBean 用于合并的对象bean
     * @return targetBean 合并后的对象
     * @Title: combineSydwCore
     * @Description: 该方法是用于相同对象不同属性值的合并，如果两个相同对象中同一属性都有值，
     * 那么sourceBean中的值会覆盖tagetBean重点的值
     * @return: Object
     */
    @SuppressWarnings("unused")
    public static <T> boolean combineSydwCore(T sourceBean, T targetBean) {
        boolean rst = false;
        if (sourceBean == null) {
            return rst;
        }
        Class sourceBeanClass = sourceBean.getClass();
        Class targetBeanClass = targetBean.getClass();

        Field[] sourceFields = sourceBeanClass.getDeclaredFields();
        Field[] targetFields = sourceBeanClass.getDeclaredFields();
        for (int i = 0; i < sourceFields.length; i++) {
            Field sourceField = sourceFields[i];
            Field targetField = targetFields[i];
            sourceField.setAccessible(true);
            targetField.setAccessible(true);
            try {
                if (!(sourceField.get(sourceBean) == null)) {
                    targetField.set(targetBean, sourceField.get(sourceBean));
                    rst = true;
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                logger.error(e.getMessage());
            }
        }
        return rst;
    }

    /**
     * 判断对象是否为空或空值
     * @param o
     * @return
     */
    public static boolean allFieldIsNULL(Object o) {
        if(o == null){
            return true;
        }
        try {
            for (Field field : o.getClass().getDeclaredFields()) {
                field.setAccessible(true);

                Object object = field.get(o);
                if (object instanceof CharSequence) {
                    if (!org.springframework.util.ObjectUtils.isEmpty(object)) {
                        return false;
                    }
                } else {
                    if (null != object) {
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            log.error("判断对象属性为空异常", e);
        }
        return true;
    }
}
