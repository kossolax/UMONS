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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import reso.common.Host;
import reso.common.Message;

public class IPLayer
	implements IPInterfaceListener
{

	public final Host host;
	
    private final FIB fib= new FIB();
    private final List<IPInterfaceListener> rawListeners=
    	new ArrayList<IPInterfaceListener>();
    private final HashMap<Integer,List<IPInterfaceListener>> listeners=
    	new HashMap<Integer,List<IPInterfaceListener>>();
    
    private final List<IPInterfaceAdapter> ifaces=
    	new ArrayList<IPInterfaceAdapter>();
    private final Map<String, IPInterfaceAdapter> ifacesByName=
    	new HashMap<String, IPInterfaceAdapter>();
    
    private boolean forwarding= false;
	
    public IPLayer(Host host) {
    	this.host= host;
    }
    
    public void addInterface(IPInterfaceAdapter iface) {
    	ifaces.add(iface);
    	ifacesByName.put(iface.getName(), iface);
    	iface.addListener(this);
    }
    
    public IPInterfaceAdapter getInterfaceByName(String name) {
    	return ifacesByName.get(name);
    }
    
    public Collection<IPInterfaceAdapter> getInterfaces() {
    	return ifaces;
    }
    
    public void enableForwarding() {
    	forwarding= true;
    }
    
    public void disableForwarding() {
    	forwarding= false;
    }
    
    public boolean isForwardingEnabled() {
    	return forwarding;
    }
    
    public boolean hasAddress(IPAddress addr) {
    	for (IPInterfaceAdapter iface: ifaces)
    		if (iface.hasAddress(addr))
    			return true;
    	return false;
    }
    
	public void receive(IPInterfaceAdapter iface, Datagram msg)
	throws Exception {
		// Raw listeners are called for any (IP) protocol, even if
		// the datagram's destination is not this node
		for (IPInterfaceListener l: rawListeners)
			l.receive(iface, msg);
		
		// Datagrams addressed to this node
		if (hasAddress(msg.dst) || msg.dst.isBroadcast()) {
			List<IPInterfaceListener> listeners=
				this.listeners.get(msg.getProtocol());
			if (listeners != null)
				for (IPInterfaceListener l: listeners)
					l.receive(iface, msg);
			return;
		}
		
		// Datagram not addressed to this node. Forward if enabled...
		if (forwarding)
			forward(msg);
	}
	
	public void forward(Datagram msg)
	throws Exception {
		if (msg.dst.isBroadcast())
			return;
		if (msg.getTTL() <= 1)
			return;
		msg.decTTL();
    	IPRouteEntry re= fib.lookup(msg.dst);
    	if (re == null)
    		throw new Exception("Destination unreachable [" + msg.dst + "]");
    	if (re.oif != null) {
    		re.oif.send(msg, re.gateway);
    	} else {
    		throw new Exception("Routing through gateway is not supported");
    	}
	}
	
	/* Add a listener for any (IP) protocol.
	 * This listener will be called even for datagrams that are
	 * not destined to this node (kind or promiscuous mode).
	 */
    public void addRawListener(IPInterfaceListener l) {
    	rawListeners.add(l);
    }
    
    /* Add a listener for a specific protocol number.
     * This listener will only be called when a received datagram
     * is destined to the local node and when the datagram's protocol number
     * matches that of the listener. */
    public void addListener(int protocol, IPInterfaceListener l) {
    	List<IPInterfaceListener> listeners= this.listeners.get(protocol);
    	if (listeners == null) {
    		listeners= new ArrayList<IPInterfaceListener>();
    		this.listeners.put(protocol, listeners);
    	}
    	listeners.add(l);
    }
    
    /* Remove a listener for a specific protocol. */
    public void removeListener(int protocol, IPInterfaceListener l) {
    	List<IPInterfaceListener> listeners= this.listeners.get(protocol);
    	if (listeners == null)
    		return;
    	listeners.remove(listeners);
    }
    
    public void addRoute(IPAddress dst, String name)
		throws Exception {
    	IPInterfaceAdapter oif= ifacesByName.get(name);
    	if (oif == null)
    		throw new Exception("Unknown interface [" + name + "]");
    	IPRouteEntry re= new IPRouteEntry(dst, oif, "STATIC");
    	fib.add(re);
    }
    
    public void addRoute(IPAddress dst, IPInterfaceAdapter oif)
	throws Exception {
    	if (!ifaces.contains(oif))
    		throw new Exception("Unknown interface [" + oif + "]");
    	IPRouteEntry re= new IPRouteEntry(dst, oif, "STATIC");
    	fib.add(re);
    }

    public void addRoute(IPAddress dst, IPAddress gateway)
    	throws Exception {
    	IPRouteEntry re= new IPRouteEntry(dst, gateway, "STATIC");
    	fib.add(re);
    }
    
    public void addRoute(IPRouteEntry re)
    throws Exception {
    	if ((re.oif != null) && !ifaces.contains(re.oif))
    		throw new Exception("Unknown interface [" + re.oif + "]");
    	fib.add(re);
    }
    
    public void removeRoute(IPAddress dst) {
    	
    	fib.remove(dst);
    }

    public Collection<IPRouteEntry> getRoutes() {
    	return fib.getEntries();
    }
    
    public IPRouteEntry getRouteTo(IPAddress dst) {
    	try {
    		return fib.lookup(dst);
    	} catch (Exception e) {
			return null;
		}
    }
    
    public void send(IPAddress src, IPAddress dst, int protocol, Message payload)
		throws Exception {
    	//System.out.println("IPLayer::send(" + src + "," + dst + "," + protocol + "," + payload + ")");

    	IPRouteEntry re= fib.lookup(dst);
    	if (re == null)
    		throw new Exception("Destination unreachable [" + dst + "]");
    	
    	//System.out.println(" lookup -> " + re.oif + "," + re.gateway);
    	
    	Datagram datagram;
    	if (!src.isUndefined()) {
    		if (!hasAddress(src))
    			throw new Exception("IP address spoofing [" + src + "]");
    		datagram= new Datagram(src, dst, protocol, 255, payload);
    	} else
    		datagram= new Datagram(re.oif.getAddress(), dst, protocol, 255, payload);
    	re.oif.send(datagram, re.gateway);
	}

}
