package it.polito.tdp.newufosightings.model;

public class Evento {

	private Integer giorno;
	private State stato;
	private EventType tipo;
	
	public enum EventType{
		INIZIO, FINE
	}

	public Evento(Integer giorno, State stato, EventType tipo) {
		super();
		this.giorno = giorno;
		this.stato = stato;
		this.tipo = tipo;
	}

	public Integer getGiorno() {
		return giorno;
	}

	public void setGiorno(Integer giorno) {
		this.giorno = giorno;
	}

	public State getStato() {
		return stato;
	}

	public void setStato(State stato) {
		this.stato = stato;
	}

	public EventType getTipo() {
		return tipo;
	}

	public void setTipo(EventType tipo) {
		this.tipo = tipo;
	}
	
	
}
