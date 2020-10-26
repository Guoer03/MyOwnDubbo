package EasyVersion.LoadBalancing;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.net.InetSocketAddress;
import java.util.*;
/*
* 优化后加入虚拟节点的一致性hash算法
* */
public class DefaultConsistentHashLoadBalancer implements LoadBalancer{
    /*
    * 存放真实节点的链表，使用链表是因为对于提供服务的服务器可能会出现宕机关机等问题导致服务被注销，
    * 会有较多的插入和删除操作*/
    private static List<String> Realservers=new LinkedList<>();

    /*
    * 存放所有虚拟节点的map
    * */
    private static SortedMap<Integer, String> sortedMap=new TreeMap<>();
    private String ClientAddr;

    /*
    * 每个真实节点对应的虚拟节点数目
    * */
    private Integer virtualNodeNum;

    public DefaultConsistentHashLoadBalancer(String clientAddr,Integer virtualNodeNum) {
        this.ClientAddr=clientAddr;
        this.virtualNodeNum=virtualNodeNum;
    }
    /*使用FNV1_32_HASH算法计算服务器的Hash值,这里不使用重写hashCode的方法，最终效果没区别*/
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
            Realservers.add(in.getIp()+":"+String.valueOf(in.getPort()));
        }
        for (String server:Realservers
        ) {//根据真实节点生成虚拟节点并加入到红黑树中
            for (int i=0;i<virtualNodeNum;i++){
                server=server+"&VN"+String.valueOf(i);
                sortedMap.put(getHash(server),server);
            }
        }
        final String SelectedServer = getServer(this.ClientAddr);
        final String[] splitedStrs = SelectedServer.split(":");
        final Instance res = new Instance();
        res.setIp(splitedStrs[0]);
        res.setPort(Integer.parseInt(splitedStrs[1].split("&")[0]));
        return res;
    }
}
