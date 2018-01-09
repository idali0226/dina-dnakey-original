package se.nrm.dina.dnakey.logic.exception;

import javax.ejb.ApplicationException;

/**
 * This is the base class for all custom specific exceptions in Dina.
 * Note that it extends {@link RuntimeException}
 * 
 * @author idali
 */
@ApplicationException
public class DinaException extends RuntimeException {
    
    
    public DinaException() {
    }

    public DinaException(String s) {
        super(s);
    }

    public DinaException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public DinaException(Throwable throwable) {
        super(throwable);
    }
    
}
