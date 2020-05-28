package it.unimib.bicap.model;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class IndaginiHeadList implements Parcelable {

	private List<IndagineHead> indagine;

	public IndaginiHeadList() {}

	/**
	* @brief constructor IndaginiHeadList
	* @param indagine
	*/
	public IndaginiHeadList(List<IndagineHead> indagine) {
		super();
		this.indagine = indagine;
	}

	protected IndaginiHeadList(Parcel in) {
		indagine = in.createTypedArrayList(IndagineHead.CREATOR);
	}

	public static final Creator<IndaginiHeadList> CREATOR = new Creator<IndaginiHeadList>() {
		@Override
		public IndaginiHeadList createFromParcel(Parcel in) {
			return new IndaginiHeadList(in);
		}

		@Override
		public IndaginiHeadList[] newArray(int size) {
			return new IndaginiHeadList[size];
		}
	};

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

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeTypedList(indagine);
	}
}