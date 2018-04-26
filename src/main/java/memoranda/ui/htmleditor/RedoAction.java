package main.java.memoranda.ui.htmleditor;

import main.java.memoranda.ui.htmleditor.util.Local;

import javax.swing.*;
import javax.swing.undo.CannotRedoException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

class RedoAction extends AbstractAction {
    private HTMLEditor htmlEditor;

    public RedoAction(HTMLEditor htmlEditor) {
        super(Local.getString("Redo"));
        this.htmlEditor = htmlEditor;
        setEnabled(false);
        putValue(
            Action.SMALL_ICON,
            new ImageIcon(htmlEditor.cl.getResource("/htmleditor/icons/redo16.png")));
        putValue(
            Action.ACCELERATOR_KEY,
            KeyStroke.getKeyStroke(
                KeyEvent.VK_Z,
                KeyEvent.CTRL_MASK + KeyEvent.SHIFT_MASK));
    }

    public void actionPerformed(ActionEvent e) {
        try {
            htmlEditor.undo.redo();
        } catch (CannotRedoException ex) {
            System.out.println("Unable to redo: " + ex);
            ex.printStackTrace();
        }
        update();
        htmlEditor.undoAction.update();
    }

    protected void update() {
        if (htmlEditor.undo.canRedo()) {
            setEnabled(true);
            putValue(
                Action.SHORT_DESCRIPTION,
                htmlEditor.undo.getRedoPresentationName());
        } else {
            setEnabled(false);
            putValue(Action.SHORT_DESCRIPTION, Local.getString("Redo"));
        }
    }
}
