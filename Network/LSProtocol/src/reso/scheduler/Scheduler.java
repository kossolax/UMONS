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

import java.util.PriorityQueue;

public class Scheduler
extends AbstractScheduler {

    private PriorityQueue<AbstractEvent> pq;

    public Scheduler() {
    	this.pq= new PriorityQueue<AbstractEvent>();
    }
    
    public void schedule(AbstractEvent evt) {
    	assert(evt.getTime() > getCurrentTime());
    	pq.offer(evt);
    }
    
    public boolean hasMoreEvents() {
    	return (pq.size() > 0);
    }

    public void runNextEvent() {
    	AbstractEvent evt= pq.poll();
    	time= evt.getTime();
    	try {
    		evt.run();
    	} catch (Exception e) {
    		System.err.println(e.getMessage());
    	}
    }
    
    public void dumpEvents() {
    	for (AbstractEvent e: pq)
    		System.out.println("queued [" + e + "]");
    }
    

}

