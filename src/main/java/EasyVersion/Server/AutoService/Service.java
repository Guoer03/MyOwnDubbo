package EasyVersion.Server.AutoService;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
/*注解的作用目标--ElementType.TYPE(类，接口，枚举)。
* ElementType.FIELD(字段，枚举的常量)
* */
@Retention(RetentionPolicy.RUNTIME)
/*
* 规定注解的生命周期
* RetentionPolicy.RUNTIME--注解不仅被保存到class文件中，jvm加载class文件之后，仍然存在。
* */
public @interface Service {
    /*
    * Service注解放在一个类上，标识这个类提供一个服务。Service注解的值定义为该服务的名称，默认值是该类的完整类名
    * */
    public String[] name() default{};
}
