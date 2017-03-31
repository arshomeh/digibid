package gr.uoa.di.digibid.exceptions;

/**
 * Created by amehrabyan, gpozidis on 29/08/16.
 */
public class UserNotFoundException extends IllegalArgumentException {
    public UserNotFoundException(String msg) {
        super(msg);
    }

    public UserNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }
}