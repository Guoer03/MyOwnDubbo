package EasyVersion.register;
import Exception.*;
public interface ServiceRegistry {
    <T> void register(T service) throws RpcException;//注册服务信息
    Object getService(String serviceName) throws RpcException;//获取服务信息
}
