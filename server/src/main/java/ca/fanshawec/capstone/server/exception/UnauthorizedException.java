package ca.fanshawec.capstone.server.exception;

//LF: TODO: Deprecated at the moment. Will add back once advisor handling and scenario locking is implemented.
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(){
        super();
    }
    public UnauthorizedException(String message){
        super(message);
    }
    public UnauthorizedException(Throwable cause){
        super(cause);
    }
    public UnauthorizedException(String message, Throwable cause){
        super(message, cause);
    }
}