package ch.hgdev.toposuite.transfer;

/**
 * Listener for handling dialog events.
 *
 * @author HGdev
 */
public interface ExportDialogListener {
    /**
     * This callback is triggered when the action performed by the dialog
     * succeed.
     *
     * @param message Success message.
     */
    void onExportDialogSuccess(String message);

    /**
     * This callback is triggered when the action performed by the dialog
     * fail.
     *
     * @param message Error message.
     */
    void onExportDialogError(String message);
}