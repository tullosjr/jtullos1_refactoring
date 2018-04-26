package main.java.memoranda.ui.htmleditor;

import main.java.memoranda.ui.htmleditor.util.Local;

import javax.swing.*;
import javax.swing.text.Element;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

class InsertTableCellAction extends AbstractAction {
    private HTMLEditor htmlEditor;

    InsertTableCellAction(HTMLEditor htmlEditor) {
        super(Local.getString("Insert table cell"));
        this.htmlEditor = htmlEditor;
        this.putValue(
            Action.ACCELERATOR_KEY,
            KeyStroke.getKeyStroke(
                KeyEvent.VK_ENTER,
                KeyEvent.CTRL_MASK + KeyEvent.SHIFT_MASK));
    }
    public void actionPerformed(ActionEvent e) {
        String tdTag = "<td><p></p></td>";
        Element td =
            htmlEditor.document
                .getParagraphElement(htmlEditor.editor.getCaretPosition())
                .getParentElement();
        try {
            htmlEditor.document.insertAfterEnd(td, tdTag);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean isEnabled() {
        if (htmlEditor.document == null)
            return false;
        return htmlEditor.document
            .getParagraphElement(htmlEditor.editor.getCaretPosition())
            .getParentElement()
            .getName()
            .toUpperCase()
            .equals("TD");
    }

    public void update() {
        this.setEnabled(isEnabled());
    }
}
