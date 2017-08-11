package com.doctor.sun.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by heky on 17/5/12.
 */

public class ISOpen{
    public boolean network;
    public boolean simple;
    public boolean surface;

    public boolean isNetwork() {
        return network;
    }

    public void setNetwork(boolean network) {
        this.network = network;
    }

    public boolean isSurface() {
        return surface;
    }

    public void setSurface(boolean surface) {
        this.surface = surface;
    }

    public boolean isSimple() {
        return simple;
    }

    public void setSimple(boolean simple) {
        this.simple = simple;
    }

    @Override
    public String toString() {
        return "simple:"+simple+",surface:"+surface+",net:"+network;
    }

    /*
    @Override
    public int describeContents() {
        return 0;
    }

    public ISOpen (Parcel i){
        this.simple = i.readByte()!=0;
        this.surface = i.readByte()!=0;
        this.network = i.readByte()!=0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte)(this.network?1:0));
        dest.writeByte((byte)(this.simple?1:0));
        dest.writeByte((byte)(this.surface?1:0));
    }

    public Creator<ISOpen>CREATOR = new Creator<ISOpen>() {
        @Override
        public ISOpen createFromParcel(Parcel source) {
            return new ISOpen(source);
        }

        @Override
        public ISOpen[] newArray(int size) {
            return new ISOpen[0];
        }
    };*/
}
