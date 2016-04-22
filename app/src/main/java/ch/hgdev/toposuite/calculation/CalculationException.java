package ch.hgdev.toposuite.calculation;

/**
 * Custom exception to be used when a calculation is impossible.
 * 
 * @author HGdev
 * 
 */
public class CalculationException extends Exception {

    private static final long serialVersionUID = -1483789450547225725L;

    public CalculationException(String message) {
        super(message);
    }

}
