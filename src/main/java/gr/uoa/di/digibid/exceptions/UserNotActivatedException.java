package gr.uoa.di.digibid.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by amehrabyan, gpozidis on 29/08/16.
 */
public class UserNotActivatedException extends AuthenticationException {
    public UserNotActivatedException(String msg) {
        super(msg);
    }

    public UserNotActivatedException(String msg, Throwable t) {
        super(msg, t);
    }
}