package main.java.memoranda;

import main.java.memoranda.interfaces.INote;
import main.java.memoranda.interfaces.INoteListener;

import java.util.Collection;
import java.util.Vector;

public class CurrentNote {

	private static INote currentINote = null;
    private static Vector noteListeners = new Vector();

    public static INote get() {
        return currentINote;
    }

    public static void set(INote INote, boolean toSaveCurrentNote) {
        noteChanged(INote, toSaveCurrentNote);
        currentINote = INote;
    }

    public static void reset() {
//    	 set toSave to true to mimic status quo behaviour only. the appropriate setting could be false
        set(null, true);
    }

    public static void addNoteListener(INoteListener nl) {
        noteListeners.add(nl);
    }

    public static Collection getChangeListeners() {
        return noteListeners;
    }

    private static void noteChanged(INote INote, boolean toSaveCurrentNote) {
        for (int i = 0; i < noteListeners.size(); i++) {
            ((INoteListener)noteListeners.get(i)).noteChange(INote,toSaveCurrentNote);
		}
    }
}
