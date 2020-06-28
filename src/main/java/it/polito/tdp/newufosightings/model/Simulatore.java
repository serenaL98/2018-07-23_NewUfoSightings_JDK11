package it.polito.tdp.newufosightings.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.newufosightings.model.Evento.EventType;

public class Simulatore {
	
	//INPUT
	private int T1;
	private int alfa;
	private Graph<State, DefaultWeightedEdge>grafo;
	
	//OUTPUT
	private Map<State, Float> statoDefcon;
	
	//CODA DEGLI EVENTI
	private PriorityQueue<Evento> coda;
	private int giorniFissi;
	
	//MODELLO DEL MONDO
	private List<State> rimanenti;
	
	//SIMULAZIONE
	public void inizio(int t1, int alfa, Graph<State, DefaultWeightedEdge>grafo) {
		this.T1 = t1;
		this.giorniFissi = t1;
		this.alfa = alfa;
		this.grafo = grafo;
		this.statoDefcon = new HashMap<>();
		this.rimanenti = new ArrayList<>(this.grafo.vertexSet());
		
		this.coda = new PriorityQueue<>();
		
		//tutti gli stati si trovano a livello 5
		for(State s: this.grafo.vertexSet()) {
			statoDefcon.put(s, (float) 5);
		}
		
		//aggiungo il primo evento	???
	}
	
	public void avvia() {
		while(!coda.isEmpty()) {
			Evento e = coda.poll();
			processEvent(e);
		}
	}
	
	public void processEvent(Evento e) {
		
		if(rimanenti.size() == 0) {
			
			switch(e.getTipo()) {
			
			case INIZIO:
				//decremento il defcon dello stato
				for(State s: this.grafo.vertexSet()) {
					if(e.getStato().equals(s)) {
						this.statoDefcon.replace(s, statoDefcon.get(s)-1);
					}
				}
				Double prob = Math.random()*100;
				
				
				//incremento di 0.5 gli adiacenti
				List<State> adiacenti = Graphs.neighborListOf(this.grafo, e.getStato());
				for(State st: adiacenti) {
					if(this.statoDefcon.containsKey(st)) {
						if(prob<=this.alfa) {
							statoDefcon.replace(st, (float) (statoDefcon.get(st)-0.5));
						}
					}
				}
				
				T1--;
				
				//se arrivo a T1 = 0 arrivo alla fine
				if(T1 == 0) {
					this.coda.add(new Evento(e.getGiorno()+1, e.getStato(), EventType.FINE));
				}
				
				break;
				
			case FINE:
				//quando arrivo alla fine ricomincio incrementando con la stessa prob i prima
				T1 = this.giorniFissi;
				
				Double probabi = Math.random();
				if(statoDefcon.containsKey(e.getStato())) {
					statoDefcon.replace(e.getStato(), statoDefcon.get(e.getStato())+1);
				}

				List<State> vicini = Graphs.neighborListOf(this.grafo, e.getStato());
				for(State st: vicini) {
					if(this.statoDefcon.containsKey(st)) {
						if(probabi<=this.alfa) {
							statoDefcon.replace(st, (float) (statoDefcon.get(st)+0.5));
						}
					}
				}
				
				break;
				
			}
			
		}
		
	}
	
	//STAMPA OUTPUT
	public String stampaStatoDefcon() {
		String stampa = "";
		for(State s: this.statoDefcon.keySet()) {
			stampa += s.getName() +" "+statoDefcon.get(s)+"\n";
		}
		return stampa;
	}
}
