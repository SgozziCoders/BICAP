public class Questionario {

	private String titolo;
	private String qualtricsUrl;
	private boolean compilato;

	public Questionario() {
	}

	/**
	* @brief constructor Questionario
	* @param titolo
	* @param qualtricsUrl
	* @param compilato
	*/
	public Questionario(String titolo, String qualtricsUrl) {
		super();
		this.titolo = titolo;
		this.qualtricsUrl = qualtricsUrl;
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


}