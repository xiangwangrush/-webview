package common.rush.com.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by xiang.wang on 2019/12/9.
 * 自定义注解
 * 执行插件初始化操作
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface RushBridge {
}
