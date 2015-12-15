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
			scheduler.run();
			
			/*
			IPRouter router = (IPRouter) network.getNodeByName("R1");
			LSPRoutingProtocol app1 = new LSPRoutingProtocol(router);
			router.addApplication( app1 );
			router.start();
			
			router = (IPRouter) network.getNodeByName("R4");
			LSPRoutingProtocol app2 = new LSPRoutingProtocol(router);
			router.addApplication( app2 );
			router.start();	
			
			
			Simulation s = new Simulation(scheduler, 10, true, network);
			s.start();
			scheduler.runUntil(10);
			
			System.out.println("R1   :");
			System.out.println(app1.table);
			System.out.println("--------------------");
			System.out.println("R4   :");
			System.out.println(app2.table);
			 */		
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
}
