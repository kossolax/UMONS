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

// This class is the basis for event schedulers. A scheduler maintains an
// internal simulation time and a set of pending events. Each event derives
// its behaviour from the AbstractEvent class.
//
// An complete implementation of a scheduler is provided in the 
// SimpleScheduler class. The SimpleScheduler class relies on the JDK's
// priorityQueue data structure to keep the pending events ordered
// according to their time of occurrence.
public abstract class AbstractScheduler
{

    protected double time= 0.0; // in seconds

    public abstract void schedule(AbstractEvent evt);
    public abstract boolean hasMoreEvents();
    public abstract void runNextEvent();

    public void run() {
    	while (hasMoreEvents())
    		runNextEvent();
    }
    
    public void runUntil(double timeLimit) {
    	while ((getCurrentTime() < timeLimit) && hasMoreEvents())
    		runNextEvent();
    }

    /**
     * Return the current simulation time (in seconds).
     * 
     * @return number of simulated seconds elapsed sice the beginning of the simulation.
     */
    public double getCurrentTime() {
    	return time;
    }
    
}
