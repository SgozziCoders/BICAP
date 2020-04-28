package com.example.bicap_model_test;
public class Istruzione {

	private String nomeFile;
	private String fileUrl;
	private String tipoFile;
	private String thumbnailUrl;

	public Istruzione() {
	}

	/**
	* @brief constructor Istruzione
	* @param nomeFile
	* @param tipoFile
	* @param fileUrl
	*/
	public Istruzione(String nomeFile, String fileUrl, String tipoFile, String thumbnailUrl) {
		super();
		this.nomeFile = nomeFile;
		this.fileUrl = fileUrl;
		this.tipoFile = tipoFile;
		this.thumbnailUrl = thumbnailUrl;
	}
	
	public String getNomeFile() {
		return nomeFile;
	}
	
	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}
	
	
	public String getFileUrl() {
		return fileUrl;
	}
	
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	
	public String getTipoFile() {
		return tipoFile;
	}
	
	public void setTipoFile(String tipoFile) {
		this.tipoFile = tipoFile;
	}

	public String getThumbnailUrl(){
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl){
		this.thumbnailUrl = thumbnailUrl;
	}

}