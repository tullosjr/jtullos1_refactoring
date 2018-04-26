package main.java.memoranda.ui.htmleditor;

import main.java.memoranda.ui.htmleditor.util.Local;

import javax.swing.*;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.event.ActionEvent;

class BreakAction extends AbstractAction {
    private HTMLEditor htmlEditor;

    BreakAction(HTMLEditor htmlEditor) {
        super(
            Local.getString("Insert break"),
            new ImageIcon(htmlEditor.cl.getResource("/htmleditor/icons/break.png")));
        this.htmlEditor = htmlEditor;
    }
    public void actionPerformed(ActionEvent e) {
        String elName =
            htmlEditor.document
                .getParagraphElement(htmlEditor.editor.getCaretPosition())
                .getName();
        /*
         * if ((elName.toUpperCase().equals("PRE")) ||
         * (elName.toUpperCase().equals("P-IMPLIED"))) {
         * editor.replaceSelection("\r"); return;
         */
        HTML.Tag tag = HTML.getTag(elName);
        if (elName.toUpperCase().equals("P-IMPLIED"))
            tag = HTML.Tag.IMPLIED;

        HTMLEditorKit.InsertHTMLTextAction hta =
            new HTMLEditorKit.InsertHTMLTextAction(
                "insertBR",
                "<br>",
                tag,
                HTML.Tag.BR);
        hta.actionPerformed(e);

        //insertHTML("<br>",editor.getCaretPosition());

    }
}
