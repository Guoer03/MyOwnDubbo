package EasyVersion.Server.AutoService;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceScan {
    /*
    * ServiceScan放在启动的入口类上，标识服务的扫描的包的范围。
    * ServiceScan的值定义为扫描范围的根包，默认值为入口类所在的包
    * 扫描时会扫描该包及其子包下的所有类，找到标记有Service的类，并注册。
    * */
    public String value() default "";
}
