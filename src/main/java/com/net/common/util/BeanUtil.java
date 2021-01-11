package com.net.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
 
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
public class BeanUtil {
 
    public static final Logger logger = LoggerFactory.getLogger(BeanUtil.class);
 
 
    /**
     * 根据clazz的属性进行拷贝
     */
    public static <T> T copyProperties(Object source, Class<T> clazz) {
        if (source == null) {
            return null;
        }
        try {
            T target = BeanUtils.instantiateClass(clazz);
            BeanUtils.copyProperties(source, target);
            return target;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
 
    /**
     * 根据clazz的属性进行拷贝+忽略属性
     */
    public static <T> T copyPropertiesIgnore(Object source, Class<T> clazz,
                                             String... ignoreProperties) {
        if (source == null) {
            return null;
        }
        try {
            T target = BeanUtils.instantiateClass(clazz);
            BeanUtils.copyProperties(source, target, ignoreProperties);
            return target;
        } catch (Exception e) {
        }
        return null;
    }
 
    /**
     * 拷贝指定属性
     */
    public static <T> T copyPropertiesSpecific(Object source, Class<T> clazz,
                                               String... specificProperties) {
        if (source == null) {
            return null;
        }
        try {
            T target = BeanUtils.instantiate(clazz);
            if (specificProperties == null) {
                return target;
            }
            List<String> specificList = Arrays.asList(specificProperties);
            copySpecificProperties(source, target, specificList);
            return target;
        } catch (Exception e) {
        }
        return null;
    }
 
    /**
     * 拷贝指定属性
     */
    public static <T> T copyPropertiesSpecific(Object source, T target,
                                               String... specificProperties) {
        if (source == null) {
            return target;
        }
        try {
            if (specificProperties != null) {
                List<String> specificList = Arrays.asList(specificProperties);
                copySpecificProperties(source, target, specificList);
            }
        } catch (Exception e) {
        }
        return target;
    }
 
    private static void copySpecificProperties(final Object source,
                                               final Object target, final Iterable<String> properties) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        final BeanWrapper trg = new BeanWrapperImpl(target);
 
        for (final String propertyName : properties) {
            trg.setPropertyValue(propertyName,
                    src.getPropertyValue(propertyName));
        }
    }
 
    /**
     *      * 实体类转Map<String,String>
     *      * @param obj
     *      * @return
     *      
     */
    public static Map<String, String> convertBeanToMap(Object obj) {
        if (obj == null) {
            return null;
        }
        Map<String, String> map = new HashMap<String, String>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    if (null == value) {
                        map.put(key, "");
                    } else {
                        map.put(key, String.valueOf(value));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
 
    /**
     *      * map 转实体类
     *      * @param clazz
     *      * @param map
     *      * @param <T>
     *      * @return
     *      
     */
    public static <T> T convertMapToBean(Class<T> clazz, Map<String, Object> map) {
        T obj = null;
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            obj = clazz.newInstance(); // 创建 JavaBean 对象
                // 给 JavaBean 对象的属性赋值
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i < propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();
                if (map.containsKey(propertyName)) {
                    // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
                    Object value = map.get(propertyName);
                    if ("".equals(value)) {
                        value = null;
                    }
                    Object[] args = new Object[1];
                    args[0] = value;
                    descriptor.getWriteMethod().invoke(obj, args);
                }
            }
        } catch (IllegalAccessException e) {
            logger.error("convertMapToBean 实例化JavaBean失败 Error{}", e);
        } catch (IntrospectionException e) {
            logger.error("convertMapToBean 分析类属性失败 Error{}", e);
        } catch (IllegalArgumentException e) {
            logger.error("convertMapToBean 映射错误 Error{}", e);
        } catch (InstantiationException e) {
            logger.error("convertMapToBean 实例化 JavaBean 失败 Error{}", e);
        } catch (InvocationTargetException e) {
            logger.error("convertMapToBean字段映射失败 Error{}", e);
        } catch (Exception e) {
            logger.error("convertMapToBean Error{}", e);
        }
        return (T) obj;
    }
 
}

