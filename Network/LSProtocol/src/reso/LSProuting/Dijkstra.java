package reso.LSProuting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;

import reso.ip.IPAddress;


public class Dijkstra {
	public Map<IPAddress, Link> graph;
	IPAddress src;
	
	
	public Dijkstra(IPAddress src, Map<IPAddress, LSMessage> LSDB) {
		this.src = src;
		this.graph = new HashMap<IPAddress, Link>(LSDB.size());
		
		IPAddress n;
		Adjacence[] LS;
		
		// Ajout des noeuds
		for( Map.Entry<IPAddress, LSMessage> i : LSDB.entrySet()) {
			n = i.getKey();
			LS = i.getValue().getAdjacence();
			
			for(int j=0; j<LS.length; j++) {
				graph.put(n, new Link(n));
			}
		}
		// Ajout des voisins
		for( Map.Entry<IPAddress, LSMessage> i : LSDB.entrySet()) {
			n = i.getKey();
			LS = i.getValue().getAdjacence();
			
			for(int j=0; j<LS.length; j++) {
				graph.get(n).voisin.put( graph.get(LS[j].routeID), LS[j].cost);
				System.out.println(n + " -> " + LS[j].routeID + " cout : " + LS[j].cost );
			}
		}
		
		Compute();
	}
	private void Compute() {
		NavigableSet<Link> q = new TreeSet<>();		
		Link u, v, source = graph.get(src);
		System.out.println(src);
		System.out.println(source.src);
		
		if( src != source.src ) {
			throw new AssertionError("Erreur fatal... Les noeuds sont mal inséré :(");
		}
		
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
		int min;
		
		while( !q.isEmpty() ) {
			// Le noeud le plus petit.
			u = q.pollFirst();
			System.out.println(u.src + "  " + u.dist);
			if( u.dist == Integer.MAX_VALUE )
				break; // Note graph est coupé en deux... Pas encore reçu toute la LSDB? Possible. Lien d'un coup infini? Oui.
	 
			// Pour chaque voisin du noeud marqué
			for( Map.Entry<Link, Integer> a : u.voisin.entrySet() ) {
				v = a.getKey();
	 
				min = u.dist + a.getValue();
				if (min < v.dist) {
					// Il existe un chemin plus court
	            	q.remove(v);
	            	v.dist = min;
	            	v.prev = u;
	            	q.add(v);
				} 
			}
		}
	}
	public ArrayList<IPAddress> GetPathTo( IPAddress dst ) {
		ArrayList<IPAddress> path = new ArrayList<IPAddress>();
		Link elem = graph.get(dst);
		
		while( elem.prev != null && elem.prev != elem ) {
			path.add(elem.src);
			elem = elem.prev;
		}
		return path;
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
	}
}
