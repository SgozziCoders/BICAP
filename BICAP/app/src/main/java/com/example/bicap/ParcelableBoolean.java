package com.example.bicap;

import android.os.Parcel;
import android.os.Parcelable;

public class ParcelableBoolean implements Parcelable {
    Boolean key;

    public ParcelableBoolean(Boolean key){
        this.key = key;
    }

    protected ParcelableBoolean(Parcel in) {
        byte tmpKey = in.readByte();
        key = tmpKey == 0 ? null : tmpKey == 1;
    }

    public static final Creator<ParcelableBoolean> CREATOR = new Creator<ParcelableBoolean>() {
        @Override
        public ParcelableBoolean createFromParcel(Parcel in) {
            return new ParcelableBoolean(in);
        }

        @Override
        public ParcelableBoolean[] newArray(int size) {
            return new ParcelableBoolean[size];
        }
    };

    public Boolean getKey() {
        return key;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (key == null ? 0 : key ? 1 : 2));
    }
}
