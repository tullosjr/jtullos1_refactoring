package main.java.memoranda.ui.htmleditor;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;

class UndoHandler implements UndoableEditListener {

    private HTMLEditor htmlEditor;

    public UndoHandler(HTMLEditor htmlEditor) {
        this.htmlEditor = htmlEditor;
    }

    /**
     * Messaged when the Document has created an edit, the edit is added to
     * <code>undo</code>, an instance of UndoManager.
     */
    public void undoableEditHappened(UndoableEditEvent e) {
        htmlEditor.undo.addEdit(e.getEdit());
        htmlEditor.undoAction.update();
        htmlEditor.redoAction.update();
    }
}
