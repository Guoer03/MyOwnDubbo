package EasyVersion;
import EasyVersion.register.DefaultServiceProvider;
import EasyVersion.register.ServiceProvider;
import EasyVersion.Exception.*;
public class TestServer {
    public static void main(String[] args) throws RpcException {
        HelloService helloService = new HelloServiceImpl();
        ServiceProvider serviceProvider = new DefaultServiceProvider();
        serviceProvider.register(helloService);
        SocketServer socketServer = new SocketServer(serviceProvider);
        socketServer.start(9002);
    }
}
