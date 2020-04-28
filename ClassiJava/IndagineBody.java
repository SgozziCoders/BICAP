package com.example.bicap_model_test;
import java.util.List;


public class IndagineBody {

	private IndagineHead head;
	private String tematica;
	private List<Istruzione> istruzioni;
	private List<Questionario> questionari;

	public IndagineBody() {
	}

	/**
	* @brief constructor IndagineBody
	* @param tematica
	* @param istruzioni
	* @param questionari
	*/
	public IndagineBody(String tematica, List<Istruzione> istruzioni, List<Questionario> questionari) {
		super();
		this.head = null;
		this.tematica = tematica;
		this.istruzioni = istruzioni;
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

	public List<Istruzione> getIstruzioni() {
		return istruzioni;
	}
	
	public void setIstruzioni(List<Istruzione> istruzioni) {
		this.istruzioni = istruzioni;
	}
	
	public List<Questionario> getQuestionari() {
		return questionari;
	}
	
	public void setQuestionari(List<Questionario> questionari) {
		this.questionari = questionari;
	}
}