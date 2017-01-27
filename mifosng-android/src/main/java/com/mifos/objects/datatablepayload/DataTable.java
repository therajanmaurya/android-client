package com.mifos.objects.datatablepayload;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

/**
 * Created by Tarun on 1/28/2017.
 */
public class DataTable implements Parcelable {

    @SerializedName("registeredTableName")
    String registeredTableName;

    @SerializedName("data")
    HashMap<String, Object> data;

    public String getRegisteredTableName() {
        return registeredTableName;
    }

    public void setRegisteredTableName(String registeredTableName) {
        this.registeredTableName = registeredTableName;
    }

    public HashMap<String, Object> getData() {
        return data;
    }

    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.registeredTableName);
        dest.writeSerializable(this.data);
    }

    public DataTable() {
    }

    protected DataTable(Parcel in) {
        this.registeredTableName = in.readString();
        this.data = (HashMap<String, Object>) in.readSerializable();
    }

    public static final Parcelable.Creator<DataTable> CREATOR = new
            Parcelable.Creator<DataTable>() {
        @Override
        public DataTable createFromParcel(Parcel source) {
            return new DataTable(source);
        }

        @Override
        public DataTable[] newArray(int size) {
            return new DataTable[size];
        }
    };
}
