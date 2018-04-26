package main.java.memoranda.interfaces;

public interface NoteListener {
  void noteChange(Note note, boolean toSaveCurrentNote);
}
