package org.luban.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Target(value = {ElementType.METHOD,ElementType.TYPE})
public @interface RpcService {

    int weight() default 50;

    String group() default "default";

    String version() default "1.0.0";

}
