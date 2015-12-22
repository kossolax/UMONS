package reso.LSProuting;

import reso.ip.IPAddress;

public class Adjacence {
	public IPAddress routeID;
	public int cost;
	public double time;
	
	public Adjacence(IPAddress routeID, int cost) {
		this.routeID = routeID;
		this.cost = cost;
	}
	public Adjacence(IPAddress routeID, int cost, double time) {
		this.routeID = routeID;
		this.cost = cost;
		this.time = time;
	}
	public String toString() {
		return routeID + ":"+cost;
	}
}