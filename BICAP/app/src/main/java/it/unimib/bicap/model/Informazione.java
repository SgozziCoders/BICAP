package it.unimib.bicap.model;
public class Informazione {

	private String nomeFile;
	private String fileUrl;
	private String tipoFile;
	private String thumbnailUrl;
	private String ultimaModifica;

	public Informazione() {
	}

	/**
	* @brief constructor Istruzione
	* @param nomeFile
	* @param tipoFile
	* @param fileUrl
	*/
	public Informazione(String nomeFile, String fileUrl, String tipoFile, String thumbnailUrl, String ultimaModifica) {
		super();
		this.nomeFile = nomeFile;
		this.fileUrl = fileUrl;
		this.tipoFile = tipoFile;
		this.thumbnailUrl = thumbnailUrl;
		this.ultimaModifica = ultimaModifica;
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

	public String getultimaModifica() {
		return ultimaModifica;
	}

	public void setEultimaModifica(String ultimaModifica) {
		this.ultimaModifica = ultimaModifica;
	}
}