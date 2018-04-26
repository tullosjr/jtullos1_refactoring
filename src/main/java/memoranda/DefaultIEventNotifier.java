/**
 * DefaultIEventNotifier.java Created on 10.03.2003, 21:18:02 Alex Package:
 * net.sf.memoranda
 * 
 * @author Alex V. Alishevskikh, alex@openmechanics.net Copyright (c) 2003
 *         Memoranda Team. http://memoranda.sf.net
 */
package main.java.memoranda;

import main.java.memoranda.interfaces.IEvent;
import main.java.memoranda.interfaces.IEventNotificationListener;
import main.java.memoranda.ui.EventNotificationDialog;

/**
 *  
 */
/*$Id: DefaultIEventNotifier.java,v 1.4 2004/01/30 12:17:41 alexeya Exp $*/
public class DefaultIEventNotifier implements IEventNotificationListener {

	/**
	 * Constructor for DefaultIEventNotifier.
	 */
	public DefaultIEventNotifier() {
		super();
	}

	/**
	 * @see IEventNotificationListener#eventIsOccured(IEvent)
	 */
	public void eventIsOccured(IEvent ev) {
		new EventNotificationDialog(
			"Memoranda event",
			ev.getTimeString(),
			ev.getText());
	}
	/**
	 * @see IEventNotificationListener#eventsChanged()
	 */
	public void eventsChanged() {
		//
	}

	
}
