package com.example.bicap_model_test;
import java.util.List;

public class Questionario {

	private String titolo;
	private String qualtricsUrl;
	private boolean compilato;
	private List<Informazione> informazioni;

	public Questionario() {
	}

	/**
	* @brief constructor Questionario
	* @param titolo
	* @param qualtricsUrl
	* @param compilato
	*/
	public Questionario(String titolo, String qualtricsUrl, List<Informazione> informazioni) {
		super();
		this.titolo = titolo;
		this.qualtricsUrl = qualtricsUrl;
		this.informazioni = informazioni;
	}
	
	public String getTitolo() {
		return titolo;
	}
	
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	
	public String getQualtricsUrl() {
		return qualtricsUrl;
	}

	public void setQualtricsUrl(String qualtricsUrl) {
		this.qualtricsUrl = qualtricsUrl;
	}

	public boolean isCompilato() {
		return compilato;
	}

	public void setCompilato(boolean compilato) {
		this.compilato = compilato;
	}

	public List<Informazione> getInformazioni() {
		return informazioni;
	}
	
	public void setInformazioni(List<Informazione> informazioni) {
		this.informazioni = informazioni;
	}

}