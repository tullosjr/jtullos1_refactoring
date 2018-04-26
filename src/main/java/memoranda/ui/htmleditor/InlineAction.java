package main.java.memoranda.ui.htmleditor;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class InlineAction extends AbstractAction {
    private HTMLEditor htmlEditor;
    int _type;

    public InlineAction(HTMLEditor htmlEditor, int type, String name) {
        super(name);
        this.htmlEditor = htmlEditor;
        _type = type;
    }

    public void actionPerformed(ActionEvent e) {
        htmlEditor.inlineCB.setSelectedIndex(_type);
    }
}
