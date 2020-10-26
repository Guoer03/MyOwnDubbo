package EasyVersion.LoadBalancing;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

public class RoundRobinLoadBalancer implements LoadBalancer {
    private int index;
    @Override
    public Instance select(List<Instance> instances) {
        if(index>=instances.size()){
            index%=instances.size();
        }
        return instances.get(index++);
    }
}
