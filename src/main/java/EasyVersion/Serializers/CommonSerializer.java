package EasyVersion.Serializers;

import javax.sql.rowset.serial.SerialException;

public interface CommonSerializer {
    byte[] serialize(Object obj) throws SerialException;
    Object deserialize(byte[] bytes,Class<?> clazz) throws SerialException;
    int getCode();
    static CommonSerializer getByCode(int code){
        if(code==1){
            return new JsonSerializer();
        }else if(code==0){
            return new KryoSerializer();
        }
        return null;
    }
}
