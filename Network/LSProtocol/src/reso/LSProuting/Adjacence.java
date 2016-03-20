package reso.LSProuting;

import reso.ip.IPAddress;

public class Adjacence {
	public IPAddress routeID;
	public int cost;
	
	public Adjacence(IPAddress routeID, int cost) {
		this.routeID = routeID;
		this.cost = cost;
	}
	
	public String toString() {
		return routeID + ":"+cost;
	}
}