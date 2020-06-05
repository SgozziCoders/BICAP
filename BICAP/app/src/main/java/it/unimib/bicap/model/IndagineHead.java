package it.unimib.bicap.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class IndagineHead implements Parcelable{

	private String titoloIndagine;
	private String erogatore;
	private String imgUrl;
	private int id;
	private String ultimaModifica;
	private Date dataDiScadenza;
	private boolean indagineInCorso;

	public IndagineHead() {}
	
	/**
	* @brief constructor IndagineHead
	* @param titoloIndagine
	* @param erogoatore
	* @param imgUrl
	* @param id
	*/
	public IndagineHead(String titoloIndagine, String erogatore, String imgUrl, int id, String ultimaModifica) {
		super();
		this.titoloIndagine = titoloIndagine;
		this.erogatore = erogatore;
		this.imgUrl = imgUrl;
		this.id = id;
		this.ultimaModifica = ultimaModifica;
	}

	protected IndagineHead(Parcel in) {
		titoloIndagine = in.readString();
		erogatore = in.readString();
		imgUrl = in.readString();
		id = in.readInt();
		ultimaModifica = in.readString();
		indagineInCorso = in.readByte() != 0;
	}

	public static final Creator<IndagineHead> CREATOR = new Creator<IndagineHead>() {
		@Override
		public IndagineHead createFromParcel(Parcel in) {
			return new IndagineHead(in);
		}

		@Override
		public IndagineHead[] newArray(int size) {
			return new IndagineHead[size];
		}
	};

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
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUltimaModifica() {
		return ultimaModifica;
	}

	public void setUltimaModifica(String ultimaModifica) {
		this.ultimaModifica = ultimaModifica;
	}

	public boolean isIndagineInCorso() {
		return indagineInCorso;
	}

	public void setIndagineInCorso(boolean indagineInCorso) {
		this.indagineInCorso = indagineInCorso;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(titoloIndagine);
		dest.writeString(erogatore);
		dest.writeString(imgUrl);
		dest.writeInt(id);
		dest.writeString(ultimaModifica);
		dest.writeByte((byte) (indagineInCorso ? 1 : 0));
	}
}
