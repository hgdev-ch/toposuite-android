package ch.hgdev.toposuite.export;

public class InvalidFormatException extends RuntimeException {
    private static final long serialVersionUID = -7698321064626795405L;

    public InvalidFormatException(String message) {
        super(message);
    }
}