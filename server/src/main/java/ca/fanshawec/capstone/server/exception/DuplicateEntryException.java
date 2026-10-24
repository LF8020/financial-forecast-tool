package ca.fanshawec.capstone.server.exception;

//DuplicateEntryException is a simple exception that extends RuntimeException
public class DuplicateEntryException extends RuntimeException {
    public DuplicateEntryException(){
        super();
    }
    public DuplicateEntryException(String message){
        super(message);
    }
}
