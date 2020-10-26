package EasyVersion.Client;

import EasyVersion.RpcRequest;
import EasyVersion.Serializers.CommonSerializer;
import EasyVersion.register.ServiceRegistry;

public abstract class CommonDubboClient {
    protected ServiceRegistry serviceRegistry;
    protected CommonSerializer serializer;

    public CommonDubboClient(ServiceRegistry serviceRegistry, CommonSerializer serializer) {
        this.serviceRegistry = serviceRegistry;
        this.serializer = serializer;
    }
    abstract Object sendRequest(RpcRequest rpcRequest);
}
