package ca.fanshawec.capstone.server.exception;

public class DuplicateEntryException extends RuntimeException {
    public DuplicateEntryException(){
        super();
    }
    public DuplicateEntryException(String message){
        super(message);
    }
}
