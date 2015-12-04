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

import java.util.HashMap;
import java.util.Map;

import reso.common.Task;
import reso.ethernet.EthernetAddress;
import reso.ethernet.EthernetFrame;
import reso.ethernet.EthernetInterface;

public class ARPProtocol {
	
    private Map<IPAddress, EthernetAddress> tableARP=
    	new HashMap<IPAddress, EthernetAddress>();
    private Map<IPAddress, Task> pendingARPTasks=
    	new HashMap<IPAddress, Task>();
    
    private final EthernetInterface iface;
    private final IPEthernetAdapter adapter;
    
    public ARPProtocol(IPEthernetAdapter adapter) {
    	this.adapter= adapter;
    	this.iface= adapter.iface;
    }

    private class TaskSendFrame
    implements Task {
    	public final Datagram datagram;
    	public final IPAddress gateway;
    	public final EthernetInterface iface; 
    	public TaskSendFrame(Datagram datagram, IPAddress gateway, EthernetInterface iface) {
    		this.datagram= datagram;
    		this.gateway= gateway;
    		this.iface= iface;
    	}
    	public void run()
    	throws Exception {
        	EthernetAddress maddr= tableARP.get(gateway);
        	if (maddr != null) {
        		EthernetFrame frame= new EthernetFrame(iface.addr, maddr, EthernetFrame.PROTO_IP, datagram);
        		iface.send(frame);
        	}
    	}
    }
    	
    public EthernetAddress getMapping(IPAddress addr) {
    	return tableARP.get(addr);
    }
    
    public void performARPRequest(IPAddress addr, Datagram datagram)
    throws Exception {
    	EthernetFrame frame= new EthernetFrame(iface.addr, EthernetAddress.BROADCAST, EthernetFrame.PROTO_ARP,
    		ARPMessage.request(addr));
    	pendingARPTasks.put(addr, new TaskSendFrame(datagram, addr, iface));
    	iface.send(frame);
    }
    
    public void handleARPMessage(EthernetFrame frame)
    throws Exception {
    	//System.out.println(adapter.ip.host + " :: " + adapter+" handleARPMessage(" + frame + ")");
    	ARPMessage msg= (ARPMessage) frame.payload;
    	
    	switch (msg.type) {
    	case REQUEST:		
    		if (msg.ipAddr.isBroadcast() || !adapter.hasAddress(msg.ipAddr))
    			return;
    		ARPMessage arpMsg= ARPMessage.response(msg.ipAddr, iface.addr); 
    		EthernetFrame response= new EthernetFrame(iface.addr, frame.src, EthernetFrame.PROTO_ARP, arpMsg);
    		iface.send(response);
    		break;
    		
    	case RESPONSE:
    		Task tsk= pendingARPTasks.get(msg.ipAddr);
    		if (tsk == null)
    			return;
    		tableARP.put(msg.ipAddr, msg.ethAddr);
    		tsk.run();
    		break;
    		
    	}
    }

	
}
