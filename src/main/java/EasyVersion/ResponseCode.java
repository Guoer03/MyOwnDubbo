package EasyVersion;

public class ResponseCode {
    public static final Integer SUCCESS=200;
    public static final Integer METHOD_NOT_FOUND=202;
    private Integer statusCode;
    private String message;

    public ResponseCode(Integer statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

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
}
