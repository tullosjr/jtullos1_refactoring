/**
 * Storage.java
 * Created on 12.02.2003, 0:58:42 Alex
 * Package: net.sf.memoranda.util
 * 
 * @author Alex V. Alishevskikh, alex@openmechanics.net
 * Copyright (c) 2003 Memoranda Team. http://memoranda.sf.net
 */
package main.java.memoranda.util;

import main.java.memoranda.interfaces.*;
import main.java.memoranda.interfaces.INote;

/**
 * 
 */
/*$Id: Storage.java,v 1.4 2004/01/30 12:17:42 alexeya Exp $*/
public interface Storage {
            
    ITaskList openTaskList(IProject prj);
    void storeTaskList(ITaskList tl, IProject prj);
    
    INoteList openNoteList(IProject prj);
    void storeNoteList(INoteList nl, IProject prj);
    
    void storeNote(INote INote, javax.swing.text.Document doc);
    javax.swing.text.Document openNote(INote INote);
    void removeNote(INote INote);
    
    String getNoteURL(INote INote);
    
    void openProjectManager();    
    void storeProjectManager();
    
    void openEventsManager();
    void storeEventsManager();
    
    void openMimeTypesList();
    void storeMimeTypesList();
    
    void createProjectStorage(IProject prj);
    void removeProjectStorage(IProject prj);
   
    IResourcesList openResourcesList(IProject prj);
    void storeResourcesList(IResourcesList rl, IProject prj);
    
    void restoreContext();
    void storeContext(); 
       
}
