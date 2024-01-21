package ch.hgdev.toposuite.transfer;

import java.io.Serial;

public class InvalidFormatException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -7698321064626795405L;

    public InvalidFormatException(String message) {
        super(message);
    }
}