package reso.LSProuting;

import reso.common.Message;
import reso.ip.IPAddress;

public class LSMessage implements Message {
	
	private IPAddress origin;
	private int seqID;
	private byte length;
	private Adjacence[] LSDB;
	
	public LSMessage(IPAddress ip, int seqID) {
		this.origin = ip;
		this.length = 0;
		this.seqID = seqID;
	}
	public void Add(Adjacence adj) {
		Adjacence[] buffer = new Adjacence[length+1];
		for(int i=0; i<length; i++) {
			buffer[i] = LSDB[i];
		}
		buffer[length] = adj;
		length++;
		LSDB = buffer;
	}
	public IPAddress getOrigin() {
		return origin;
	}
	public int getSequenceID() {
		return seqID;
	}
	public Adjacence[] getAdjacence() {
		return LSDB;
	}
	public String toString() {
		String res = "";
		res += "[LSP SeqID: "+seqID+" From: "+ origin;
		
		if( length > 0 ) {
			res += " LS: (";
			for(int i=0; i<length; i++)
				res += LSDB[i]+",";
				
			res = res.substring(0,  res.length() - 1) +")";
		}
		res += "]";
		
		return res;
	}

}
