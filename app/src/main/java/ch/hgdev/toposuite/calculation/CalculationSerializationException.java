package ch.hgdev.toposuite.calculation;

/**
 * Custom exception to be used when a calculation cannot be deserialized correctly.
 * Useful for instance if one of the required input for a calculation is invalid.
 * 
 * @author HGdev
 * 
 */
public class CalculationSerializationException extends Exception {

    private static final long serialVersionUID = -1483789450547225726L;

    public CalculationSerializationException() {
        // nothing to be done actually
    }

    public CalculationSerializationException(String message) {
        super(message);
    }

}
