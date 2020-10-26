package EasyVersion.Serializers;
import EasyVersion.RpcRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
public class JsonSerializer implements CommonSerializer {
    private static final Logger logger = LoggerFactory.getLogger(JsonSerializer.class);
    private ObjectMapper objectMapper=new ObjectMapper();
    @Override
    public byte[] serialize(Object obj) {
        try{
            return objectMapper.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            logger.error("序列化时有错误发生：{}",e.getMessage());
            return null;
        }
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        try{
            Object obj = objectMapper.readValue(bytes, clazz);
            if(obj instanceof RpcRequest){
                obj=handleRequest(obj);
            }
            return obj;
        } catch (IOException e) {
            logger.error("反序列化时出现错误：",e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /*
    * 由于使用了json序列化和反序列化Object数组，无法保证反序列化后仍然为原实例类型，需要重新判断处理
    * */
    private Object handleRequest(Object obj) throws IOException{
        RpcRequest request=(RpcRequest)obj;
        for(int i=0;i<request.getParamTypes().length;i++){
            Class<?> clazz=request.getParamTypes()[i];
            if(!clazz.isAssignableFrom(request.getParameters()[i].getClass())){
                byte[] bytes = objectMapper.writeValueAsBytes(request.getParameters()[i]);
                request.getParameters()[i]=objectMapper.readValue(bytes,clazz);
            }
        }
        return request;
    }
    @Override
    public int getCode() {
        return SerializerCode.valueOf("JSON").getCode();
    }
}
