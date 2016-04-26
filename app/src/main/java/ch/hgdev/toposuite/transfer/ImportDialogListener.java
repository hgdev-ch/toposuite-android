package ch.hgdev.toposuite.transfer;

/**
 * Listener for handling dialog events.
 *
 * @author HGdev
 */
public interface ImportDialogListener {
    /**
     * This callback is triggered when the action performed by the dialog
     * succeed.
     *
     * @param message Success message.
     */
    void onImportDialogSuccess(String message);

    /**
     * This callback is triggered when the action performed by the dialog
     * fail.
     *
     * @param message Error message.
     */
    void onImportDialogError(String message);
}
