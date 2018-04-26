package main.java.memoranda.interfaces;

import main.java.memoranda.interfaces.NoteList;
import main.java.memoranda.interfaces.Project;
import main.java.memoranda.interfaces.ResourcesList;
import main.java.memoranda.interfaces.TaskList;

/*$Id: ProjectListener.java,v 1.3 2004/01/30 12:17:41 alexeya Exp $*/
public interface ProjectListener {
  void projectChange(Project prj, NoteList nl, TaskList tl, ResourcesList rl);
  void projectWasChanged();
}