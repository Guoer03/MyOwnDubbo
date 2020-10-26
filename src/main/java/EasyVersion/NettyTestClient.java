package EasyVersion;

import EasyVersion.Client.CommonDubboClient;
import EasyVersion.Client.NettyClient;
import EasyVersion.Client.RpcClientProxy;
import EasyVersion.LoadBalancing.DefaultConsistentHashLoadBalancer;
import EasyVersion.Serializers.KryoSerializer;
import EasyVersion.register.NacosServiceRegistry;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NettyTestClient {
    public static void main(String[] args) throws UnknownHostException {
        CommonDubboClient client = new NettyClient(new NacosServiceRegistry(new DefaultConsistentHashLoadBalancer(InetAddress.getLocalHost().getHostAddress(),5)),new KryoSerializer());
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object);
        System.out.println(res);
    }
}
