/*******************************************************************************
 * Copyright (c) 2011 Bruno Quoitin.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Bruno Quoitin - initial API and implementation
 ******************************************************************************/
package reso.scheduler;

public abstract class AbstractEvent
    implements Comparable<AbstractEvent>
{

    private final double time;

    /**
     * Create a new simulation event.
     * @param time in seconds the event should occur.
     */
    public AbstractEvent(double time) {
    	this.time= time;
    }

    /**
     * Return the time in seconds this simulation is scheduled.
     * @return
     */
    public final double getTime() {
    	return time;
    }

    public final int compareTo(AbstractEvent evt) {
    	if (this.time < evt.time)
    		return -1;
    	else if (this.time > evt.time)
    		return 1;
    	return 0;
    }

    public abstract void run() throws Exception;

}
