package com.example.bicap_model_test;
public class IndagineHead {

	private String titoloIndagine;
	private String erogatore;
	private String imgUrl;
	private int idIndagine;
	
	public IndagineHead() {
	}
	
	/**
	* @brief constructor IndagineHead
	* @param titoloIndagine
	* @param erogoatore
	* @param imgUrl
	* @param id
	*/
	public IndagineHead(String titoloIndagine, String erogatore, String imgUrl, int idIndagine) {
		super();
		this.titoloIndagine = titoloIndagine;
		this.erogatore = erogatore;
		this.imgUrl = imgUrl;
		this.idIndagine = idIndagine;
	}

	public String getTitoloIndagine() {
		return titoloIndagine;
	}
	
	public void setTitoloIndagine(String titoloIndagine) {
		this.titoloIndagine = titoloIndagine;
	}
	
	public String getErogatore() {
		return erogatore;
	}
	
	public void setErogatore(String erogatore) {
		this.erogatore = erogatore;
	}
	
	public String getImgUrl() {
		return imgUrl;
	}
	
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	
	public int getId() {
		return idIndagine;
	}

	public void setId(int id) {
		this.idIndagine = id;
	}

}
