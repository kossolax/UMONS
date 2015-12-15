package reso.LSProuting;

import java.util.ArrayList;
import java.util.List;

import reso.common.Message;
import reso.examples.dv_routing.DVMessage.DV;
import reso.ip.IPAddress;
import reso.ip.IPInterfaceAdapter;

public class LSPMessage implements Message {

	
	class LSDB {
		public IPAddress ip;
		public long length;
		
		public LSDB(IPAddress ip, long length) {
			this.ip = ip;
			this.length = length;
		}
		public String toString() {
			return "LSP["+ip+"@"+length+"]";
		}
	};
	
	public final List<LSDB> lsdb;
	
	public LSPMessage() {
		lsdb = new ArrayList<LSDB>();
	}
	public boolean add(IPAddress ip, long length) {
		for( LSDB lsp : lsdb ) {
			if( lsp.ip == ip )
				return false;
		}
		lsdb.add(new LSDB(ip, length));
		return true;
	}
	
	public String toString() {
		String s= "LSDB[ ";
		for( LSDB lsp : lsdb )
			s += lsp + ",";
		
		return s+"] ";
	}

}
