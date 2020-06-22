package com.example.bicap_model_test;
import java.util.List;

public class IndaginiHeadList {

	private List<IndagineHead> indagine;

	public IndaginiHeadList() {
	}

	/**
	* @brief constructor IndaginiHeadList
	* @param indagine
	*/
	public IndaginiHeadList(List<IndagineHead> indagine) {
		super();
		this.indagine = indagine;
	}
	
	public List<IndagineHead> getHeads() {
		return indagine;
	}
	
	public void setHeads(List<IndagineHead> indagine) {
		this.indagine = indagine;
	}

	public IndagineHead getIndagineHeadFromId(int id){
		for (IndagineHead i: indagine) {
			if(i.getId() == id)
				return i;
		}
		return null;
	}
}