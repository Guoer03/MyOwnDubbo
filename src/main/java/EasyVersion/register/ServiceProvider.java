package EasyVersion.register;
import EasyVersion.Exception.*;

import java.util.Set;

//本地注册表类
public interface ServiceProvider {
    <T> void register(T service) throws RpcException;//注册服务信息
    Object getService(String serviceName) throws RpcException;//获取服务信息
    Set<String> getLocalServiceNames();//获得所有服务的名称
}
