package reso.LSProuting;

import reso.common.*;
import reso.ip.*;
import reso.scheduler.AbstractScheduler;

public class LSPTimer extends AbstractTimer {
	
	LSPRoutingProtocol protocol;
	public LSPTimer(AbstractScheduler scheduler, double interval, boolean repeat, LSPRoutingProtocol lspRoutingProtocol) {
		super(scheduler, interval, repeat);
		this.protocol = lspRoutingProtocol;
	}

	@Override
	protected void run() throws Exception {
		protocol.sendLSD();
	}
}
