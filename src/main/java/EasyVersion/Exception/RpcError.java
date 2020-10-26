package EasyVersion.Exception;

public class RpcError {
    public static final String SERVICE_NOT_IMPLEMENT_ANY_INTERFACE="服务并未实现任何接口";
    public static final String SERVICE_NOT_FOUND="服务未找到";
    public static final String UNKNOWN_PROTOCOL="未知的协议包";
    public static final String UNKNOWN_PACKAGE_TYPE="无法判断是request还是response";
    public static final String UNKNOWN_SERIALIZER="无法识别的反序列化器";
    public static final String FAILED_TO_CONNECT_TO_SERVICE_REGISTRY="无法连接到注册中心";
    public static final String SERIALIZER_NOT_FOUND="未找到序列化器";
}
