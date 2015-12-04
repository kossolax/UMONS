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
package reso.common;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import reso.ethernet.EthernetAddress;
import reso.scheduler.AbstractScheduler;

public class Network {

	public final AbstractScheduler scheduler;
	private static long nextEthernetID= 0;
	
	private Map<String,Node> nodes= new TreeMap<String,Node>();
	
	public Network(AbstractScheduler scheduler) {
		this.scheduler= scheduler;
	}
	
	public void addNode(Node node)
	throws Exception {
		if (nodes.containsKey(node.name))
			throw new Exception("Node [" + node.name + "] already exists");
		nodes.put(node.name, node);
		node.setNetwork(this);
	}
	
	public Collection<Node> getNodes() {
		return nodes.values();
	}
	
	public Node getNodeByName(String name) {
		return nodes.get(name);
	}
	
	public AbstractScheduler getScheduler() {
		return scheduler;
	}
	
	public static EthernetAddress generateEthernetAddress() {
		nextEthernetID+= 1;
		long id= nextEthernetID;
		int [] bytes= new int [] {0, 0, 0, 0, 0, 0};
		for (int i= 0; i < 6; i++) {
			bytes[5-i]= (int) (id % 256);
			id= id/256;
		}
		EthernetAddress addr= null;
		try {
			addr= EthernetAddress.getByAddress(bytes[0], bytes[1], bytes[2], bytes[3], bytes[4], bytes[5]);
		} catch (Exception e) {
		}
		return addr;
	}

}
