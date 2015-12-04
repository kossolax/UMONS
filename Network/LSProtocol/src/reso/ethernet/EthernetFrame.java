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

import reso.common.*;

public class EthernetFrame extends AbstractMessage
{

	public static ProtocolTable protocols= new ProtocolTable();
	
	public static final int PROTO_ARP= 0;//allocateProtocolNumber("ARP");
	public static final int PROTO_IP= 1;//allocateProtocolNumber("IP");

    public final EthernetAddress src, dst;
    public final int protocol;
    public final Message payload;

    public EthernetFrame(EthernetAddress src, EthernetAddress dst, int protocol, Message payload) {
    	this.src= src;
    	this.dst= dst;
    	this.protocol= protocol;
    	this.payload= payload;
    }

    public String toString() {
    	return "src=" + src + ", dst=" + dst + ", payload=[" + payload.toString() + "]";
    }
    
	public static int allocateProtocolNumber(String name) {
		return protocols.allocateProtocolNumber(name);
	}

}
