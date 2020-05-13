package com.lagou.edu.mvcframework.annotations;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)    // JVM加载到内存中之后也能生效
public @interface YuController {
    String value() default "";
}
