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
package reso.ethernet;

import reso.common.AbstractHardwareInterface;
import reso.common.Host;

public class EthernetInterface
    extends AbstractHardwareInterface<EthernetFrame>
{

    public final EthernetAddress addr;
    
    public EthernetInterface(Host host, EthernetAddress addr)
	throws Exception {
    	super(host, "eth");
    	this.addr= addr;
    }

    public boolean hasAddr(EthernetAddress addr) {
    	if (this.addr.equals(addr))
    		return true;
    	if (addr.isBroadcast())
    		return true;
    	return false;
    }
	    
    public void receive(EthernetFrame frame)
    throws Exception {
    	if (!isActive())
    		return;
    	if (frame.dst.isBroadcast() || addr.equals(frame.dst))
    		toListeners(frame);
    }

	public void send(EthernetFrame msg)
	throws Exception {
		if (!isActive())
			return;
    	getLink().send(this, msg);
    }
	
}
