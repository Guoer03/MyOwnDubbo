package EasyVersion;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
@Getter
@Setter
public class HelloObject implements Serializable {
    /*Serializable是一个对象序列化的接口，一个类只有实现了Serializable接口，它的对象才是可序列化的。
    因此如果要序列化某些类的对象，这些类就必须实现Serializable接口。
    而实际上，Serializable是一个空接口，没有什么具体内容，它的目的只是简单的标识一个类的对象可以被序列化。*/
    private Integer id;
    private String message;
    private Integer age;

    public HelloObject() {
    }

    public HelloObject(Integer id, String message) {
        this.id = id;
        this.message = message;
    }
}
