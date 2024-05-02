package it.tsp.boundary;

public class TransactionException extends RuntimeException {
    public TransactionException(String message){
        super(message);
    }
}
