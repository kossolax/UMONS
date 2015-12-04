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
package reso.ip;

import reso.common.AbstractMessageWithPayload;
import reso.common.Message;
import reso.common.ProtocolTable;

public class Datagram
extends AbstractMessageWithPayload
{

	private final static ProtocolTable protocols= new ProtocolTable();
	
    public final IPAddress src, dst;

    private int ttl;

    public Datagram(IPAddress src, IPAddress dst, int protocol,
		    int ttl, Message payload) {
    	super(protocol, payload);
    	this.src= src;
    	this.dst= dst;
    	this.ttl= ttl;
    }

    public int getTTL() {
    	return ttl;
    }

    public void decTTL() {
    	ttl-= 1;
    }

    public String toString() {
    	return "src=" + src + ", dst=" + dst + ",proto=" + getProtocol() +
    		", payload=[" + getPayload() + "]";
    }
    
	public static int allocateProtocolNumber(String name) {
		return protocols.allocateProtocolNumber(name);
	}

}
