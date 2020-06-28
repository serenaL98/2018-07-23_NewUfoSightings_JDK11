package it.polito.tdp.newufosightings.model;

public class Collegamento {
	
	private State stat1;
	private State stat2;
	private Integer peso;
	
	public Collegamento(State stat1, State stat2, Integer peso) {
		super();
		this.stat1 = stat1;
		this.stat2 = stat2;
		this.peso = peso;
	}
	
	public State getStat1() {
		return stat1;
	}
	public void setStat1(State stat1) {
		this.stat1 = stat1;
	}
	public State getStat2() {
		return stat2;
	}
	public void setStat2(State stat2) {
		this.stat2 = stat2;
	}
	public Integer getPeso() {
		return peso;
	}
	public void setPeso(Integer peso) {
		this.peso = peso;
	}
	
	

}
