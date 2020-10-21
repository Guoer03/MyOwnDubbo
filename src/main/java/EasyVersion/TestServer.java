package EasyVersion;
import Exception.*;
public class TestServer {
    public static void main(String[] args) throws RpcException {
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.register(helloService);
        RpcServer rpcServer = new RpcServer(serviceRegistry);
        rpcServer.start(9002);
    }
}
