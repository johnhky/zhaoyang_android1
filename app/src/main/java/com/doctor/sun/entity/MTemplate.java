package com.doctor.sun.entity;

import android.os.Parcel;

import com.doctor.sun.R;

/**
 * Created by lucas on 12/4/15.
 */
public class MTemplate extends QTemplate{

    @Override
    public int getItemLayoutId() {
        return R.layout.item_template;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);

    }

    public MTemplate() {
    }

    protected MTemplate(Parcel in) {
        super(in);

    }

    public static final Creator<MTemplate> CREATOR = new Creator<MTemplate>() {
        public MTemplate createFromParcel(Parcel source) {
            return new MTemplate(source);
        }

        public MTemplate[] newArray(int size) {
            return new MTemplate[size];
        }
    };
}
