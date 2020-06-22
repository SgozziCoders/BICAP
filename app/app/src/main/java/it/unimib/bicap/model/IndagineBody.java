package it.unimib.bicap.model;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;


public class IndagineBody implements Parcelable {

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

	protected IndagineBody(Parcel in) {
		head = in.readParcelable(IndagineHead.class.getClassLoader());
		tematica = in.readString();
	}

	public static final Creator<IndagineBody> CREATOR = new Creator<IndagineBody>() {
		@Override
		public IndagineBody createFromParcel(Parcel in) {
			return new IndagineBody(in);
		}

		@Override
		public IndagineBody[] newArray(int size) {
			return new IndagineBody[size];
		}
	};

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

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(head, flags);
		dest.writeString(tematica);
	}
}