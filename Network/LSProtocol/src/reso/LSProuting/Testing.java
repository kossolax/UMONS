package reso.LSProuting;

import reso.common.*;
import reso.ip.*;
import reso.scheduler.*;
import reso.utilities.*;



public class Testing {
	
	public static final String TOPO_FILE= "reso/data/topology.txt";
	
	public static void main(String[] args) {
		String filename= Testing.class.getClassLoader().getResource(TOPO_FILE).getFile();
		
		AbstractScheduler scheduler= new Scheduler();
		try {
			Network network= NetworkBuilder.loadTopology(filename, scheduler);
			for (Node n: network.getNodes()) {
				IPRouter router = (IPRouter) n;
				router.addApplication( new LSPRoutingProtocol(router) );
				router.start();
			}
			scheduler.runUntil(10);
			for (Node n: network.getNodes()) {
				IPRouter router = (IPRouter) n;
				router.stop();
			}
			scheduler.runUntil(1);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
