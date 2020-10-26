package EasyVersion.Server;

import EasyVersion.Exception.RpcException;
import EasyVersion.Serializers.CommonSerializer;
import EasyVersion.register.DefaultServiceProvider;
import EasyVersion.register.ServiceProvider;
import EasyVersion.register.ServiceRegistry;

import java.net.InetSocketAddress;
import java.util.Iterator;

public abstract class CommonDubboServer {
    protected String host;
    protected int port;

    public CommonDubboServer(String host, int port, ServiceRegistry serviceRegistry, CommonSerializer serializer, ServiceProvider serviceProvider) {
        this.host = host;
        this.port = port;
        this.serviceRegistry = serviceRegistry;
        this.serializer = serializer;
        this.serviceProvider = serviceProvider;
    }
    protected ServiceRegistry serviceRegistry;
    protected CommonSerializer serializer;
    protected ServiceProvider serviceProvider;
    public abstract void start();
    public abstract <T> CommonDubboServer publishService(Object service, Class<T> serviceClass) throws RpcException;
    public void clearRegistry(){

        for (String serviceName:
             serviceProvider.getLocalServiceNames()) {
            serviceRegistry.ServiceLogOut(serviceName,new InetSocketAddress(host,port));
        }
    }
}
