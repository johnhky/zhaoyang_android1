package com.doctor.sun.entity;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.doctor.sun.R;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by rick on 16/8/2016.
 */

public class Advice implements LayoutId, Parcelable {
    public static final String TYPE_SENT = "sent";
    public static final String TYPE_REPLIED = "replied";

    /**
     * created_at : 2016-08-15 03:14:29
     * feedback : 多读读
     * id : 15
     * reply :
     * type : sent
     * updated_at : 2016-08-15 03:14:29
     */

    @JsonProperty("created_at")
    public String createdAt;
    @JsonProperty("feedback")
    public String feedback;
    @JsonProperty("id")
    public String id;
    @JsonProperty("reply")
    public String reply;
    @JsonProperty("type")
    public String type;
    @JsonProperty("updated_at")
    public String updatedAt;

    public String getTypeText(String type) {
        if (type.equals(TYPE_SENT)) {
            return "意见已发送";
        } else {
            return "系统已回复";
        }
    }

    public int getTypeTextColor(Context context, String type) {
        if (type.equals(TYPE_SENT)) {
            return context.getResources().getColor(R.color.orange);
        } else {
            return context.getResources().getColor(R.color.green);
        }
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_advice;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.createdAt);
        dest.writeString(this.feedback);
        dest.writeString(this.id);
        dest.writeString(this.reply);
        dest.writeString(this.type);
        dest.writeString(this.updatedAt);
    }

    public Advice() {
    }

    protected Advice(Parcel in) {
        this.createdAt = in.readString();
        this.feedback = in.readString();
        this.id = in.readString();
        this.reply = in.readString();
        this.type = in.readString();
        this.updatedAt = in.readString();
    }

    public static final Parcelable.Creator<Advice> CREATOR = new Parcelable.Creator<Advice>() {
        @Override
        public Advice createFromParcel(Parcel source) {
            return new Advice(source);
        }

        @Override
        public Advice[] newArray(int size) {
            return new Advice[size];
        }
    };
}
