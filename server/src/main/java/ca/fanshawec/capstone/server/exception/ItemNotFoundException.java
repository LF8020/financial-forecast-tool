package ca.fanshawec.capstone.server.exception;

//ItemNotFoundException is a simple exception that extends RuntimeException
public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(){
        super();
    }
    public ItemNotFoundException(String message){
        super(message);
    }
}
