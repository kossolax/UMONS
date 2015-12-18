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
	
	public static final String TOPO_FILE= "reso/data/topologyBIS.txt";
	
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
			for (Node n: network.getNodes()) {
				IPRouter router = (IPRouter) n;
				router.stop();
			}
			scheduler.run();
			FIBDumper.dumpForAllRouters(network);
			for (Node n: network.getNodes()) {
				IPAddress ndst= ((IPHost) n).getIPLayer().getInterfaceByName("lo0").getAddress();
				
				File f= new File(".\\graph\\topology-routing-" + ndst + ".graphviz");
				Writer w= new BufferedWriter(new FileWriter(f));
				NetworkGrapher.toGraphviz2(network, ndst, new PrintWriter(w));
				w.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
