package main.java.memoranda.ui.htmleditor;

import main.java.memoranda.ui.htmleditor.util.Local;

import javax.swing.*;
import javax.swing.undo.CannotUndoException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

class UndoAction extends AbstractAction {
    private HTMLEditor htmlEditor;

    public UndoAction(HTMLEditor htmlEditor) {
        super(Local.getString("Undo"));
        this.htmlEditor = htmlEditor;
        setEnabled(false);
        putValue(
            Action.SMALL_ICON,
            new ImageIcon(htmlEditor.cl.getResource("/htmleditor/icons/undo16.png")));
        putValue(
            Action.ACCELERATOR_KEY,
            KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_MASK));
    }

    public void actionPerformed(ActionEvent e) {
        try {
            htmlEditor.undo.undo();
        } catch (CannotUndoException ex) {
            System.out.println("Unable to undo: " + ex);
            ex.printStackTrace();
        }
        update();
        htmlEditor.redoAction.update();
    }

    protected void update() {
        if (htmlEditor.undo.canUndo()) {
            setEnabled(true);
            putValue(
                Action.SHORT_DESCRIPTION,
                htmlEditor.undo.getUndoPresentationName());
        } else {
            setEnabled(false);
            putValue(Action.SHORT_DESCRIPTION, Local.getString("Undo"));
        }
    }
}
