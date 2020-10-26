package EasyVersion.LoadBalancing;
import com.alibaba.nacos.api.naming.pojo.Instance;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
public class ConsistentHashLoadBalancer implements LoadBalancer {
    private static List<String> servers=new ArrayList<>();
    private static SortedMap<Integer, String> sortedMap=new TreeMap<>();
    private String ClientAddr;
    public ConsistentHashLoadBalancer(String clientAddr) {
        this.ClientAddr=clientAddr;
    }
    /**
     35      * 使用FNV1_32_HASH算法计算服务器的Hash值,这里不使用重写hashCode的方法，最终效果没区别
     36      */
     private static int getHash(String str)
     {
                 final int p = 16777619;
                 int hash = (int)2166136261L;
                 for (int i = 0; i < str.length(); i++){
                     hash = (hash ^ str.charAt(i)) * p;
                 }
                 hash += hash << 13;
                 hash ^= hash >> 7;
                 hash += hash << 3;
                 hash ^= hash >> 17;
                 hash += hash << 5;

                 // 如果算出来的值为负数则取其绝对值
                 if (hash < 0){
                     hash = Math.abs(hash);
                 }
                 return hash;
     }

     private static String getServer(String node){
         //得到带路由的节点的hash值
         int hash=getHash(node);
         SortedMap<Integer,String> subMap=sortedMap.tailMap(hash);
         Integer i =subMap.firstKey();
         return subMap.get(i);
     }
    @Override
    public Instance select(List<Instance> instances) {
         for (Instance in:instances
        ) {
            servers.add(in.getIp()+":"+String.valueOf(in.getPort()));
        }
        for (String server:servers
        ) {
            sortedMap.put(getHash(server),server);
        }
        final String SelectedServer = getServer(this.ClientAddr);
        final String[] splitedStrs = SelectedServer.split(":");
        final Instance res = new Instance();
        res.setIp(splitedStrs[0]);
        res.setPort(Integer.parseInt(splitedStrs[1]));
        return res;
     }
}
