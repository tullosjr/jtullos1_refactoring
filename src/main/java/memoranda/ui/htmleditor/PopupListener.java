package main.java.memoranda.ui.htmleditor;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class PopupListener extends MouseAdapter {
    private HTMLEditor htmlEditor;

    public PopupListener(HTMLEditor htmlEditor) {
        this.htmlEditor = htmlEditor;
    }

    public void mousePressed(MouseEvent e) {
        maybeShowPopup(e);
    }

    public void mouseReleased(MouseEvent e) {
        maybeShowPopup(e);
    }

    private void maybeShowPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            JPopupMenu popupMenu = new JPopupMenu();
            popupMenu.setFocusable(false);

            popupMenu.add(htmlEditor.jMenuItemUndo);
            popupMenu.add(htmlEditor.jMenuItemRedo);
            popupMenu.addSeparator();
            popupMenu.add(htmlEditor.jMenuItemCut);
            popupMenu.add(htmlEditor.jMenuItemCopy);
            popupMenu.add(htmlEditor.jMenuItemPaste);
            popupMenu.addSeparator();
            if (htmlEditor.jMenuItemInsCell.getAction().isEnabled()) {
                popupMenu.add(htmlEditor.jMenuItemInsCell);
                htmlEditor.jMenuItemInsCell.setEnabled(true);
                popupMenu.add(htmlEditor.jMenuItemInsRow);
                htmlEditor.jMenuItemInsRow.setEnabled(true);
                popupMenu.addSeparator();
            }
            popupMenu.add(htmlEditor.jMenuItemProp);
            popupMenu.show(e.getComponent(), e.getX(), e.getY());

        }
    }
}
