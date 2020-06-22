package com.example.bicap_model_test;
import java.util.List;


public class IndagineBody {

	private IndagineHead head;
	private String tematica;
	private List<Informazione> informazioni;
	private List<Questionario> questionari;

	public IndagineBody() {
	}

	/**
	* @brief constructor IndagineBody
	* @param tematica
	* @param informazioni
	* @param questionari
	*/
	public IndagineBody(String tematica, List<Informazione> informazioni, List<Questionario> questionari) {
		super();
		this.head = null;
		this.tematica = tematica;
		this.informazioni = informazioni;
		this.questionari = questionari;
	}
	
	public IndagineHead getHead() {
		return head;
	}

	public void setHead(IndagineHead head) {
		this.head = head;
	}
	
	public String getTematica() {
		return tematica;
	}
	
	public void setTematica(String tematica) {
		this.tematica = tematica;
	}

	public List<Informazione> getInformazioni() {
		return informazioni;
	}
	
	public void setInformazioni(List<Informazione> informazioni) {
		this.informazioni = informazioni;
	}
	
	public List<Questionario> getQuestionari() {
		return questionari;
	}
	
	public void setQuestionari(List<Questionario> questionari) {
		this.questionari = questionari;
	}
}