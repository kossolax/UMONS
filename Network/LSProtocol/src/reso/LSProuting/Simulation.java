package reso.LSProuting;

import reso.common.*;
import reso.ip.*;
import reso.scheduler.AbstractScheduler;

public class Simulation extends AbstractTimer {
	Network network;
	public Simulation(AbstractScheduler scheduler, double interval, boolean repeat, Network network) {
		super(scheduler, interval, repeat);
		this.network = network;
	}

	@Override
	protected void run() throws Exception {
		for( Node n: network.getNodes() ) {
			IPRouter r = (IPRouter) n;
			System.out.println(scheduler.getCurrentTime() + "  "+ r);
			
			
		}
	}
}
