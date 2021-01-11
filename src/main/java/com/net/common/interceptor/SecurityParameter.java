package com.net.common.interceptor;

import java.lang.annotation.*;

import org.springframework.web.bind.annotation.Mapping;


@Target({ElementType.PARAMETER,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Mapping
@Documented
public @interface SecurityParameter {

	  /**
     * 入参是否解密，默认false解密
     */
    boolean inDecode() default false;

    /**
     * 出参是否加密，默认false加密
     */
    boolean outEncode() default false;
}
