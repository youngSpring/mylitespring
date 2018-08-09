package org.litespring.stereotype;

import java.lang.annotation.*;

@Target(ElementType.TYPE)//表示这个注解可以放在哪里
@Retention(RetentionPolicy.RUNTIME)//表示这个注解保留到什么时候
@Documented
public @interface Component {
    /**
     * The value may indicate a suggestion for a logical component name,
     * to be turned into a Spring bean in case of an autodetected component.
     * @return the suggested component name, if any
     * 在被spring扫描后，形成的bean的名字就是这个value给的名字
     */
    String value() default "";
}
