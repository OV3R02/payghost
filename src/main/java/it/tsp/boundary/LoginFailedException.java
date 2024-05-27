package it.tsp.boundary;

public class LoginFailedException extends RuntimeException {

    public LoginFailedException  (String error){
        super(error);
    }
}
