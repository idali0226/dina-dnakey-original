package se.nrm.dina.dnakey.logic.exception;

/**
 * Inconsistency in the metadata.
 */
public class InvalidMetadataException extends DinaException {

    public InvalidMetadataException(String message) {
        super(message);
    }


    public InvalidMetadataException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidMetadataException(Throwable cause) {
        super(cause);
    }
}
