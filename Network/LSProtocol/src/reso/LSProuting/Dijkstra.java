package reso.LSProuting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import reso.ip.IPAddress;


public class Dijkstra {
	public Map<IPAddress, Link> graph;
	IPAddress src;
	
	public Dijkstra(IPAddress src, Map<IPAddress, LSDBEntry> LSDB) {
		this.src = src;
		this.graph = new HashMap<IPAddress, Link>(LSDB.size());
		
		IPAddress n;
		Adjacence[] LS;
		
		// Ajout des noeuds
		for( Map.Entry<IPAddress, LSDBEntry> i : LSDB.entrySet()) {
			n = i.getKey();
			graph.put(n, new Link(n));
		}
		// Ajout des voisins
		for( Map.Entry<IPAddress, LSDBEntry> i : LSDB.entrySet()) {
			n = i.getKey();
			LS = i.getValue().message.getAdjacence();
			if( LS == null ) return; // Notre LSDB n'est pas encore cr�e, inutile de tester dijstra
			Link k = graph.get(n);
			
			for(int j=0; j<LS.length; j++) {
				// Si le cout du lien est valide et que
				// Le voisin a �t� d�tect�, mais le lien en face a �t� coup� apr�s la d�tection:
				if( LS[j].cost >= 0 && LS[j].cost <= Integer.MAX_VALUE && LSDB.containsKey(LS[j].routeID) && LSDB.get(LS[j].routeID).message.contains(n) ) {
					k.voisin.put( graph.get(LS[j].routeID), LS[j].cost);
				}
			}
		}
		
		
		
		Compute();
	}
	private int addMetric(int m1, int m2) {
		if (((long) m1) + ((long) m2) > Integer.MAX_VALUE)
			return Integer.MAX_VALUE;
		return m1 + m2;
	}
	private void Compute() {
		PriorityQueue<Link> q = new PriorityQueue<Link>();		
		Link u, v, source = graph.get(src);
		int alt;
		
		for( Link w : graph.values() ) {
			if( w == source ) {
				w.prev = source;
				w.dist = 0;
			}
			else {
				w.prev = null;
				w.dist = Integer.MAX_VALUE;
			}
			q.add(w);
		}
		
		while( !q.isEmpty() ) {
			// Le noeud le plus petit.
			u = q.poll();
			
			if( u.dist == Integer.MAX_VALUE )
				break; // Notre graph est coup� en deux... Lien d'un coup infini? Oui.
	 
			// Pour chaque voisin du noeud marqu�
			for( Map.Entry<Link, Integer> a : u.voisin.entrySet() ) {
				v = a.getKey();
				if( v == null )
					break; // Notre graph est coup� en deux... Pas encore re�u toute la LSDB.
				
				alt = u.dist + a.getValue();
				alt = addMetric(u.dist, a.getValue());
				
				if( alt < v.dist ) {
					// Il existe un chemin plus court
	            	q.remove(v);
	            	v.dist = alt;
	            	v.prev = u;
	            	q.add(v);
				} 
			}
		}
	}
	public ArrayList<Adjacence> GetPathTo( IPAddress dst ) {
		ArrayList<Adjacence> path = new ArrayList<Adjacence>();
		Link elem = graph.get(dst);
		
		while( elem.prev != null && elem.prev != elem ) {
			path.add(new Adjacence(elem.src, elem.dist));
			elem = elem.prev;
		}
		return path;
	}
	public void GetAllPath( ) {
		for(Link f:graph.values()){
			System.out.println("A " + f.src);
			ArrayList<Adjacence> path = new ArrayList<Adjacence>();
			Link elem = graph.get(f.src);
			
			while( elem.prev != null && elem.prev != elem ) {
				path.add(new Adjacence(elem.src, elem.dist));
				elem = elem.prev;
			}
			System.out.println(path);
		}
	}
	
	public static class Link implements Comparable<Link> {
		public IPAddress src;
		public int dist = Integer.MAX_VALUE;
		public Link prev = null;
		public Map<Link, Integer> voisin = new HashMap<Link, Integer>();
		
		public Link(IPAddress src) {
			this.src = src;
		}
		public int compareTo(Link dst) {
			return Integer.compare(dist, dst.dist);
		}
		public String toString() {
			String str;
			
			if (this == this.prev)
	            str = this.src.toString();
	        else if (this.prev == null)
	        	str = this.src.toString() + "(non joignable)";
	        else
	        	str = this.prev.toString() + " -> " + this.src;

			return str;
		}
	}
}
