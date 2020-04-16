
import java.util.List;


public class IndagineBody {

	private IndagineHead head;
	private String descrizione;
	private List<Istruzione> istruzioni;
	private List<Questionario> questionari;

	public IndagineBody() {
	}

	/**
	* @brief constructor IndagineBody
	* @param descrizione
	* @param istruzioni
	* @param questionari
	*/
	public IndagineBody(String descrizione, List<Istruzione> istruzioni, List<Questionario> questionari) {
		super();
		this.head = null;
		this.descrizione = descrizione;
		this.istruzioni = istruzioni;
		this.questionari = questionari;
	}
	
	public IndagineHead getHead() {
		return head;
	}

	public void setHead(IndagineHead head) {
		this.head = head;
	}
	
	public String getDescrizione() {
		return descrizione;
	}
	
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
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