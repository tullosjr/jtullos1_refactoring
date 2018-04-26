package main.java.memoranda.ui.htmleditor;

import main.java.memoranda.ui.htmleditor.util.Local;

import javax.swing.*;
import javax.swing.text.Element;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

class InsertTableRowAction extends AbstractAction {
    private HTMLEditor htmlEditor;

    InsertTableRowAction(HTMLEditor htmlEditor) {
        super(Local.getString("Insert table row"));
        this.htmlEditor = htmlEditor;
        this.putValue(
            Action.ACCELERATOR_KEY,
            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, KeyEvent.CTRL_MASK));
    }
    public void actionPerformed(ActionEvent e) {
        String trTag = "<tr>";
        Element tr =
            htmlEditor.document
                .getParagraphElement(htmlEditor.editor.getCaretPosition())
                .getParentElement()
                .getParentElement();
        for (int i = 0; i < tr.getElementCount(); i++)
            if (tr.getElement(i).getName().toUpperCase().equals("TD"))
                trTag += "<td><p></p></td>";
        trTag += "</tr>";

        /*
         * HTMLEditorKit.InsertHTMLTextAction hta = new
         * HTMLEditorKit.InsertHTMLTextAction("insertTR",trTag,
         * HTML.Tag.TABLE, HTML.Tag.TR);
         */
        try {
            htmlEditor.document.insertAfterEnd(tr, trTag);
            //editorKit.insertHTML(document, editor.getCaretPosition(),
            // trTag, 3, 0, HTML.Tag.TR);
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
