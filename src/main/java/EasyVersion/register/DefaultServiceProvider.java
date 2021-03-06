package EasyVersion.register;

import EasyVersion.HelloServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import EasyVersion.Exception.RpcException;
import EasyVersion.Exception.RpcError;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultServiceProvider implements ServiceProvider {
    private static final Logger logger= LoggerFactory.getLogger(HelloServiceImpl.class);
    private static final Map<String,Object> serviceMap=new ConcurrentHashMap<>();

    @Override
    public  Set<String> getLocalServiceNames() {
        return serviceMap.keySet();
    }

    //将服务名与提供服务的对象的对应关系保存在一个ConcurrentHashMap来保存当前有哪些服务已经被注册（考虑到并发场景）
    private static Set<String> registeredService;
    @Override
    public <T> void register(T service) throws RpcException {
        String serviceName=service.getClass().getCanonicalName();
        //返回该服务的全路径类名。
        if(registeredService!=null&&registeredService.contains(serviceName)){
            //如果该服务已经存在，则无需注册
            return;
        }
        final Class<?>[] interfaces = service.getClass().getInterfaces();
        if(interfaces.length==0){
            throw new RpcException(RpcError.SERVICE_NOT_IMPLEMENT_ANY_INTERFACE);
        }
        for(Class<?> i:interfaces){
            serviceMap.put(i.getCanonicalName(),service);
            registeredService=serviceMap.keySet();
            //如果一个服务实现类对象实现了多个服务接口，
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
