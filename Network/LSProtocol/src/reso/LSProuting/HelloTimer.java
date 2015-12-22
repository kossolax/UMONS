package reso.LSProuting;

import reso.common.*;
import reso.scheduler.AbstractScheduler;

public class HelloTimer extends AbstractTimer {
	
	LSPRoutingProtocol protocol;
	public HelloTimer(AbstractScheduler scheduler, double interval, boolean repeat, LSPRoutingProtocol lspRoutingProtocol) {
		super(scheduler, interval, repeat);
		this.protocol = lspRoutingProtocol;
	}

	@Override
	protected void run() throws Exception {
		protocol.sendHELLO();
	}
}
