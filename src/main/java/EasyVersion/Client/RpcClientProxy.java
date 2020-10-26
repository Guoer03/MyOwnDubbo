package EasyVersion.Client;

import EasyVersion.RpcRequest;
import EasyVersion.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class RpcClientProxy implements InvocationHandler {
    private String host;
    private int port;
    private CommonDubboClient client;
    public RpcClientProxy(CommonDubboClient client) {
        this.client=client;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest request = RpcRequest.builder().interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameters(args)
                .paramTypes(method.getParameterTypes())
                .build();
        return ((RpcResponse)client.sendRequest(request)).getData();
    }

    public RpcClientProxy(String host, int port) {
        this.host = host;
        this.port = port;
    }
    public <T> T getProxy(Class<T> clazz){
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),new Class<?>[]{clazz},this);
    }
}
