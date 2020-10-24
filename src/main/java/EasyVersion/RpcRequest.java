package EasyVersion;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Builder
@Setter
@Getter
public class RpcRequest implements Serializable {
    /*
    * 待调用的接口名称
    * */
    private String interfaceName;
    /*
    * 待调用的方法名称
    * */
    private String methodName;
    /*
    * 待调用方法的参数*/
    private Object[] parameters;
    /*
    * 调用方法的参数类型
    * */
    private Class<?>[] paramTypes;

    public RpcRequest() {
    }

    public RpcRequest(String interfaceName, String methodName, Object[] parameters, Class<?>[] paramTypes) {
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.parameters = parameters;
        this.paramTypes = paramTypes;
    }
}
