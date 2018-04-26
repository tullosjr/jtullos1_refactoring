/**
 * IProject.java
 * Created on 11.02.2003, 16:11:47 Alex
 * Package: net.sf.memoranda
 * 
 * @author Alex V. Alishevskikh, alex@openmechanics.net
 * Copyright (c) 2003 Memoranda Team. http://memoranda.sf.net
 */
package main.java.memoranda.interfaces;

import main.java.memoranda.date.CalendarDate;

/**
 * 
 */

/*$Id: IProject.java,v 1.5 2004/11/22 10:02:37 alexeya Exp $*/
public interface IProject {
    
    public static final int SCHEDULED = 0;
   
    public static final int ACTIVE = 1;
    
    public static final int COMPLETED = 2;
    
    public static final int FROZEN = 4;
    
    public static final int FAILED = 5;
    
    String getID();
       
    CalendarDate getStartDate();
    void setStartDate(CalendarDate date);
    
    CalendarDate getEndDate();
    void setEndDate(CalendarDate date);
    
    String getTitle();
    void setTitle(String title);
    
    void setDescription(String description);
    String getDescription();
    
    int getStatus();
            
    //int getProgress();
    
    //ITaskList getTaskList();
    
    //INoteList getNoteList();
    
    //IResourcesList getResourcesList();
    
    void freeze();
    void unfreeze();  
    
}
