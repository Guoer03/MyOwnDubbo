package EasyVersion;

import java.io.Serializable;
public class RpcResponse<T> implements Serializable {
    //响应状态码
    private Integer statusCode;
    //响应状态补充信息
    private String message;
    //响应数据
    private T data;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> RpcResponse<T> success(T data){//该静态方法快速生成成功时的响应对象
        RpcResponse<T> response = new RpcResponse<T>();
        response.setStatusCode(ResponseCode.SUCCESS);
        response.setData(data);
        return response;
    }
    public static <T> RpcResponse<T> fail(ResponseCode responseCode){//该静态方法快速生成失败时的响应对象
        RpcResponse<T> response = new RpcResponse<T>();
        response.setStatusCode(responseCode.getStatusCode());
        response.setMessage(responseCode.getMessage());
        return response;
    }
}
