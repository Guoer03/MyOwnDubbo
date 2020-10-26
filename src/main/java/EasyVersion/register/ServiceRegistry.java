package EasyVersion.register;

import java.net.InetSocketAddress;

public interface ServiceRegistry {
    //register将服务的名称和地址注册进服务注册中心
    void register(String serviceName, InetSocketAddress inetSocketAddress);
    //根据服务名称
    InetSocketAddress lookupService(String serviceName);
    void ServiceLogOut(String serviceName, InetSocketAddress inetSocketAddress);
}
