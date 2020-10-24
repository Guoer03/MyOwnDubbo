package EasyVersion;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.rowset.serial.SerialException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.LinkPermission;

public class KryoSerializer implements CommonSerializer {
    private static final Logger logger= LoggerFactory.getLogger(KryoSerializer.class);

    private static final ThreadLocal<Kryo> kryoThreadLocal=ThreadLocal.withInitial(()->
    {  Kryo kryo=new Kryo();
       kryo.register(RpcResponse.class);
       kryo.register(RpcRequest.class);
       kryo.setReferences(true);
       kryo.setRegistrationRequired(false);
       return kryo;
    });//Kryo可能会有线程安全问题，所以此处使用ThreadLocal来解决。

    @Override
    public byte[] serialize(Object obj) throws SerialException {
        try(ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
            Output output = new Output(byteArrayOutputStream)){
            Kryo kryo=kryoThreadLocal.get();
            kryo.writeObject(output,obj);
            kryoThreadLocal.remove();
            return output.toBytes();
        }catch (Exception e){
            logger.error("序列化时有错误发生:",e);
            throw new SerialException("序列化时有错误发生");
        }
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) throws SerialException {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             Input input = new Input(byteArrayInputStream)) {
            Kryo kryo = kryoThreadLocal.get();
            Object o = kryo.readObject(input, clazz);
            kryoThreadLocal.remove();
            return o;
        } catch (Exception e) {
            logger.error("反序列化时有错误发生:", e);
            throw new SerialException("反序列化时有错误发生");
        }
    }
    @Override
    public int getCode() {
        return SerializerCode.valueOf("KRYO").getCode();
    }
}
