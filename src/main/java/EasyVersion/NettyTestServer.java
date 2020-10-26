package EasyVersion;
import EasyVersion.LoadBalancing.DefaultConsistentHashLoadBalancer;
import EasyVersion.Serializers.KryoSerializer;
import EasyVersion.Server.AutoService.ServiceScan;
import EasyVersion.Server.NettyServer;
import EasyVersion.register.DefaultServiceProvider;
import EasyVersion.register.NacosServiceRegistry;
import EasyVersion.Exception.*;
import EasyVersion.register.ServiceRegistry;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
@ServiceScan
public class NettyTestServer {
    public static void main(String[] args) throws RpcException, UnknownHostException, InstantiationException, IllegalAccessException {
        final NettyServer server = new NettyServer("127.0.0.1", 9019, new DefaultServiceProvider(), new NacosServiceRegistry(), new KryoSerializer());
        server.start();
    }
}
