package reso.LSProuting;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Writer;

import reso.common.*;
import reso.ip.*;
import reso.scheduler.*;
import reso.utilities.*;

public class Testing {
	private static IPAddress getRouterID(IPLayer ip) {
		IPAddress routerID= null;
		for (IPInterfaceAdapter iface: ip.getInterfaces()) {
			IPAddress addr= iface.getAddress();
			if (routerID == null)
				routerID= addr;
			else if (routerID.compareTo(addr) < 0)
				routerID= addr;
		}
		return routerID;
	}
	public static final String TOPO_FILE= "reso/data/topologyTest4.txt";
	
	public static void main(String[] args) {
		String filename= Testing.class.getClassLoader().getResource(TOPO_FILE).getFile();
		
		AbstractScheduler scheduler = new Scheduler();
		try {
			Network network= NetworkBuilder.loadTopology(filename, scheduler);
			for (Node n: network.getNodes()) {
				IPRouter router = (IPRouter) n;
				router.addApplication( new LSPRoutingProtocol(scheduler, router, 10, 30) );
				router.start();
			}
			//scheduler.run();
			scheduler.runUntil(30);
			((IPHost) network.getNodeByName("R5")).getInterfaceByName("eth0").down();
			((IPHost) network.getNodeByName("R7")).getInterfaceByName("eth0").down();
			//((IPHost) network.getNodeByName("R5")).getInterfaceByName("eth1").down();
			((IPHost) network.getNodeByName("R6")).getInterfaceByName("eth0").down();
			//((IPHost) network.getNodeByName("R2")).getInterfaceByName("eth2").down();
			
			
			scheduler.runUntil(100);
			
			//FIBDumper.dumpForAllRouters(network);
			for (Node n: network.getNodes()) {
				IPAddress ndst= getRouterID(((IPHost) n).getIPLayer());
				
				File f= new File(".\\topology-routing-" + ndst + ".graphviz");
				Writer w= new BufferedWriter(new FileWriter(f));
				NetworkGrapher.toGraphviz2(network, ndst, new PrintWriter(w));
				w.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
