package EasyVersion.Server;
import EasyVersion.Exception.RpcException;
import EasyVersion.ReflectUtil;
import EasyVersion.Serializers.CommonSerializer;
import EasyVersion.Server.AutoService.Service;
import EasyVersion.Server.AutoService.ServiceScan;
import EasyVersion.register.ServiceProvider;
import EasyVersion.register.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Set;

public abstract class CommonDubboServer {
    protected String host;
    protected int port;
    private Logger logger=LoggerFactory.getLogger(CommonDubboServer.class);
    public CommonDubboServer(String host, int port, ServiceRegistry serviceRegistry, CommonSerializer serializer, ServiceProvider serviceProvider) throws RpcException, InstantiationException, IllegalAccessException {
        this.host = host;
        this.port = port;
        this.serviceRegistry = serviceRegistry;
        this.serializer = serializer;
        this.serviceProvider = serviceProvider;
        ScanServices();
    }
    protected ServiceRegistry serviceRegistry;
    protected CommonSerializer serializer;
    protected ServiceProvider serviceProvider;
    public abstract void start();
    public abstract <T> CommonDubboServer publishService(Object service, String serviceName) throws RpcException;
    public void clearRegistry(){

        for (String serviceName:
             serviceProvider.getLocalServiceNames()) {
            serviceRegistry.ServiceLogOut(serviceName,new InetSocketAddress(host,port));
        }
    }
    public void ScanServices() throws RpcException, IllegalAccessException, InstantiationException {
        final String mainClassName = ReflectUtil.getStackTrace();
        Class<?> startClass = null;
        try{
            startClass=Class.forName(mainClassName);
            if(!startClass.isAnnotationPresent(ServiceScan.class)){
                logger.error("启动类缺少@ServiceScan注解");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String basePackage = startClass.getAnnotation(ServiceScan.class).value();
        if("".equals(basePackage)){
            //默认情况
            basePackage=mainClassName.substring(0,mainClassName.lastIndexOf("."));
        }
        Set<Class<?>> classes = ReflectUtil.getClasses(basePackage);
        for (Class<?> clazz:classes
             ) {
            //找到所有被Service注解了的类，将该类实现的服务接口注册到注册中心，实现自动注册。
            if(clazz.isAnnotationPresent(Service.class)){
                final String[] serviceNames = clazz.getAnnotation(Service.class).name();
                if(serviceNames.length==0){
                    final Class<?>[] interfaces = clazz.getInterfaces();
                    for(Class<?> oneInterface:interfaces){
                        publishService(clazz.newInstance(),oneInterface.getCanonicalName());
                    }
                }else{
                    for(String serviceName:serviceNames) {
                        Object obj;
                        try {
                            obj = clazz.newInstance();
                            publishService(obj,serviceName);
                        } catch (IllegalAccessException | InstantiationException e) {
                            logger.error("创建" + clazz + "有错误发生");
                            continue;
                        }
                    }
                }
            }
        }
    }
}
