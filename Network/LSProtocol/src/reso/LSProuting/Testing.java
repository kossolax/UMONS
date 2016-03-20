package reso.LSProuting;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
	private static void dumpGraphiz(Network network, String path, String name) throws IOException {
		for (Node n: network.getNodes()) {
			IPAddress ndst= getRouterID(((IPHost) n).getIPLayer());
			
			File f= new File(path+"topology-routing-"+name+"-" + ndst + ".graphviz");
			Writer w= new BufferedWriter(new FileWriter(f));
			NetworkGrapher.toGraphviz2(network, ndst, new PrintWriter(w));
			w.close();
		}
	}
	public static final String TOPO_FILE= "reso/data/topologyTest4.txt";
	
	public static void main(String[] args) {
		String filename= Testing.class.getClassLoader().getResource(TOPO_FILE).getFile();
		
		AbstractScheduler scheduler = new Scheduler();
		try {
			Network network= NetworkBuilder.loadTopology(filename, scheduler);
			
			for (Node n: network.getNodes()) {
				IPRouter router = (IPRouter) n;
				router.addApplication( new LSPRoutingProtocol(scheduler, router, 1, 5) );
				router.start();
			}
			
			
			
			scheduler.runUntil(15);
			dumpGraphiz(network, "./data/", "un");
			//FIBDumper.dumpForAllRouters(network);
			System.out.println("fin simulation 1/4 : Aucun problème sur les liens");
			
			
			((IPHost) network.getNodeByName("R4")).getIPLayer().getInterfaceByName("eth1").setMetric(1000);
			((IPHost) network.getNodeByName("R4")).getIPLayer().getInterfaceByName("eth2").setMetric(1000);
			((IPHost) network.getNodeByName("R2")).getIPLayer().getInterfaceByName("eth0").setMetric(1000);
			((IPHost) network.getNodeByName("R8")).getIPLayer().getInterfaceByName("eth1").setMetric(1000);
			scheduler.runUntil(60);
			dumpGraphiz(network, "./data/", "deux");
			System.out.println("fin simulation 2/4 : Changement des metrics sur certains liens: nouvelles routes optimal");
			
			
			((IPHost) network.getNodeByName("R6")).getInterfaceByName("eth1").down();
			((IPHost) network.getNodeByName("R6")).getInterfaceByName("eth0").down();
			((IPHost) network.getNodeByName("R4")).getInterfaceByName("eth2").down();	
			scheduler.runUntil(100);
			dumpGraphiz(network, "./data/", "trois");
			System.out.println("fin simulation 3/4 : Lien brisé dans certain sens: nouvelles routes optimal");
			
			
			((IPHost) network.getNodeByName("R4")).getInterfaceByName("eth1").down();	
			scheduler.runUntil(200);
			dumpGraphiz(network, "./data/", "quatre");
			System.out.println("fin simulation 4/4 : Isolation d'un routeur: timeout de LSP dans les LSDB");
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
