package EasyVersion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultServiceRegistry implements ServiceRegistry {
    private static final Logger logger= LoggerFactory.getLogger(HelloServiceImpl.class);
    private   final Map<String,Object> serviceMap=new ConcurrentHashMap<>();
    //将服务名与提供服务的对象的对应关系保存在一个ConcurrentHashMap来保存当前有哪些服务已经被注册（考虑到并发场景）
    private final Set<String> registeredService=ConcurrentHashMap.newKeySet();
    @Override
    public <T> void register(T service) throws RpcException {
        String serviceName=service.getClass().getCanonicalName();//返回该服务的全路径类名。
        if(registeredService.contains(serviceName)){//如果该服务已经存在，则无需注册
            return;
        }
        final Class<?>[] interfaces = service.getClass().getInterfaces();
        if(interfaces.length==0){
            throw new RpcException(RpcError.SERVICE_NOT_IMPLEMENT_ANY_INTERFACE);
        }
        for(Class<?> i:interfaces){
            serviceMap.put(i.getCanonicalName(),service);//如果一个服务实现类对象实现了多个服务接口，
        }

    }

    @Override
    public Object getService(String serviceName) throws RpcException {
        Object service = serviceMap.get(serviceName);
        if (service == null) {
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        }
        return service;
    }
}
