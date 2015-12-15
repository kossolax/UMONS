package reso.LSProuting;

import java.util.HashMap;
import java.util.Map;

import javax.swing.plaf.TableHeaderUI;

import reso.common.AbstractApplication;
import reso.common.Interface;
import reso.common.InterfaceAttrListener;
import reso.examples.dv_routing.DVMessage;
import reso.ip.Datagram;
import reso.ip.IPAddress;
import reso.ip.IPInterfaceAdapter;
import reso.ip.IPInterfaceListener;
import reso.ip.IPLayer;
import reso.ip.IPLoopbackAdapter;
import reso.ip.IPRouter;

public class LSPRoutingProtocol extends AbstractApplication implements IPInterfaceListener, InterfaceAttrListener {

	public static final String PROTOCOL_NAME= "LSP_ROUTING";
	public static final int IP_PROTO_LSP = Datagram.allocateProtocolNumber(PROTOCOL_NAME);
	
	private final IPLayer ip;
	public final Map<IPAddress,HELLOMessage> table = new HashMap<IPAddress,HELLOMessage>();
	
	/** Constructor
	 * 
	 * @param router is the router that hosts the routing protocol
	 */
	public LSPRoutingProtocol(IPRouter router) {
		super(router, PROTOCOL_NAME);
		this.ip = router.getIPLayer();
	}

	private IPAddress getRouterID() {
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
	
	@Override
	public void start() throws Exception {
		ip.addListener(IP_PROTO_LSP, this);
		
		for (IPInterfaceAdapter iface: ip.getInterfaces())
			iface.addAttrListener(this);
		
		HELLOMessage hello = new HELLOMessage(getRouterID());
		// Send initial DV
		for (IPInterfaceAdapter iface: ip.getInterfaces()) {
			if (iface instanceof IPLoopbackAdapter)
				continue;
			
			Datagram dm = new Datagram(iface.getAddress(), IPAddress.BROADCAST, IP_PROTO_LSP, 1, hello);
			iface.send(dm, null);
		}
	}
	
	public void stop() {
		ip.removeListener(IP_PROTO_LSP, this);
		for (IPInterfaceAdapter iface: ip.getInterfaces())
			iface.removeAttrListener(this);
	}

	public int addMetric(int m1, int m2) {
		if (((long) m1) + ((long) m2) > Integer.MAX_VALUE)
			return Integer.MAX_VALUE;
		return m1 + m2;
	}
	
	@Override
	public void receive(IPInterfaceAdapter iface, Datagram msg) throws Exception {
		System.out.println(  ((int) (host.getNetwork().getScheduler().getCurrentTime() * 1000)) + "ms " + host.name + " " + iface + " " + msg  );
		if( msg.getPayload() instanceof HELLOMessage ) {
			HELLOMessage m = (HELLOMessage) msg.getPayload();
			
			if( !table.containsKey(m.GetOrigin()) ) {
				if( !m.contains(iface.getAddress()) ) {
					m.Add( m.GetOrigin() );
					m.SetOrigin( iface.getAddress() );
				}
				else {
					table.put( iface.getAddress(), m);
				}
				
				//Datagram dm = new Datagram(iface.getAddress(), msg.src, IP_PROTO_LSP, 1, m);
				//iface.send(dm, msg.src);
				Datagram dm = new Datagram(iface.getAddress(), IPAddress.BROADCAST, IP_PROTO_LSP, 1, m);
				iface.send(dm, null);
			}
			else {
				System.out.println(host.name + " ==> "+ table);
			}
		}
	}

	@Override
	public void attrChanged(Interface iface, String attr) {
		System.out.println("attribute \"" + attr + "\" changed on interface \"" + iface + "\" : " + iface.getAttribute(attr));
	}
	
		
}
