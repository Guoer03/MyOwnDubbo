package EasyVersion.Client;

import EasyVersion.Serializers.CommonSerializer;
import EasyVersion.register.ServiceProvider;
import EasyVersion.register.ServiceRegistry;

public abstract class ComonDubboServer {
    protected String host;
    protected int port;
    protected ServiceRegistry serviceRegistry;
    protected CommonSerializer serializer;
    protected ServiceProvider serviceProvider;
}
