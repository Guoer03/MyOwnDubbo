package EasyVersion.register;
import EasyVersion.LoadBalancing.LoadBalancer;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.InetSocketAddress;
import java.util.List;
public class NacosServiceRegistry implements ServiceRegistry{
    private static final Logger logger= LoggerFactory.getLogger(NacosServiceRegistry.class);
    private static final String SERVER_ADDR="127.0.0.1:8848";
    private static NamingService namingService;
    private LoadBalancer loadBalancer;
    static {
        try {
            namingService = NamingFactory.createNamingService(SERVER_ADDR);
        } catch (NacosException e) {
            logger.error("连接到Nacos时有错误发生: ", e);
        }
    }

    public NacosServiceRegistry() {
    }

    public NacosServiceRegistry(LoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    @Override
    public void register(String serviceName, InetSocketAddress inetSocketAddress) {
        try {
            namingService.registerInstance(serviceName,inetSocketAddress.getHostName(),inetSocketAddress.getPort());
        } catch (NacosException e) {
            logger.error("注册时有错误发生",e);
        }
    }
    @Override
    public InetSocketAddress lookupService(String serviceName) {
        try {
            //返回某个服务的所有提供者列表。
            final List<Instance> allInstances = namingService.getAllInstances(serviceName);
            final Instance instance = allInstances.get(0);
            return new InetSocketAddress(instance.getIp(),instance.getPort());
        } catch (NacosException e) {
            logger.error("获取服务时有错误发生:",e);
        }
        return null;
    }

    @Override
    public void ServiceLogOut(String serviceName, InetSocketAddress inetSocketAddress) {
        try {
            namingService.deregisterInstance(serviceName, String.valueOf(inetSocketAddress.getAddress()),inetSocketAddress.getPort());
        } catch (NacosException e) {
            logger.error("注销服务{}时出错",serviceName);
        }
    }
}
