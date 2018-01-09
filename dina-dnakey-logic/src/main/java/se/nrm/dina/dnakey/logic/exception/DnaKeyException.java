package se.nrm.dina.dnakey.logic.exception;

/**
 *
 * @author idali
 */
public class DnaKeyException extends DinaException {
    public DnaKeyException() {
    }

    public DnaKeyException(String s) {
        super(s);
    }

    public DnaKeyException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public DnaKeyException(Throwable throwable) {
        super(throwable);
    }
}
