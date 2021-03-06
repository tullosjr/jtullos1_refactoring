package main.java.memoranda.ui.htmleditor;

import main.java.memoranda.ui.htmleditor.util.Local;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.*;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;


//TASK 2-1 SMELL WITHIN A CLASS
public class HTMLEditor extends JPanel {
    public HTMLEditorPane editor = new HTMLEditorPane("");


    JScrollPane jScrollPane1 = new JScrollPane();
    public HTMLEditorKit editorKit = new HTMLEditorKit();
    public HTMLDocument document = null;

    boolean bold = false;
    boolean italic = false;
    boolean under = false;
    boolean list = false;

    String currentTagName = "BODY";
    Element currentParaElement = null;

    Border border1, border2;

    Class cl = main.java.memoranda.ui.htmleditor.HTMLEditor.class;

    String imagesDir = null;
    String imagesPath = null;

    public void setImagesDir(String path) {
        imagesDir = path;
    }

    public String getImagesDir() {
        return imagesDir;
    }

    abstract class HTMLEditorAction extends AbstractAction {
        HTMLEditorAction(String name, ImageIcon icon) {
            super(name, icon);
            super.putValue(Action.SHORT_DESCRIPTION, name);
        }

        HTMLEditorAction(String name) {
            super(name);
            super.putValue(Action.SHORT_DESCRIPTION, name);
        }
    }

    public Action boldAction =
            new HTMLEditorAction(
                    Local.getString("Bold"),
                    new ImageIcon(cl.getResource("/htmleditor/icons/bold.png"))) {
                public void actionPerformed(ActionEvent e) {
                    boldActionB_actionPerformed(e);
                }
            };

    public Action italicAction =
            new HTMLEditorAction(
                    Local.getString("Italic"),
                    new ImageIcon(cl.getResource("/htmleditor/icons/italic.png"))) {
                public void actionPerformed(ActionEvent e) {
                    italicActionB_actionPerformed(e);
                }
            };

    public Action underAction =
            new HTMLEditorAction(
                    Local.getString("Underline"),
                    new ImageIcon(cl.getResource("/htmleditor/icons/underline.png"))) {
                public void actionPerformed(ActionEvent e) {
                    underActionB_actionPerformed(e);
                }
            };

    public Action ulAction =
            new HTMLEditorAction(
                    Local.getString("Unordered list"),
                    new ImageIcon(
                            cl.getResource("/htmleditor/icons/listunordered.png"))) {
                public void actionPerformed(ActionEvent e) {
                    ulActionB_actionPerformed(e);
                }
            };

    public Action olAction =
            new HTMLEditorAction(
                    Local.getString("Ordered list"),
                    new ImageIcon(cl.getResource("/htmleditor/icons/listordered.png"))) {
                public void actionPerformed(ActionEvent e) {
                    olActionB_actionPerformed(e);
                }
            };

    public Action lAlignAction =
            new HTMLEditorAction(
                    Local.getString("Align left"),
                    new ImageIcon(cl.getResource("/htmleditor/icons/alignleft.png"))) {
                public void actionPerformed(ActionEvent e) {
                    lAlignActionB_actionPerformed(e);
                }
            };

    public Action cAlignAction =
            new HTMLEditorAction(
                    Local.getString("Align center"),
                    new ImageIcon(cl.getResource("/htmleditor/icons/aligncenter.png"))) {
                public void actionPerformed(ActionEvent e) {
                    cAlignActionB_actionPerformed(e);
                }
            };

    public Action rAlignAction =
            new HTMLEditorAction(
                    Local.getString("Align right"),
                    new ImageIcon(cl.getResource("/htmleditor/icons/alignright.png"))) {
                public void actionPerformed(ActionEvent e) {
                    rAlignActionB_actionPerformed(e);
                }
            };

    public Action imageAction =
            new HTMLEditorAction(
                    Local.getString("Insert image"),
                    new ImageIcon(cl.getResource("/htmleditor/icons/image.png"))) {
                public void actionPerformed(ActionEvent e) {
                    imageActionB_actionPerformed(e);
                }
            };

    public Action tableAction =
            new HTMLEditorAction(
                    Local.getString("Insert table"),
                    new ImageIcon(cl.getResource("/htmleditor/icons/table.png"))) {
                public void actionPerformed(ActionEvent e) {
                    tableActionB_actionPerformed(e);
                }
            };

    public Action linkAction =
            new HTMLEditorAction(
                    Local.getString("Insert hyperlink"),
                    new ImageIcon(cl.getResource("/htmleditor/icons/link.png"))) {
                public void actionPerformed(ActionEvent e) {
                    linkActionB_actionPerformed(e);
                }
            };

    public Action propsAction =
            new HTMLEditorAction(
                    Local.getString("Object properties"),
                    new ImageIcon(cl.getResource("/htmleditor/icons/properties.png"))) {
                public void actionPerformed(ActionEvent e) {
                    propsActionB_actionPerformed(e);
                }
            };

    public Action selectAllAction =
            new HTMLEditorAction(Local.getString("Select all")) {
                public void actionPerformed(ActionEvent e) {
                    editor.selectAll();
                }
            };

    public Action insertHRAction =
            new HTMLEditorAction(
                    Local.getString("Insert horizontal rule"),
                    new ImageIcon(cl.getResource("/htmleditor/icons/hr.png"))) {
                public void actionPerformed(ActionEvent e) {
                    try {
                        editorKit.insertHTML(
                                document,
                                editor.getCaretPosition(),
                                "<hr>",
                                0,
                                0,
                                HTML.Tag.HR);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }
            };

    CharTablePanel charTablePanel = new CharTablePanel(editor);

    boolean charTableShow = false;

    public JTabbedPane toolsPanel = new JTabbedPane();
    public boolean toolsPanelShow = false;

    public void showToolsPanel() {
        if (toolsPanelShow)
            return;
        this.add(toolsPanel, BorderLayout.SOUTH);
        toolsPanelShow = true;
    }

    public void hideToolsPanel() {
        if (!toolsPanelShow)
            return;
        this.remove(charTablePanel);
        toolsPanelShow = false;
    }

    void addCharTablePanel() {
        showToolsPanel();
        toolsPanel.addTab(Local.getString("Characters"), charTablePanel);
    }

    void removeCharTablePanel() {
        toolsPanel.remove(charTablePanel);
        if (toolsPanel.getTabCount() == 0)
            hideToolsPanel();
    }

    public Action insCharAction =
            new HTMLEditorAction(
                    Local.getString("Insert character"),
                    new ImageIcon(cl.getResource("/htmleditor/icons/char.png"))) {
                public void actionPerformed(ActionEvent e) {
                    if (!charTableShow) {
                        addCharTablePanel();
                        charTableShow = true;
                        insCharActionB.setBorder(border2);
                    } else {
                        removeCharTablePanel();
                        charTableShow = false;
                        insCharActionB.setBorder(border1);
                    }
                    insCharActionB.setBorderPainted(charTableShow);
                }
            };

    public Action findAction =
            new HTMLEditorAction(
                    Local.getString("Find & Replace"),
                    new ImageIcon(cl.getResource("/htmleditor/icons/find.png"))) {
                public void actionPerformed(ActionEvent e) {
                    doFind();
                }
            };

    public InsertTableCellAction insertTableCellAction =
            new InsertTableCellAction(this);
    public InsertTableRowAction insertTableRowAction =
            new InsertTableRowAction(this);
    public BreakAction breakAction = new BreakAction(this);

    public Action cutAction = new HTMLEditorKit.CutAction();

    public Action styleCopyAction = new HTMLEditorKit.CopyAction();

    public Action copyAction = styleCopyAction;

    public Action stylePasteAction = new HTMLEditorKit.PasteAction();

    public Action pasteAction = //new HTMLEditorKit.PasteAction();

            new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    doPaste();
                }
            };

    private void doCopy() {
        Element el = document.getParagraphElement(editor.getSelectionStart());
        if (el.getName().toUpperCase().equals("P-IMPLIED"))
            el = el.getParentElement();
        String elName = el.getName();
        StringWriter sw = new StringWriter();
        String copy;
        java.awt.datatransfer.Clipboard clip =
                java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
        try {
            editorKit.write(
                    sw,
                    document,
                    editor.getSelectionStart(),
                    editor.getSelectionEnd() - editor.getSelectionStart());
            copy = sw.toString();
            copy = copy.split("<" + elName + "(.*?)>")[1];
            copy = copy.split("</" + elName + ">")[0];
            clip.setContents(
                    new java.awt.datatransfer.StringSelection(copy.trim()),
                    null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void doPaste() {
        Clipboard clip =
                java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
        try {
            Transferable content = clip.getContents(this);
            if (content == null)
                return;
            String txt =
                    content
                            .getTransferData(new DataFlavor(String.class, "String"))
                            .toString();
            document.replace(
                    editor.getSelectionStart(),
                    editor.getSelectionEnd() - editor.getSelectionStart(),
                    txt,
                    editorKit.getInputAttributes());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Action zoomInAction = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            doZoom(true);
        }
    };

    public Action zoomOutAction = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            doZoom(false);
        }
    };

    /**
     * Listener for the edits on the current document.
     */
    protected UndoableEditListener undoHandler = new UndoHandler(this);

    /**
     * UndoManager that we add edits to.
     */
    protected UndoManager undo = new UndoManager();

    public UndoAction undoAction = new UndoAction(this);
    public RedoAction redoAction = new RedoAction(this);

    JButton jAlignActionB = new JButton();
    public JToolBar editToolbar = new JToolBar();
    JButton lAlignActionB = new JButton();
    JButton olActionB = new JButton();
    JButton linkActionB = new JButton();
    JButton italicActionB = new JButton();
    JButton propsActionB = new JButton();
    JButton imageActionB = new JButton();

    public final int T_P = 0;
    public final int T_H1 = 1;
    public final int T_H2 = 2;
    public final int T_H3 = 3;
    public final int T_H4 = 4;
    public final int T_H5 = 5;
    public final int T_H6 = 6;
    public final int T_PRE = 7;
    //private final int T_ADDRESS = 8;
    public final int T_BLOCKQ = 8; //9;

    String[] elementTypes =
            {
                    Local.getString("Paragraph"),
                    Local.getString("Header") + " 1",
                    Local.getString("Header") + " 2",
                    Local.getString("Header") + " 3",
                    Local.getString("Header") + " 4",
                    Local.getString("Header") + " 5",
                    Local.getString("Header") + " 6",
                    Local.getString("Preformatted"),
                    //"Address",
                    Local.getString("Blockquote")};
    public JComboBox blockCB = new JComboBox(elementTypes);
    boolean blockCBEventsLock = false;

    public final int I_NORMAL = 0;
    public final int I_EM = 1;
    public final int I_STRONG = 2;
    public final int I_CODE = 3;
    public final int I_CITE = 4;
    public final int I_SUPERSCRIPT = 5;
    public final int I_SUBSCRIPT = 6;
    public final int I_CUSTOM = 7;

    String[] inlineTypes =
            {
                    Local.getString("Normal"),
                    Local.getString("Emphasis"),
                    Local.getString("Strong"),
                    Local.getString("Code"),
                    Local.getString("Cite"),
                    Local.getString("Superscript"),
                    Local.getString("Subscript"),
                    Local.getString("Custom style") + "..."};
    public JComboBox inlineCB = new JComboBox(inlineTypes);
    boolean inlineCBEventsLock = false;

    JButton boldActionB = new JButton();
    JButton ulActionB = new JButton();
    JButton rAlignActionB = new JButton();
    JButton tableActionB = new JButton();
    JButton cAlignActionB = new JButton();
    JButton underActionB = new JButton();
    BorderLayout borderLayout1 = new BorderLayout();
    JPopupMenu defaultPopupMenu = new JPopupMenu();
    //JPopupMenu tablePopupMenu = new JPopupMenu();

    JMenuItem jMenuItemUndo = new JMenuItem(undoAction);
    JMenuItem jMenuItemRedo = new JMenuItem(redoAction);

    JMenuItem jMenuItemCut = new JMenuItem(cutAction);
    JMenuItem jMenuItemCopy = new JMenuItem(copyAction);
    JMenuItem jMenuItemPaste = new JMenuItem(pasteAction);
    JMenuItem jMenuItemProp = new JMenuItem(propsAction);

    JMenuItem jMenuItemInsCell = new JMenuItem(insertTableCellAction);
    JMenuItem jMenuItemInsRow = new JMenuItem(insertTableRowAction);

    int currentCaret = 0;

    int currentFontSize = 4;
    JButton brActionB = new JButton();
    JButton hrActionB = new JButton();
    JButton insCharActionB = new JButton();

    public HTMLEditor() {
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void jbInit() throws Exception {

        cutAction.putValue(
                Action.SMALL_ICON,
                new ImageIcon(cl.getResource("/htmleditor/icons/cut.png")));
        cutAction.putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_MASK));
        cutAction.putValue(Action.NAME, Local.getString("Cut"));
        cutAction.putValue(Action.SHORT_DESCRIPTION, Local.getString("Cut"));

        copyAction.putValue(
                Action.SMALL_ICON,
                new ImageIcon(cl.getResource("/htmleditor/icons/copy.png")));
        copyAction.putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK));
        copyAction.putValue(Action.NAME, Local.getString("Copy"));
        copyAction.putValue(Action.SHORT_DESCRIPTION, Local.getString("Copy"));

        pasteAction.putValue(
                Action.SMALL_ICON,
                new ImageIcon(cl.getResource("/htmleditor/icons/paste.png")));
        pasteAction.putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_MASK));
        pasteAction.putValue(Action.NAME, Local.getString("Paste"));
        pasteAction.putValue(
                Action.SHORT_DESCRIPTION,
                Local.getString("Paste"));

        stylePasteAction.putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke(
                        KeyEvent.VK_V,
                        KeyEvent.CTRL_MASK + KeyEvent.SHIFT_MASK));
        stylePasteAction.putValue(
                Action.NAME,
                Local.getString("Paste special"));
        stylePasteAction.putValue(
                Action.SHORT_DESCRIPTION,
                Local.getString("Paste special"));

        selectAllAction.putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_MASK));

        boldAction.putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke(KeyEvent.VK_B, KeyEvent.CTRL_MASK));
        italicAction.putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke(KeyEvent.VK_I, KeyEvent.CTRL_MASK));
        underAction.putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke(KeyEvent.VK_U, KeyEvent.CTRL_MASK));
        breakAction.putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, KeyEvent.SHIFT_MASK));
        breakAction.putValue(Action.NAME, Local.getString("Insert Break"));
        breakAction.putValue(
                Action.SHORT_DESCRIPTION,
                Local.getString("Insert Break"));

        findAction.putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_MASK));

        document = (HTMLDocument) editorKit.createDefaultDocument();

        border1 =
                BorderFactory.createEtchedBorder(
                        Color.white,
                        new Color(142, 142, 142));
        border2 =
                BorderFactory.createBevelBorder(
                        BevelBorder.LOWERED,
                        Color.white,
                        Color.white,
                        new Color(142, 142, 142),
                        new Color(99, 99, 99));
        this.setLayout(borderLayout1);

        editor.addCaretListener(new CaretListener() {
            public void caretUpdate(CaretEvent e) {
                editor_caretUpdate(e);
            }
        });

        editor.setEditorKit(editorKit);
        editorKit.setDefaultCursor(new Cursor(Cursor.TEXT_CURSOR));

        editor.setDocument(document);
        document.addUndoableEditListener(undoHandler);

        this.setPreferredSize(new Dimension(520, 57));
        editToolbar.setRequestFocusEnabled(false);
        editToolbar.setToolTipText("");

        boldActionB.setAction(boldAction);
        boldActionB.setBorder(border1);
        boldActionB.setMaximumSize(new Dimension(22, 22));
        boldActionB.setMinimumSize(new Dimension(22, 22));
        boldActionB.setPreferredSize(new Dimension(22, 22));
        boldActionB.setBorderPainted(false);
        boldActionB.setFocusable(false);
        boldActionB.setText("");

        italicActionB.setAction(italicAction);
        italicActionB.setBorder(border1);
        italicActionB.setMaximumSize(new Dimension(22, 22));
        italicActionB.setMinimumSize(new Dimension(22, 22));
        italicActionB.setPreferredSize(new Dimension(22, 22));
        italicActionB.setBorderPainted(false);
        italicActionB.setFocusable(false);
        italicActionB.setText("");

        underActionB.setAction(underAction);
        underActionB.setBorder(border1);
        underActionB.setMaximumSize(new Dimension(22, 22));
        underActionB.setMinimumSize(new Dimension(22, 22));
        underActionB.setPreferredSize(new Dimension(22, 22));
        underActionB.setBorderPainted(false);
        underActionB.setFocusable(false);
        underActionB.setText("");

        lAlignActionB.setAction(lAlignAction);
        lAlignActionB.setMaximumSize(new Dimension(22, 22));
        lAlignActionB.setMinimumSize(new Dimension(22, 22));
        lAlignActionB.setPreferredSize(new Dimension(22, 22));
        lAlignActionB.setBorderPainted(false);
        lAlignActionB.setFocusable(false);
        lAlignActionB.setText("");

        rAlignActionB.setAction(rAlignAction);
        rAlignActionB.setFocusable(false);
        rAlignActionB.setPreferredSize(new Dimension(22, 22));
        rAlignActionB.setBorderPainted(false);
        rAlignActionB.setMinimumSize(new Dimension(22, 22));
        rAlignActionB.setMaximumSize(new Dimension(22, 22));
        rAlignActionB.setText("");

        cAlignActionB.setAction(cAlignAction);
        cAlignActionB.setMaximumSize(new Dimension(22, 22));
        cAlignActionB.setMinimumSize(new Dimension(22, 22));
        cAlignActionB.setPreferredSize(new Dimension(22, 22));
        cAlignActionB.setBorderPainted(false);
        cAlignActionB.setFocusable(false);
        cAlignActionB.setText("");

        ulActionB.setAction(ulAction);
        ulActionB.setMaximumSize(new Dimension(22, 22));
        ulActionB.setMinimumSize(new Dimension(22, 22));
        ulActionB.setPreferredSize(new Dimension(22, 22));
        ulActionB.setBorderPainted(false);
        ulActionB.setFocusable(false);
        ulActionB.setText("");

        olActionB.setAction(olAction);
        olActionB.setMaximumSize(new Dimension(22, 22));
        olActionB.setMinimumSize(new Dimension(22, 22));
        olActionB.setPreferredSize(new Dimension(22, 22));
        olActionB.setBorderPainted(false);
        olActionB.setFocusable(false);
        olActionB.setText("");

        linkActionB.setAction(linkAction);
        linkActionB.setMaximumSize(new Dimension(22, 22));
        linkActionB.setMinimumSize(new Dimension(22, 22));
        linkActionB.setPreferredSize(new Dimension(22, 22));
        linkActionB.setBorderPainted(false);
        linkActionB.setFocusable(false);
        linkActionB.setText("");

        propsActionB.setAction(propsAction);
        propsActionB.setFocusable(false);
        propsActionB.setPreferredSize(new Dimension(22, 22));
        propsActionB.setBorderPainted(false);
        propsActionB.setMinimumSize(new Dimension(22, 22));
        propsActionB.setMaximumSize(new Dimension(22, 22));
        propsActionB.setText("");

        imageActionB.setAction(imageAction);
        imageActionB.setMaximumSize(new Dimension(22, 22));
        imageActionB.setMinimumSize(new Dimension(22, 22));
        imageActionB.setPreferredSize(new Dimension(22, 22));
        imageActionB.setBorderPainted(false);
        imageActionB.setFocusable(false);
        imageActionB.setText("");

        tableActionB.setAction(tableAction);
        tableActionB.setFocusable(false);
        tableActionB.setPreferredSize(new Dimension(22, 22));
        tableActionB.setBorderPainted(false);
        tableActionB.setMinimumSize(new Dimension(22, 22));
        tableActionB.setMaximumSize(new Dimension(22, 22));
        tableActionB.setText("");

        brActionB.setAction(breakAction);
        brActionB.setFocusable(false);
        brActionB.setBorderPainted(false);
        brActionB.setPreferredSize(new Dimension(22, 22));
        brActionB.setMinimumSize(new Dimension(22, 22));
        brActionB.setMaximumSize(new Dimension(22, 22));
        brActionB.setText("");

        hrActionB.setAction(insertHRAction);
        hrActionB.setMaximumSize(new Dimension(22, 22));
        hrActionB.setMinimumSize(new Dimension(22, 22));
        hrActionB.setPreferredSize(new Dimension(22, 22));
        hrActionB.setBorderPainted(false);
        hrActionB.setFocusable(false);
        hrActionB.setText("");

        insCharActionB.setAction(insCharAction);
        insCharActionB.setBorder(border1);
        insCharActionB.setMaximumSize(new Dimension(22, 22));
        insCharActionB.setMinimumSize(new Dimension(22, 22));
        insCharActionB.setPreferredSize(new Dimension(22, 22));
        insCharActionB.setBorderPainted(false);
        insCharActionB.setFocusable(false);
        insCharActionB.setText("");

        blockCB.setBackground(new Color(230, 230, 230));
        blockCB.setMaximumRowCount(12);
        blockCB.setFont(new java.awt.Font("Dialog", 1, 10));
        blockCB.setMaximumSize(new Dimension(120, 22));
        blockCB.setMinimumSize(new Dimension(60, 22));
        blockCB.setPreferredSize(new Dimension(79, 22));
        blockCB.setFocusable(false);
        blockCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                blockCB_actionPerformed(e);
            }
        });

        inlineCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                inlineCB_actionPerformed(e);
            }
        });
        inlineCB.setFocusable(false);
        inlineCB.setPreferredSize(new Dimension(79, 22));
        inlineCB.setMinimumSize(new Dimension(60, 22));
        inlineCB.setMaximumSize(new Dimension(120, 22));
        inlineCB.setFont(new java.awt.Font("Dialog", 1, 10));
        inlineCB.setMaximumRowCount(12);
        inlineCB.setBackground(new Color(230, 230, 230));

        this.add(jScrollPane1, BorderLayout.CENTER);
        this.add(editToolbar, BorderLayout.NORTH);

        editToolbar.add(propsActionB, null);
        editToolbar.addSeparator();
        editToolbar.add(blockCB, null);

        editToolbar.addSeparator();
        editToolbar.add(inlineCB, null);
        editToolbar.addSeparator();
        editToolbar.add(boldActionB, null);
        editToolbar.add(italicActionB, null);
        editToolbar.add(underActionB, null);
        editToolbar.addSeparator();
        editToolbar.add(ulActionB, null);
        editToolbar.add(olActionB, null);
        editToolbar.addSeparator();
        editToolbar.add(lAlignActionB, null);
        editToolbar.add(cAlignActionB, null);
        editToolbar.add(rAlignActionB, null);
        editToolbar.addSeparator();
        editToolbar.add(imageActionB, null);
        editToolbar.add(tableActionB, null);
        editToolbar.add(linkActionB, null);
        editToolbar.addSeparator();
        editToolbar.add(hrActionB, null);
        editToolbar.add(brActionB, null);
        editToolbar.add(insCharActionB, null);

        jScrollPane1.getViewport().add(editor, null);

        toolsPanel.setTabPlacement(JTabbedPane.BOTTOM);
        toolsPanel.setFont(new Font("Dialog", 1, 10));

        editor.getKeymap().removeKeyStrokeBinding(
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
        editor.getKeymap().addActionForKeyStroke(
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                new ParaBreakAction(this));

        editor.getKeymap().removeKeyStrokeBinding(
                KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK));
        editor.getKeymap().removeKeyStrokeBinding(
                KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_MASK));
        editor.getKeymap().removeKeyStrokeBinding(
                KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_MASK));

        editor.getKeymap().addActionForKeyStroke(
                KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK),
                copyAction);
        editor.getKeymap().addActionForKeyStroke(
                KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_MASK),
                pasteAction);
        editor.getKeymap().addActionForKeyStroke(
                KeyStroke.getKeyStroke(
                        KeyEvent.VK_V,
                        KeyEvent.CTRL_MASK + KeyEvent.SHIFT_MASK),
                stylePasteAction);
        editor.getKeymap().addActionForKeyStroke(
                KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_MASK),
                cutAction);

        editor.getKeymap().addActionForKeyStroke(
                KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_MASK),
                findAction);

        editor.addMouseListener(new PopupListener(this));

        document.getStyleSheet().setBaseFontSize(currentFontSize);
        this.requestFocusInWindow();
    }

    /**
     * Resets the undo manager.
     */
    protected void resetUndoManager() {
        undo.discardAllEdits();
        undoAction.update();
        redoAction.update();
    }

    public String getContent() {
        try {
            return editor.getText();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public void boldActionB_actionPerformed(ActionEvent e) {
        if (!bold) {
            boldActionB.setBorder(border2);
        } else {
            boldActionB.setBorder(border1);
        }
        bold = !bold;
        boldActionB.setBorderPainted(bold);
        new StyledEditorKit.BoldAction().actionPerformed(e);
    }

    public void italicActionB_actionPerformed(ActionEvent e) {
        if (!italic) {
            italicActionB.setBorder(border2);
        } else {
            italicActionB.setBorder(border1);
        }
        italic = !italic;
        italicActionB.setBorderPainted(italic);
        new StyledEditorKit.ItalicAction().actionPerformed(e);
    }

    public void underActionB_actionPerformed(ActionEvent e) {
        if (!under) {
            underActionB.setBorder(border2);
        } else {
            underActionB.setBorder(border1);
        }
        under = !under;
        underActionB.setBorderPainted(under);

        new StyledEditorKit.UnderlineAction().actionPerformed(e);
    }

    void editor_caretUpdate(CaretEvent e) {
        currentCaret = e.getDot();

        AttributeSet charattrs = null;
        if (editor.getCaretPosition() > 0)
            try {
                charattrs =
                        document
                                .getCharacterElement(editor.getCaretPosition() - 1)
                                .getAttributes();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        else
            charattrs =
                    document
                            .getCharacterElement(editor.getCaretPosition())
                            .getAttributes();

        if (charattrs
                .containsAttribute(StyleConstants.Bold, new Boolean(true))) {
            boldActionB.setBorder(border2);
            bold = true;
        } else if (bold) {
            boldActionB.setBorder(border1);
            bold = false;
        }
        boldActionB.setBorderPainted(bold);
        if (charattrs
                .containsAttribute(StyleConstants.Italic, new Boolean(true))) {
            italicActionB.setBorder(border2);
            italic = true;
        } else if (italic) {
            italicActionB.setBorder(border1);
            italic = false;
        }
        italicActionB.setBorderPainted(italic);
        if (charattrs
                .containsAttribute(StyleConstants.Underline, new Boolean(true))) {
            underActionB.setBorder(border2);
            under = true;
        } else if (under) {
            underActionB.setBorder(border1);
            under = false;
        }
        underActionB.setBorderPainted(under);
        inlineCBEventsLock = true;
        inlineCB.setEnabled(!charattrs.isDefined(HTML.Tag.A));
        if (charattrs.isDefined(HTML.Tag.EM))
            inlineCB.setSelectedIndex(I_EM);
        else if (charattrs.isDefined(HTML.Tag.STRONG))
            inlineCB.setSelectedIndex(I_STRONG);
        else if (
                (charattrs.isDefined(HTML.Tag.CODE))
                        || (charattrs.isDefined(HTML.Tag.SAMP)))
            inlineCB.setSelectedIndex(I_CODE);
        else if (charattrs.isDefined(HTML.Tag.SUP))
            inlineCB.setSelectedIndex(I_SUPERSCRIPT);
        else if (charattrs.isDefined(HTML.Tag.SUB))
            inlineCB.setSelectedIndex(I_SUBSCRIPT);
        else if (charattrs.isDefined(HTML.Tag.CITE))
            inlineCB.setSelectedIndex(I_CITE);
        else if (charattrs.isDefined(HTML.Tag.FONT))
            inlineCB.setSelectedIndex(I_CUSTOM);
        else
            inlineCB.setSelectedIndex(I_NORMAL);
        inlineCBEventsLock = false;

        Element pEl = document.getParagraphElement(editor.getCaretPosition());
        String pName = pEl.getName().toUpperCase();
        blockCBEventsLock = true;
        if (pName.equals("P-IMPLIED"))
            pName = pEl.getParentElement().getName().toUpperCase();

        if (pName.equals("P"))
            blockCB.setSelectedIndex(T_P);
        else if (pName.equals("H1"))
            blockCB.setSelectedIndex(T_H1);
        else if (pName.equals("H2"))
            blockCB.setSelectedIndex(T_H2);
        else if (pName.equals("H3"))
            blockCB.setSelectedIndex(T_H3);
        else if (pName.equals("H4"))
            blockCB.setSelectedIndex(T_H4);
        else if (pName.equals("H5"))
            blockCB.setSelectedIndex(T_H5);
        else if (pName.equals("H6"))
            blockCB.setSelectedIndex(T_H6);
        else if (pName.equals("PRE"))
            blockCB.setSelectedIndex(T_PRE);
            /*
             * else if (pName.equals("ADDRESS"))
             */
        else if (pName.equals("BLOCKQUOTE"))
            blockCB.setSelectedIndex(T_BLOCKQ);
        blockCBEventsLock = false;
        this.insertTableCellAction.update();
        this.insertTableRowAction.update();
    }

    public void ulActionB_actionPerformed(ActionEvent e) {
        String parentname =
                document
                        .getParagraphElement(editor.getCaretPosition())
                        .getParentElement()
                        .getName();
        HTML.Tag parentTag = HTML.getTag(parentname);
        HTMLEditorKit.InsertHTMLTextAction ulAction =
                new HTMLEditorKit.InsertHTMLTextAction(
                        "insertUL",
                        "<ul><li></li></ul>",
                        parentTag,
                        HTML.Tag.UL);
        ulAction.actionPerformed(e);
        list = true;
    }

    public void olActionB_actionPerformed(ActionEvent e) {
        String parentname =
                document
                        .getParagraphElement(editor.getCaretPosition())
                        .getParentElement()
                        .getName();
        HTML.Tag parentTag = HTML.getTag(parentname);
        HTMLEditorKit.InsertHTMLTextAction olAction =
                new HTMLEditorKit.InsertHTMLTextAction(
                        "insertOL",
                        "<ol><li></li></ol>",
                        parentTag,
                        HTML.Tag.OL);
        olAction.actionPerformed(e);
        list = true;
    }

    void removeIfEmpty(Element elem) {
        if (elem.getEndOffset() - elem.getStartOffset() < 2) {
            try {
                document.remove(elem.getStartOffset(), elem.getEndOffset());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void lAlignActionB_actionPerformed(ActionEvent e) {
        HTMLEditorKit.AlignmentAction aa =
                new HTMLEditorKit.AlignmentAction(
                        "leftAlign",
                        StyleConstants.ALIGN_LEFT);
        aa.actionPerformed(e);
    }

    public void cAlignActionB_actionPerformed(ActionEvent e) {
        HTMLEditorKit.AlignmentAction aa =
                new HTMLEditorKit.AlignmentAction(
                        "centerAlign",
                        StyleConstants.ALIGN_CENTER);
        aa.actionPerformed(e);
    }

    public void rAlignActionB_actionPerformed(ActionEvent e) {
        HTMLEditorKit.AlignmentAction aa =
                new HTMLEditorKit.AlignmentAction(
                        "rightAlign",
                        StyleConstants.ALIGN_RIGHT);
        aa.actionPerformed(e);
    }

    public void jAlignActionB_actionPerformed(ActionEvent e) {
        HTMLEditorKit.AlignmentAction aa =
                new HTMLEditorKit.AlignmentAction(
                        "justifyAlign",
                        StyleConstants.ALIGN_JUSTIFIED);
        aa.actionPerformed(e);
    }

    public void insertHTML(String html, int location) {
        //assumes editor is already set to "text/html" type
        try {
            HTMLEditorKit kit = (HTMLEditorKit) editor.getEditorKit();
            Document doc = editor.getDocument();
            StringReader reader = new StringReader(html);
            kit.read(reader, doc, location);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void imageActionB_actionPerformed(ActionEvent e) {
        ImageDialog dlg = new ImageDialog(null);
        Dimension dlgSize = dlg.getPreferredSize();
        Dimension frmSize = this.getSize();
        Point loc = this.getLocationOnScreen();
        dlg.setLocation(
                (frmSize.width - dlgSize.width) / 2 + loc.x,
                (frmSize.height - dlgSize.height) / 2 + loc.y);
        //dlg.setLocation(imageActionB.getLocationOnScreen());
        dlg.setModal(true);
        dlg.setVisible(true);

        if (!dlg.CANCELLED) {
            String parentname =
                    document
                            .getParagraphElement(editor.getCaretPosition())
                            .getParentElement()
                            .getName();
            //HTML.Tag parentTag = HTML.getTag(parentname);
            String urlString = dlg.fileField.getText();
            String path = urlString;
            if (imagesDir != null) {
                try {
                    URL url = new URL(urlString);
                    if (!url.getProtocol().startsWith("http"))
                        path = imagesDir + "/" + url.getFile();
                } catch (MalformedURLException e1) {
                }
            }
            try {
                String imgTag =
                        "<img src=\""
                                + path
                                + "\" alt=\""
                                + dlg.altField.getText()
                                + "\" ";
                String w = dlg.widthField.getText();
                try {
                    Integer.parseInt(w, 10);
                    imgTag += " width=\"" + w + "\" ";
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                String h = dlg.heightField.getText();
                try {
                    Integer.parseInt(h, 10);
                    imgTag += " height=\"" + h + "\" ";
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                String hs = dlg.hspaceField.getText();
                try {
                    Integer.parseInt(hs, 10);
                    imgTag += " hspace=\"" + hs + "\" ";
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                String vs = dlg.vspaceField.getText();
                try {
                    Integer.parseInt(vs, 10);
                    imgTag += " vspace=\"" + vs + "\" ";
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                String b = dlg.borderField.getText();
                try {
                    Integer.parseInt(b, 10);
                    imgTag += " border=\"" + b + "\" ";
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                if (dlg.alignCB.getSelectedIndex() > 0)
                    imgTag += " align=\""
                            + dlg.alignCB.getSelectedItem()
                            + "\" ";
                imgTag += ">";

                if (dlg.urlField.getText().length() > 0) {
                    imgTag =
                            "<a href=\""
                                    + dlg.urlField.getText()
                                    + "\">"
                                    + imgTag
                                    + "</a>";
                    if (editor.getCaretPosition() == document.getLength())
                        imgTag += "&nbsp;";
                    editorKit.insertHTML(
                            document,
                            editor.getCaretPosition(),
                            imgTag,
                            0,
                            0,
                            HTML.Tag.A);
                } else
                    editorKit.insertHTML(
                            document,
                            editor.getCaretPosition(),
                            imgTag,
                            0,
                            0,
                            HTML.Tag.IMG);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void tableActionB_actionPerformed(ActionEvent e) {
        TableDialog dlg = new TableDialog(null);
        Dimension dlgSize = dlg.getPreferredSize();
        Dimension frmSize = this.getSize();
        Point loc = this.getLocationOnScreen();
        dlg.setLocation(
                (frmSize.width - dlgSize.width) / 2 + loc.x,
                (frmSize.height - dlgSize.height) / 2 + loc.y);

        dlg.setModal(true);
        dlg.setVisible(true);
        if (dlg.CANCELLED)
            return;
        String tableTag = "<table ";
        String w = dlg.widthField.getText().trim();
        if (w.length() > 0)
            tableTag += " width=\"" + w + "\" ";
        String h = dlg.heightField.getText().trim();
        if (h.length() > 0)
            tableTag += " height=\"" + h + "\" ";
        String cp = dlg.cellpadding.getValue().toString();
        try {
            Integer.parseInt(cp, 10);
            tableTag += " cellpadding=\"" + cp + "\" ";
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        String cs = dlg.cellspacing.getValue().toString();
        try {
            Integer.parseInt(cs, 10);
            tableTag += " cellspacing=\"" + cs + "\" ";
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        String b = dlg.border.getValue().toString();
        try {
            Integer.parseInt(b, 10);
            tableTag += " border=\"" + b + "\" ";
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (dlg.alignCB.getSelectedIndex() > 0)
            tableTag += " align=\"" + dlg.alignCB.getSelectedItem() + "\" ";
        if (dlg.vAlignCB.getSelectedIndex() > 0)
            tableTag += " valign=\"" + dlg.vAlignCB.getSelectedItem() + "\" ";
        if (dlg.bgcolorField.getText().length() > 0)
            tableTag += " bgcolor=\"" + dlg.bgcolorField.getText() + "\" ";
        tableTag += ">";
        int cols = 1;
        int rows = 1;
        try {
            cols = ((Integer) dlg.columns.getValue()).intValue();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            rows = ((Integer) dlg.rows.getValue()).intValue();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        for (int r = 0; r < rows; r++) {
            tableTag += "<tr>";
            for (int c = 0; c < cols; c++)
                tableTag += "<td><p></p></td>";
            tableTag += "</tr>";
        }
        tableTag += "</table>";
        String parentname =
                document
                        .getParagraphElement(editor.getCaretPosition())
                        .getParentElement()
                        .getName();
        HTML.Tag parentTag = HTML.getTag(parentname);
        System.out.println(parentTag + ":\n" + tableTag);

        try {
            editorKit.insertHTML(
                    document,
                    editor.getCaretPosition(),
                    tableTag,
                    1,
                    0,
                    HTML.Tag.TABLE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void linkActionB_actionPerformed(ActionEvent e) {
        LinkDialog dlg = new LinkDialog(null);
        Dimension dlgSize = dlg.getPreferredSize();
        Dimension frmSize = this.getSize();
        Point loc = this.getLocationOnScreen();
        dlg.setLocation(
                (frmSize.width - dlgSize.width) / 2 + loc.x,
                (frmSize.height - dlgSize.height) / 2 + loc.y);
        dlg.setModal(true);
        if (editor.getSelectedText() != null)
            dlg.txtDesc.setText(editor.getSelectedText());
        dlg.setVisible(true);
        if (dlg.CANCELLED)
            return;
        String aTag = "<a";
        if (dlg.txtURL.getText().length() > 0)
            aTag += " href=\"" + dlg.txtURL.getText() + "\"";
        if (dlg.txtName.getText().length() > 0)
            aTag += " name=\"" + dlg.txtName.getText() + "\"";
        if (dlg.txtTitle.getText().length() > 0)
            aTag += " title=\"" + dlg.txtTitle.getText() + "\"";
        if (dlg.chkNewWin.isSelected())
            aTag += " target=\"_blank\"";
        aTag += ">" + dlg.txtDesc.getText() + "</a>";
        if (editor.getCaretPosition() == document.getLength())
            aTag += "&nbsp;";
        editor.replaceSelection("");
        try {
            editorKit.insertHTML(
                    document,
                    editor.getCaretPosition(),
                    aTag,
                    0,
                    0,
                    HTML.Tag.A);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void setLinkProperties(
            Element el,
            String href,
            String target,
            String title,
            String name) {
        LinkDialog dlg = new LinkDialog(null);
        dlg.setLocation(linkActionB.getLocationOnScreen());
        dlg.setModal(true);
        dlg.txtURL.setText(href);
        dlg.txtName.setText(name);
        dlg.txtTitle.setText(title);
        try {
            dlg.txtDesc.setText(
                    document.getText(
                            el.getStartOffset(),
                            el.getEndOffset() - el.getStartOffset()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        dlg.chkNewWin.setSelected(target.toUpperCase().equals("_BLANK"));
        dlg.header.setText(Local.getString("Hyperlink properties"));
        dlg.setTitle(Local.getString("Hyperlink properties"));
        dlg.setVisible(true);
        if (dlg.CANCELLED)
            return;
        String aTag = "<a";
        if (dlg.txtURL.getText().length() > 0)
            aTag += " href=\"" + dlg.txtURL.getText() + "\"";
        if (dlg.txtName.getText().length() > 0)
            aTag += " name=\"" + dlg.txtName.getText() + "\"";
        if (dlg.txtTitle.getText().length() > 0)
            aTag += " title=\"" + dlg.txtTitle.getText() + "\"";
        if (dlg.chkNewWin.isSelected())
            aTag += " target=\"_blank\"";
        aTag += ">" + dlg.txtDesc.getText() + "</a>";
        try {
            document.setOuterHTML(el, aTag);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void setImageProperties(
            Element el,
            String src,
            String alt,
            String width,
            String height,
            String hspace,
            String vspace,
            String border,
            String align) {
        ImageDialog dlg = new ImageDialog(null);
        dlg.setLocation(imageActionB.getLocationOnScreen());
        dlg.setModal(true);
        dlg.setTitle(Local.getString("Image properties"));
        dlg.fileField.setText(src);
        dlg.altField.setText(alt);
        dlg.widthField.setText(width);
        dlg.heightField.setText(height);
        dlg.hspaceField.setText(hspace);
        dlg.vspaceField.setText(vspace);
        dlg.borderField.setText(border);
        dlg.alignCB.setSelectedItem(align);
        dlg.updatePreview();
        dlg.setVisible(true);
        if (dlg.CANCELLED)
            return;
        String imgTag =
                "<img src=\""
                        + dlg.fileField.getText()
                        + "\" alt=\""
                        + dlg.altField.getText()
                        + "\" ";
        String w = dlg.widthField.getText();
        try {
            Integer.parseInt(w, 10);
            imgTag += " width=\"" + w + "\" ";
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        String h = dlg.heightField.getText();
        try {
            Integer.parseInt(h, 10);
            imgTag += " height=\"" + h + "\" ";
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        String hs = dlg.hspaceField.getText();
        try {
            Integer.parseInt(hs, 10);
            imgTag += " hspace=\"" + hs + "\" ";
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        String vs = dlg.vspaceField.getText();
        try {
            Integer.parseInt(vs, 10);
            imgTag += " vspace=\"" + vs + "\" ";
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        String b = dlg.borderField.getText();
        try {
            Integer.parseInt(b, 10);
            imgTag += " border=\"" + b + "\" ";
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (dlg.alignCB.getSelectedIndex() > 0)
            imgTag += " align=\"" + dlg.alignCB.getSelectedItem() + "\" ";
        imgTag += ">";
        if (dlg.urlField.getText().length() > 0) {
            imgTag =
                    "<a href=\"" + dlg.urlField.getText() + "\">" + imgTag + "</a>";
            if (editor.getCaretPosition() == document.getLength())
                imgTag += "&nbsp;";
        }
        try {
            document.setOuterHTML(el, imgTag);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void setElementProperties(Element el, String id, String cls, String sty) {
        ElementDialog dlg = new ElementDialog(null);
        Dimension dlgSize = dlg.getPreferredSize();
        Dimension frmSize = this.getSize();
        Point loc = this.getLocationOnScreen();
        dlg.setLocation(
                (frmSize.width - dlgSize.width) / 2 + loc.x,
                (frmSize.height - dlgSize.height) / 2 + loc.y);
        dlg.setModal(true);
        dlg.setTitle(Local.getString("Object properties"));
        dlg.idField.setText(id);
        dlg.classField.setText(cls);
        dlg.styleField.setText(sty);
        dlg.setVisible(true);
        if (dlg.CANCELLED)
            return;
        SimpleAttributeSet attrs = new SimpleAttributeSet(el.getAttributes());
        if (dlg.idField.getText().length() > 0)
            attrs.addAttribute(HTML.Attribute.ID, dlg.idField.getText());
        if (dlg.classField.getText().length() > 0)
            attrs.addAttribute(HTML.Attribute.CLASS, dlg.classField.getText());
        if (dlg.styleField.getText().length() > 0)
            attrs.addAttribute(HTML.Attribute.STYLE, dlg.styleField.getText());
        document.setParagraphAttributes(el.getStartOffset(), 0, attrs, true);
    }

    void setTableProperties(Element td) {
        Element tr = td.getParentElement();
        Element table = tr.getParentElement();

        TdDialog dlg = new TdDialog(null);
        dlg.setLocation(editor.getLocationOnScreen());
        dlg.setModal(true);
        dlg.setTitle(Local.getString("Table properties"));

        /** **********PARSE ELEMENTS*********** */
        // TD***
        AttributeSet tda = td.getAttributes();
        if (tda.isDefined(HTML.Attribute.BGCOLOR)) {
            dlg.tdBgcolorField.setText(
                    tda.getAttribute(HTML.Attribute.BGCOLOR).toString());
            Util.setBgcolorField(dlg.tdBgcolorField);
        }
        if (tda.isDefined(HTML.Attribute.WIDTH))
            dlg.tdWidthField.setText(
                    tda.getAttribute(HTML.Attribute.WIDTH).toString());
        if (tda.isDefined(HTML.Attribute.HEIGHT))
            dlg.tdHeightField.setText(
                    tda.getAttribute(HTML.Attribute.HEIGHT).toString());
        if (tda.isDefined(HTML.Attribute.COLSPAN))
            try {
                Integer i =
                        new Integer(
                                tda.getAttribute(HTML.Attribute.COLSPAN).toString());
                dlg.tdColspan.setValue(i);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        if (tda.isDefined(HTML.Attribute.ROWSPAN))
            try {
                Integer i =
                        new Integer(
                                tda.getAttribute(HTML.Attribute.ROWSPAN).toString());
                dlg.tdRowspan.setValue(i);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        if (tda.isDefined(HTML.Attribute.ALIGN))
            dlg.tdAlignCB.setSelectedItem(
                    tda
                            .getAttribute(HTML.Attribute.ALIGN)
                            .toString()
                            .toLowerCase());
        if (tda.isDefined(HTML.Attribute.VALIGN))
            dlg.tdValignCB.setSelectedItem(
                    tda
                            .getAttribute(HTML.Attribute.VALIGN)
                            .toString()
                            .toLowerCase());
        dlg.tdNowrapChB.setSelected((tda.isDefined(HTML.Attribute.NOWRAP)));

        //TR ****
        AttributeSet tra = tr.getAttributes();
        if (tra.isDefined(HTML.Attribute.BGCOLOR)) {
            dlg.trBgcolorField.setText(
                    tra.getAttribute(HTML.Attribute.BGCOLOR).toString());
            Util.setBgcolorField(dlg.trBgcolorField);
        }
        if (tra.isDefined(HTML.Attribute.ALIGN))
            dlg.trAlignCB.setSelectedItem(
                    tra
                            .getAttribute(HTML.Attribute.ALIGN)
                            .toString()
                            .toLowerCase());
        if (tra.isDefined(HTML.Attribute.VALIGN))
            dlg.trValignCB.setSelectedItem(
                    tra
                            .getAttribute(HTML.Attribute.VALIGN)
                            .toString()
                            .toLowerCase());

        //TABLE ****
        AttributeSet ta = table.getAttributes();
        if (ta.isDefined(HTML.Attribute.BGCOLOR)) {
            dlg.bgcolorField.setText(
                    ta.getAttribute(HTML.Attribute.BGCOLOR).toString());
            Util.setBgcolorField(dlg.bgcolorField);
        }
        if (ta.isDefined(HTML.Attribute.WIDTH))
            dlg.widthField.setText(
                    ta.getAttribute(HTML.Attribute.WIDTH).toString());
        if (ta.isDefined(HTML.Attribute.HEIGHT))
            dlg.heightField.setText(
                    ta.getAttribute(HTML.Attribute.HEIGHT).toString());
        if (ta.isDefined(HTML.Attribute.ALIGN))
            dlg.alignCB.setSelectedItem(
                    ta.getAttribute(HTML.Attribute.ALIGN).toString().toLowerCase());
        if (ta.isDefined(HTML.Attribute.VALIGN))
            dlg.vAlignCB.setSelectedItem(
                    ta
                            .getAttribute(HTML.Attribute.VALIGN)
                            .toString()
                            .toLowerCase());
        if (ta.isDefined(HTML.Attribute.CELLPADDING))
            try {
                Integer i =
                        new Integer(
                                ta.getAttribute(HTML.Attribute.CELLPADDING).toString());
                dlg.cellpadding.setValue(i);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        if (ta.isDefined(HTML.Attribute.CELLSPACING))
            try {
                Integer i =
                        new Integer(
                                ta.getAttribute(HTML.Attribute.CELLSPACING).toString());
                dlg.cellspacing.setValue(i);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        if (ta.isDefined(HTML.Attribute.BORDER))
            try {
                Integer i =
                        new Integer(
                                ta.getAttribute(HTML.Attribute.BORDER).toString());
                dlg.border.setValue(i);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        /** ****************************** */

        dlg.setVisible(true);
        if (dlg.CANCELLED)
            return;

        /** ******** SET ATTRIBUTES ********* */
        // TD***
        String tdTag = "<td";
        if (dlg.tdBgcolorField.getText().length() > 0)
            tdTag += " bgcolor=\"" + dlg.tdBgcolorField.getText() + "\"";

        if (dlg.tdWidthField.getText().length() > 0)
            tdTag += " width=\"" + dlg.tdWidthField.getText() + "\"";

        if (dlg.tdHeightField.getText().length() > 0)
            tdTag += " height=\"" + dlg.tdHeightField.getText() + "\"";

        if (!dlg.tdColspan.getValue().toString().equals("0"))
            tdTag += " colspan=\"" + dlg.tdColspan.getValue().toString() + "\"";

        if (!dlg.tdRowspan.getValue().toString().equals("0"))
            tdTag += " rowspan=\"" + dlg.tdRowspan.getValue().toString() + "\"";

        if (dlg.tdAlignCB.getSelectedItem().toString().length() > 0)
            tdTag += " align=\""
                    + dlg.tdAlignCB.getSelectedItem().toString()
                    + "\"";

        if (dlg.tdValignCB.getSelectedItem().toString().length() > 0)
            tdTag += " valign=\""
                    + dlg.tdValignCB.getSelectedItem().toString()
                    + "\"";

        if (dlg.tdNowrapChB.isSelected())
            tdTag += " nowrap";

        tdTag += ">";

        //TR***
        String trTag = "<tr";
        if (dlg.trBgcolorField.getText().length() > 0)
            trTag += " bgcolor=\"" + dlg.trBgcolorField.getText() + "\"";

        if (dlg.trAlignCB.getSelectedItem().toString().length() > 0)
            trTag += " align=\""
                    + dlg.trAlignCB.getSelectedItem().toString()
                    + "\"";

        if (dlg.trValignCB.getSelectedItem().toString().length() > 0)
            trTag += " valign=\""
                    + dlg.trValignCB.getSelectedItem().toString()
                    + "\"";

        trTag += ">";

        //TABLE ***
        String tTag = "<table";
        if (dlg.bgcolorField.getText().length() > 0)
            tTag += " bgcolor=\"" + dlg.bgcolorField.getText() + "\"";

        if (dlg.widthField.getText().length() > 0)
            tTag += " width=\"" + dlg.widthField.getText() + "\"";

        if (dlg.heightField.getText().length() > 0)
            tTag += " height=\"" + dlg.heightField.getText() + "\"";

        tTag += " cellpadding=\""
                + dlg.cellpadding.getValue().toString()
                + "\"";

        tTag += " cellspacing=\""
                + dlg.cellspacing.getValue().toString()
                + "\"";

        tTag += " border=\"" + dlg.border.getValue().toString() + "\"";

        if (dlg.alignCB.getSelectedItem().toString().length() > 0)
            tTag += " align=\""
                    + dlg.alignCB.getSelectedItem().toString()
                    + "\"";

        if (dlg.vAlignCB.getSelectedItem().toString().length() > 0)
            tTag += " valign=\""
                    + dlg.vAlignCB.getSelectedItem().toString()
                    + "\"";

        tTag += ">";

        try {
            StringWriter sw = new StringWriter();
            String copy;

            editorKit.write(
                    sw,
                    document,
                    td.getStartOffset(),
                    td.getEndOffset() - td.getStartOffset());
            copy = sw.toString();
            copy = copy.split("<td(.*?)>")[1];
            copy = copy.split("</td>")[0];
            document.setOuterHTML(td, tdTag + copy + "</td>");


            sw = new StringWriter();
            editorKit.write(
                    sw,
                    document,
                    tr.getStartOffset(),
                    tr.getEndOffset() - tr.getStartOffset());
            copy = sw.toString();
            copy = copy.split("<tr(.*?)>")[1];
            copy = copy.split("</tr>")[0];
            document.setOuterHTML(tr, trTag + copy + "</tr>");


            sw = new StringWriter();
            editorKit.write(
                    sw,
                    document,
                    table.getStartOffset(),
                    table.getEndOffset() - table.getStartOffset());
            copy = sw.toString();
            copy = copy.split("<table(.*?)>")[1];
            copy = copy.split("</table>")[0];
            document.setOuterHTML(table, tTag + copy + "</table>");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void blockCB_actionPerformed(ActionEvent e) {
        if (blockCBEventsLock)
            return;
        int sel = blockCB.getSelectedIndex();
        HTML.Tag tag = null;

        switch (sel) {
            case T_P:
                tag = HTML.Tag.P;
                break;
            case T_H1:
                tag = HTML.Tag.H1;
                break;
            case T_H2:
                tag = HTML.Tag.H2;
                break;
            case T_H3:
                tag = HTML.Tag.H3;
                break;
            case T_H4:
                tag = HTML.Tag.H4;
                break;
            case T_H5:
                tag = HTML.Tag.H5;
                break;
            case T_H6:
                tag = HTML.Tag.H6;
                break;
            case T_PRE:
                tag = HTML.Tag.PRE;
                break;
            case T_BLOCKQ:
                tag = HTML.Tag.BLOCKQUOTE;
                break;
        }

        Element el = document.getParagraphElement(editor.getCaretPosition());
        if (el.getName().toUpperCase().equals("P-IMPLIED")) {
            Element pEl = el.getParentElement();
            String pElName = pEl.getName();
            String newName = tag.toString();
            StringWriter sw = new StringWriter();
            String copy;
            try {
                editorKit.write(
                        sw,
                        document,
                        el.getStartOffset(),
                        el.getEndOffset() - el.getStartOffset());
                copy = sw.toString();
                copy = copy.split("<" + pElName + "(.*?)>")[1];
                copy = copy.split("</" + pElName + ">")[0];
                document.setOuterHTML(
                        pEl,
                        "<" + newName + ">" + copy + "</" + newName + ">");
                return;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        SimpleAttributeSet attrs = new SimpleAttributeSet(el.getAttributes());
        attrs.addAttribute(StyleConstants.NameAttribute, tag);
        if (editor.getSelectionEnd() - editor.getSelectionStart() > 0)
            document.setParagraphAttributes(
                    editor.getSelectionStart(),
                    editor.getSelectionEnd() - editor.getSelectionStart(),
                    attrs,
                    true);
        else
            document.setParagraphAttributes(
                    editor.getCaretPosition(),
                    0,
                    attrs,
                    true);

    }

    public void propsActionB_actionPerformed(ActionEvent e) {
        AbstractDocument.BranchElement pEl =
                (AbstractDocument.BranchElement) document.getParagraphElement(
                        editor.getCaretPosition());
        System.out.println("--------------");
        System.out.println(
                pEl.getName() + "<-" + pEl.getParentElement().getName());
        Element el = pEl.positionToElement(editor.getCaretPosition());
        System.out.println(
                ":"
                        + el.getAttributes().getAttribute(StyleConstants.NameAttribute));
        AttributeSet attrs = el.getAttributes();
        String elName =
                attrs
                        .getAttribute(StyleConstants.NameAttribute)
                        .toString()
                        .toUpperCase();
        if (elName.equals("IMG")) {
            String src = "",
                    alt = "",
                    width = "",
                    height = "",
                    hspace = "",
                    vspace = "",
                    border = "",
                    align = "";
            if (attrs.isDefined(HTML.Attribute.SRC))
                src = attrs.getAttribute(HTML.Attribute.SRC).toString();
            if (attrs.isDefined(HTML.Attribute.ALT))
                alt = attrs.getAttribute(HTML.Attribute.ALT).toString();
            if (attrs.isDefined(HTML.Attribute.WIDTH))
                width = attrs.getAttribute(HTML.Attribute.WIDTH).toString();
            if (attrs.isDefined(HTML.Attribute.HEIGHT))
                height = attrs.getAttribute(HTML.Attribute.HEIGHT).toString();
            if (attrs.isDefined(HTML.Attribute.HSPACE))
                hspace = attrs.getAttribute(HTML.Attribute.HSPACE).toString();
            if (attrs.isDefined(HTML.Attribute.VSPACE))
                vspace = attrs.getAttribute(HTML.Attribute.VSPACE).toString();
            if (attrs.isDefined(HTML.Attribute.BORDER))
                border = attrs.getAttribute(HTML.Attribute.BORDER).toString();
            if (attrs.isDefined(HTML.Attribute.ALIGN))
                align = attrs.getAttribute(HTML.Attribute.ALIGN).toString();
            setImageProperties(
                    el,
                    src,
                    alt,
                    width,
                    height,
                    hspace,
                    vspace,
                    border,
                    align);
            return;
        }

        Object k = null;
        for (Enumeration en = attrs.getAttributeNames();
             en.hasMoreElements();
                ) {
            k = en.nextElement();
            if (k.toString().equals("a")) {
                String[] param = attrs.getAttribute(k).toString().split(" ");
                String href = "", target = "", title = "", name = "";
                for (int i = 0; i < param.length; i++)
                    if (param[i].startsWith("href="))
                        href = param[i].split("=")[1];
                    else if (param[i].startsWith("title="))
                        title = param[i].split("=")[1];
                    else if (param[i].startsWith("target="))
                        target = param[i].split("=")[1];
                    else if (param[i].startsWith("name="))
                        name = param[i].split("=")[1];
                setLinkProperties(el, href, target, title, name);
                return;
            }
            System.out.println(k + " = '" + attrs.getAttribute(k) + "'");
        }

        if (pEl.getParentElement().getName().toUpperCase().equals("TD")) {
            setTableProperties(pEl.getParentElement());
            return;
        }

        String id = "", cls = "", sty = "";
        AttributeSet pa = pEl.getAttributes();
        if (pa.getAttribute(HTML.Attribute.ID) != null)
            id = pa.getAttribute(HTML.Attribute.ID).toString();
        if (pa.getAttribute(HTML.Attribute.CLASS) != null)
            cls = pa.getAttribute(HTML.Attribute.CLASS).toString();
        if (pa.getAttribute(HTML.Attribute.STYLE) != null)
            sty = pa.getAttribute(HTML.Attribute.STYLE).toString();
        setElementProperties(pEl, id, cls, sty);

    }

    String setFontProperties(Element el, String text) {
        FontDialog dlg = new FontDialog(null);
        Dimension dlgSize = dlg.getSize();
        Dimension frmSize = this.getSize();
        Point loc = this.getLocationOnScreen();
        dlg.setLocation(
                (frmSize.width - dlgSize.width) / 2 + loc.x,
                (frmSize.height - dlgSize.height) / 2 + loc.y);
        dlg.setModal(true);
        AttributeSet ea = el.getAttributes();
        if (ea.isDefined(StyleConstants.FontFamily))
            dlg.fontFamilyCB.setSelectedItem(
                    ea.getAttribute(StyleConstants.FontFamily).toString());
        if (ea.isDefined(HTML.Tag.FONT)) {
            String s = ea.getAttribute(HTML.Tag.FONT).toString();
            String size =
                    s.substring(s.indexOf("size=") + 5, s.indexOf("size=") + 6);
            dlg.fontSizeCB.setSelectedItem(size);
        }
        if (ea.isDefined(StyleConstants.Foreground)) {
            dlg.colorField.setText(
                    Util.encodeColor(
                            (Color) ea.getAttribute(StyleConstants.Foreground)));
            Util.setColorField(dlg.colorField);
            dlg.sample.setForeground(
                    (Color) ea.getAttribute(StyleConstants.Foreground));
        }
        if (text != null)
            dlg.sample.setText(text);
        dlg.setVisible(true);
        if (dlg.CANCELLED)
            return null;
        String attrs = "";
        if (dlg.fontSizeCB.getSelectedIndex() > 0)
            attrs += "size=\"" + dlg.fontSizeCB.getSelectedItem() + "\"";
        if (dlg.fontFamilyCB.getSelectedIndex() > 0)
            attrs += "face=\"" + dlg.fontFamilyCB.getSelectedItem() + "\"";
        if (dlg.colorField.getText().length() > 0)
            attrs += "color=\"" + dlg.colorField.getText() + "\"";
        if (attrs.length() > 0)
            return " " + attrs;
        else
            return null;
    }

    void inlineCB_actionPerformed(ActionEvent e) {
        if (inlineCBEventsLock)
            return;
        int sel = inlineCB.getSelectedIndex();
        if (sel == I_NORMAL) {
            Element el =
                    document.getCharacterElement(editor.getCaretPosition());
            SimpleAttributeSet attrs = new SimpleAttributeSet();
            attrs.addAttribute(StyleConstants.NameAttribute, HTML.Tag.CONTENT);
            if (editor.getSelectionEnd() > editor.getSelectionStart())
                document.setCharacterAttributes(
                        editor.getSelectionStart(),
                        editor.getSelectionEnd() - editor.getSelectionStart(),
                        attrs,
                        true);
            else
                document.setCharacterAttributes(
                        el.getStartOffset(),
                        el.getEndOffset() - el.getStartOffset(),
                        attrs,
                        true);
            return;
        }
        String text = "&nbsp;";
        if (editor.getSelectedText() != null)
            text = editor.getSelectedText();
        String tag = "";
        String att = "";
        switch (sel) {
            case I_EM:
                tag = "em";
                break;
            case I_STRONG:
                tag = "strong";
                break;
            case I_CODE:
                tag = "code";
                break;
            case I_SUPERSCRIPT:
                tag = "sup";
                break;
            case I_SUBSCRIPT:
                tag = "sub";
                break;
            case I_CITE:
                tag = "cite";
                break;
            case I_CUSTOM:
                tag = "font";
                att =
                        setFontProperties(
                                document.getCharacterElement(editor.getCaretPosition()),
                                editor.getSelectedText());
                if (att == null)
                    return;
                break;
        }
        String html = "<" + tag + att + ">" + text + "</" + tag + ">";
        if (editor.getCaretPosition() == document.getLength())
            html += "&nbsp;";
        editor.replaceSelection("");
        try {
            editorKit.insertHTML(
                    document,
                    editor.getCaretPosition(),
                    html,
                    0,
                    0,
                    HTML.getTag(tag));
            if (editor.getCaretPosition() == document.getLength())
                editor.setCaretPosition(editor.getCaretPosition() - 1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void doZoom(boolean in) {
        //TODO: Zoom
    }

    public void setDocument(Document doc) {
        this.document = (HTMLDocument) doc;
        initEditor();
    }

    public void initEditor() {
        editor.setDocument(document);
        resetUndoManager();
        document.addUndoableEditListener(undoHandler);
        editor.scrollRectToVisible(new Rectangle(0, 0, 0, 0));
        editor.setCaretPosition(0);
    }

    public boolean isDocumentChanged() {
        return undo.canUndo();
    }

    public void setStyleSheet(Reader r) {
        StyleSheet css = new StyleSheet();
        try {
            css.loadRules(r, null);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        editorKit.setStyleSheet(css);
    }

    public void reload() {

    }

    void doFind() {
        FindDialog dlg = new FindDialog();
        Dimension dlgSize = dlg.getSize();
        Dimension frmSize = this.getSize();
        Point loc = this.getLocationOnScreen();
        dlg.setLocation(
                (frmSize.width - dlgSize.width) / 2 + loc.x,
                (frmSize.height - dlgSize.height) / 2 + loc.y);
        dlg.setModal(true);
        if (editor.getSelectedText() != null)
            dlg.txtSearch.setText(editor.getSelectedText());
        else if (Context.get("LAST_SEARCHED_WORD") != null)
            dlg.txtSearch.setText(Context.get("LAST_SEARCHED_WORD").toString());
        dlg.setVisible(true);
        if (dlg.CANCELLED)
            return;
        Context.put("LAST_SEARCHED_WORD", dlg.txtSearch.getText());
        String repl = null;
        if (dlg.chkReplace.isSelected())
            repl = dlg.txtReplace.getText();
        Finder finder =
                new Finder(
                        this,
                        dlg.txtSearch.getText(),
                        dlg.chkWholeWord.isSelected(),
                        dlg.chkCaseSens.isSelected(),
                        dlg.chkRegExp.isSelected(),
                        repl);
        finder.start();

    }
}