package main.java.memoranda.ui.htmleditor;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class BlockAction extends AbstractAction {
    private HTMLEditor htmlEditor;
    int _type;

    public BlockAction(HTMLEditor htmlEditor, int type, String name) {
        super(name);
        this.htmlEditor = htmlEditor;
        _type = type;
    }

    public void actionPerformed(ActionEvent e) {
        htmlEditor.blockCB.setSelectedIndex(_type);
    }
}
