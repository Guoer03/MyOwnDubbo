package EasyVersion.LoadBalancing;
import com.alibaba.nacos.api.naming.pojo.Instance;
import java.util.List;
public interface LoadBalancer {
    Instance select(List<Instance> instances);
    //接口中的select方法用于从一些列Instance中选择一个
}
