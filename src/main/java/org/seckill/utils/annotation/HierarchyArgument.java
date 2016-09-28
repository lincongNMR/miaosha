package org.seckill.utils.annotation;


import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ValueConstants;

import java.lang.annotation.*;

/**
 * Created by linyitian on 2016/7/22.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HierarchyArgument{
    String defaultValue() default ValueConstants.DEFAULT_NONE;

}
