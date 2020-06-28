package it.polito.tdp.newufosightings.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.newufosightings.db.NewUfoSightingsDAO;

public class Model {
	
	private NewUfoSightingsDAO dao;
	private List<String> forme;
	
	//grafo semplice, pesato, non orientato
	private Graph<State, DefaultWeightedEdge> grafo;
	private List<State> stati;
	private Map<String, State> mappa;
	private List<Collegamento> collegamenti;
	
	public Model() {
		this.dao = new NewUfoSightingsDAO();
	}
	
	public List<String> prendiForme(int anno){
		this.forme = new ArrayList<>(this.dao.prendiForme(anno));
		return this.forme;
	}

	public void creaGrafo(String forma, int anno) {
		this.stati = this.dao.loadAllStates();
		this.mappa = new HashMap<>();
		
		for(State s: this.stati) {
			mappa.put(s.getId(), s);
		}
		System.out.println("PRIMO: "+this.stati.get(0).getName());
		
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//VERTICI
		Graphs.addAllVertices(this.grafo, this.stati);
		
		//ARCHI
		this.collegamenti = new ArrayList<>(this.dao.prendiCollegamenti(forma, anno, this.mappa));
		System.out.println("Stampa mappa: "+this.mappa.get("AL"));
		System.out.println("Stampa colleagamento: "+this.collegamenti.get(0).getStat1());
		
		for(Collegamento col : this.collegamenti) {
			for(State s: this.stati) {
				String vicini = s.getNeighbors();
				if( vicini != null && (col.getStat1().equals(s) && vicini.contains(col.getStat2().getId())) ) {
					Graphs.addEdge(this.grafo, col.getStat1(), col.getStat2(), col.getPeso());
				}
			}
		}
	}
	
	public int numeroVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int numeroArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<State> elencoStati(){
		return this.stati;
	}
	
	public String pesiAdiacenti(State stato) {
		int somma = 0;
		for(State s: Graphs.neighborListOf(this.grafo, stato)) {
			DefaultWeightedEdge arco = this.grafo.getEdge(stato, s);
			int peso = (int) this.grafo.getEdgeWeight(arco);
			somma += peso;
		}
		
		return stato.getName() + ": somma dei pesi adiacenti " + somma;
	}
}
