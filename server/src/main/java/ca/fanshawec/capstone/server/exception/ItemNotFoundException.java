package ca.fanshawec.capstone.server.exception;

public class ItemNotFoundException extends RuntimeException {
    private String value;
    public String getValue(){
        return value;
    }
    public ItemNotFoundException(){
        super();
    }
    public ItemNotFoundException(String message){
        super(message);
    }
    public ItemNotFoundException(Throwable cause){
        super(cause);
    }
    public ItemNotFoundException(String message, Throwable cause){
        super(message, cause);
    }
}
