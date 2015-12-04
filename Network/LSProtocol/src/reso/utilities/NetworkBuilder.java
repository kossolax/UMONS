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
package reso.utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import reso.common.HardwareInterface;
import reso.common.Link;
import reso.common.Message;
import reso.common.Network;
import reso.common.Node;
import reso.ethernet.EthernetAddress;
import reso.ethernet.EthernetFrame;
import reso.ethernet.EthernetInterface;
import reso.ip.IPAddress;
import reso.ip.IPEthernetAdapter;
import reso.ip.IPHost;
import reso.ip.IPInterfaceAdapter;
import reso.ip.IPLayer;
import reso.ip.IPLoopbackAdapter;
import reso.ip.IPRouter;
import reso.scheduler.AbstractScheduler;

public class NetworkBuilder {
	
	/**
	 * Create an IP host with a single physical (Ethernet) interface and a loopback interface.
	 *  
	 * @param name is the name of the host
	 * @param addr is the IP address associated with the physical interface
	 * @param maddr is the physical (MAC) address
	 * @param scheduler
	 * @return
	 * @throws Exception
	 */
	public static IPHost createHost(Network network, String name, IPAddress addr, EthernetAddress maddr)
	throws Exception {	
		IPHost host= new IPHost(name);
	    IPLayer ip= host.getIPLayer();
		
		// Add loopback interface w/ IP address 127.0.0.1
		// + add route to 127.0.0.1
	    IPInterfaceAdapter ip_lo0= new IPLoopbackAdapter(ip, 0);
	    ip.addInterface(ip_lo0);
	    ip_lo0.addAddress(IPAddress.LOCALHOST);
	    host.getIPLayer().addRoute(IPAddress.LOCALHOST, ip_lo0);

	    // Add Ethernet interface w/ provided IP address
	    // + add a route to that address
	    EthernetInterface if_eth0= new EthernetInterface(host, maddr);
	    host.addInterface(if_eth0);
	    IPInterfaceAdapter ip_eth0= new IPEthernetAdapter(ip, if_eth0);
	    ip_eth0.addAddress(addr);
	    ip.addInterface(ip_eth0);
	    
	    network.addNode(host);
		return host;
	}

	public static IPRouter createRouter(Network network, String name,
			IPAddress[] ipAddresses, EthernetAddress[] ethernetAddresses)
	throws Exception {
		IPRouter router= new IPRouter(name);
		IPLayer ip= router.getIPLayer();
		
	    // Add Ethernet interfaces w/ provided IP addresses
	    // + add a route to each address
		for (int i= 0; i < ipAddresses.length; i++) {
			EthernetInterface if_eth= new EthernetInterface(router, ethernetAddresses[i]);
			router.addInterface(if_eth);
			IPInterfaceAdapter ip_eth= new IPEthernetAdapter(ip, if_eth);
			ip_eth.addAddress(ipAddresses[i]);
			ip.addInterface(ip_eth);
		}
		
		network.addNode(router);
		return router;
	}
	
	private static enum State {
		INIT, ROUTER, LINK
	};
	
	private static IPRouter loadTopologyRouter(List<String> tokens, Network network)
	throws Exception {
		if (tokens.size() != 2)
			throw new Exception("Invalid number of parameters in router declaration");
		String name= tokens.get(1);
		IPRouter router= createRouter(network, name, new IPAddress[] {}, new EthernetAddress[] {});
		return router;
	}
	
	private static void loadTopologyRouterLoopback(List<String> tokens, IPRouter router)
	throws Exception {
		if (tokens.size() != 2)
			throw new Exception("Invalid number of parameters in lo interface declaration");
		IPInterfaceAdapter lo= new IPLoopbackAdapter(router.getIPLayer(), 0);
		router.getIPLayer().addInterface(lo);
		IPAddress addr= IPAddress.getByAddress(tokens.get(1));
		lo.addAddress(addr);
		lo.setMetric(0);
		router.getIPLayer().addInterface(lo);
		router.getIPLayer().addRoute(addr, lo);
	}
	
	private static void loadTopologyRouterEthernet(List<String> tokens, IPRouter router)
	throws Exception {
		if (tokens.size() != 2)
			throw new Exception("Invalid number of parameters in eth interface declaration");
		EthernetInterface eth= new EthernetInterface(router, Network.generateEthernetAddress());
		router.addInterface(eth);
		IPAddress addr= IPAddress.getByAddress(tokens.get(1));
		IPInterfaceAdapter ip_eth= new IPEthernetAdapter(router.getIPLayer(), eth);
		ip_eth.addAddress(addr);
		router.getIPLayer().addInterface(ip_eth);
	}
	
	private static Link<?> loadTopologyLink(List<String> tokens, Network network) throws Exception {
		if (tokens.size() != 6)
			throw new Exception("Invalid number of parameters in link declaration");
		Node n1= network.getNodeByName(tokens.get(1));
		HardwareInterface<? extends Message> if1= n1.getInterfaceByName(tokens.get(2));
		Node n2= network.getNodeByName(tokens.get(3));
		HardwareInterface<? extends Message> if2= n2.getInterfaceByName(tokens.get(4));
		if (!if1.getType().equals("eth") || !if2.getType().equals("eth"))
			throw new Exception("cannot connect interfaces [" + n1 + "." + if1 + "," + n2 + "." + if2 + "]");
		return new Link<EthernetFrame>((EthernetInterface) if1, (EthernetInterface) if2,
			Double.valueOf(tokens.get(5)));
	}
	
	private static void loadTopologyLinkMetric(List<String> tokens, Link<?> link) throws Exception {
		if ((tokens.size() < 2) || (tokens.size() > 3))
			throw new Exception("Invalid number of parameters in link metric declaration");
		HardwareInterface<? extends Message> if1= link.getHead();
		IPInterfaceAdapter ip_if1= ((IPHost) if1.getNode()).getIPLayer().getInterfaceByName(if1.getName());
		HardwareInterface<? extends Message> if2= link.getTail();
		IPInterfaceAdapter ip_if2= ((IPHost) if2.getNode()).getIPLayer().getInterfaceByName(if2.getName());
		String token= tokens.get(1);
		int metric;
		if (token.equals("?"))
			metric= Integer.MAX_VALUE;
		else
			metric= Integer.valueOf(token);
		ip_if1.setMetric(metric);
		
		if (tokens.size() == 3) {
			token= tokens.get(2);
			if (token.equals("?"))
				metric= Integer.MAX_VALUE;
			else
				metric= Integer.valueOf(token);
		}
		ip_if2.setMetric(metric);
	}
	
	public static Network loadTopology(String filename, AbstractScheduler scheduler)
	throws Exception {
		Network network= new Network(scheduler);
		State state= State.INIT;
		FileReader fr= new FileReader(filename);
		BufferedReader br= new BufferedReader(fr);
		IPRouter router= null;
		Link<?> link= null;
		int lineCount= 0;
		try {
			do {
				String line= br.readLine();
				if (line == null)
					break; // end of file
				if (line.charAt(0) == '#')
					continue; // comment
				lineCount++;
				StringTokenizer st= new StringTokenizer(line);
				if (!st.hasMoreTokens())
					continue;
				List<String> tokens= new ArrayList<String>();
				while (st.hasMoreTokens())
					tokens.add(st.nextToken());
				
				String token= tokens.get(0);
				switch (state) {
				case INIT:
					if (token.equals("router")) {
						router= loadTopologyRouter(tokens, network);
						state= State.ROUTER;
					} else if (token.equals("link")) {
						link= loadTopologyLink(tokens, network);
						state= State.LINK;						
					} else
						throw new Exception("Unexpected token [" + token + "]");
					break;
					
				case ROUTER:
					if (token.equals("lo")) {
						loadTopologyRouterLoopback(tokens, router);
					} else if (token.equals("eth")) {
						loadTopologyRouterEthernet(tokens, router);
					} else if (token.equals("router")) {
						router= loadTopologyRouter(tokens, network);
					} else if (token.equals("link")) {
						link= loadTopologyLink(tokens, network);
						state= State.LINK;
					}
					break;
					
				case LINK:
					if (token.equals("router")) {
						router= loadTopologyRouter(tokens, network);
						state= State.ROUTER;
					} else if (token.equals("link")) {
						link= loadTopologyLink(tokens, network);
					} else if (token.equals("metric")) {
						loadTopologyLinkMetric(tokens, link);
					} else
						throw new Exception("Unexpected token [" + token + "]");
					break;
					
				}
			} while (true);
		} catch (Exception e) {
			throw new Exception("Could not load topology @ line "+ lineCount + " (" + e.getMessage() + ")", e);
		} finally {
			br.close();
		}
		return network;
	}
	
}
