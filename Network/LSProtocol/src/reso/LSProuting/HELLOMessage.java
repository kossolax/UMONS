package reso.LSProuting;

import reso.common.Message;
import reso.ip.IPAddress;

public class HELLOMessage implements Message {

	private IPAddress origin;
	private byte length;
	private IPAddress[] router;
	
	public HELLOMessage(IPAddress ip) {
		this.origin = ip;
		this.length = 0;
	}
	public void Add(IPAddress ip) {
		IPAddress[] buffer = new IPAddress[length+1];
		for(int i=0; i<length; i++) {
			buffer[i] = router[i];
		}
		buffer[length] = ip;
		length++;
		router = buffer;
	}
	public IPAddress GetOrigin() {
		return this.origin;
	}
	public void SetOrigin(IPAddress ip) {
		this.origin = ip;
	}
	public byte length() {
		return length;
	}
	public boolean contains(IPAddress ip) {
		for(int i=0; i<length; i++) {
			if( router[i] == ip )
				return true;
		}
		return false;
	}
	
	public String toString() {
		String res = "";
		res += "[HELLO From: "+ origin;
		
		if( length > 0 ) {
			res += " Voisin: (";
			for(int i=0; i<length; i++)
				res += router[i]+",";
				
			res = res.substring(0,  res.length() - 1) +")";
		}
		res += "]";
		
		return res;
	}
	
}
