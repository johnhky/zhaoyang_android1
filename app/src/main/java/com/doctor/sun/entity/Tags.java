package com.doctor.sun.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by rick on 24/8/2016.
 */

public class Tags implements Parcelable {

    /**
     * tag_id : 1472008685NhaDrwJUWY
     * tag_name : null
     */

    @JsonProperty("tag_id")
    public String tagId;
    @JsonProperty("tag_name")
    public String tagName;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tagId);
        dest.writeString(this.tagName);
    }

    public Tags() {
    }

    protected Tags(Parcel in) {
        this.tagId = in.readString();
        this.tagName = in.readString();
    }

    public static final Parcelable.Creator<Tags> CREATOR = new Parcelable.Creator<Tags>() {
        @Override
        public Tags createFromParcel(Parcel source) {
            return new Tags(source);
        }

        @Override
        public Tags[] newArray(int size) {
            return new Tags[size];
        }
    };

    @Override
    public String toString() {
        return tagName;
    }
}
