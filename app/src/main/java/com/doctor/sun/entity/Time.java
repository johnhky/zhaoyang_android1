package com.doctor.sun.entity;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;

import com.doctor.sun.BR;
import com.doctor.sun.R;
import com.doctor.sun.entity.handler.TimeHandler;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * Created by lucas on 12/1/15.
 */
public class Time extends BaseObservable implements LayoutId, Parcelable {


    public static final int TYPE_UNDEFINE = 0;
    public static final int TYPE_DETAIL = 1;
    public static final int TYPE_QUICK = 2;
    public static final int TYPE_BREAK = 3;
    /**
     * doctor_id : 1
     * week : 16
     * type : 1
     * from : 10:00:00
     * to : 23:00:00
     * updated_at : 2015-08-10 17:49:58
     * created_at : 2015-08-10 17:49:58
     * id : 9
     */

    @JsonProperty("id")
    private int id = 0;
    @JsonProperty("doctor_id")
    private int doctorId;
    @JsonProperty("week")
    private int week;
    @Type
    @JsonProperty("type")
    private int type = TYPE_DETAIL;
    @JsonProperty("from")
    private String from;
    @JsonProperty("to")
    private String to;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("created_at")
    private String createdAt;


    private TimeHandler handler = new TimeHandler(this);
    /**
     * reserva : 0
     * optional : 0
     */

    @JsonProperty("reserva")
    private int reserva;
    @JsonProperty("optional")
    private int optional;

    public TimeHandler getHandler() {
        return handler;
    }

    public void setHandler(TimeHandler timeHandler) {
        this.handler = timeHandler;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public void setWeek(int weekLabel) {
        this.week = weekLabel;
    }

    public void setType(int type) {
        this.type = type;
        notifyPropertyChanged(BR.type);
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public int getWeek() {
        return week;
    }

    //TODO 修改这里
    public String getWeekLabel() {

        StringBuilder result = new StringBuilder();
        double weekDouble = (double) week;

        while (weekDouble / 64f >= 1) {
            weekDouble -= 64f;
            result.insert(0, "  星期日");
        }


        while (weekDouble / 32f >= 1) {
            weekDouble -= 32f;
            result.insert(0, "  星期六");
        }


        while (weekDouble / 16f >= 1) {
            weekDouble -= 16f;
            result.insert(0, "  星期五");
        }


        while (weekDouble / 8f >= 1) {
            weekDouble -= 8f;
            result.insert(0, "  星期四");
        }


        while (weekDouble / 4f >= 1) {
            weekDouble -= 4f;
            result.insert(0, "  星期三");
        }


        while (weekDouble / 2f >= 1) {
            weekDouble -= 2f;
            result.insert(0, "  星期二");
        }


        while (weekDouble / 1f >= 1) {
            weekDouble -= 1f;
            result.insert(0, "  星期一");
        }

        if (week == 127) {
            result = new StringBuilder();
            result.insert(0, "  每天");
        }

        if (week == 31) {
            result = new StringBuilder();
            result.insert(0, "  工作日");
        }

        return result.toString();
    }

    @Bindable
    public int getType() {
        return type;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public int getId() {
        return id;
    }

    public String date() {
        return (type == TYPE_QUICK ? "简捷复诊" : "详细就诊") + ':' + getWeekLabel();
    }

    public String disturbDate() {
        return "免打扰周期:" + getWeekLabel();
    }

    public String time() {
        return from.substring(0, 5) + " - " + to.substring(0, 5);
    }

    public String disturbTime() {
        return from.substring(0, 5) + ' ' + '-' + ' ' + to.substring(0, 5);
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_time;
    }

    @Override
    public String toString() {
        return "Time{" +
                "doctorId=" + doctorId +
                ", week='" + week + '\'' +
                ", type='" + type + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", id=" + id +
                '}';
    }

    public Time() {
    }

    public void setReserva(int reserva) {
        this.reserva = reserva;
    }

    public void setOptional(int optional) {
        this.optional = optional;
    }

    public int getReserva() {
        return reserva;
    }

    public int getOptional() {
        return optional;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.doctorId);
        dest.writeInt(this.week);
        dest.writeInt(this.type);
        dest.writeString(this.from);
        dest.writeString(this.to);
        dest.writeString(this.updatedAt);
        dest.writeString(this.createdAt);
        dest.writeInt(this.id);
        dest.writeInt(this.reserva);
        dest.writeInt(this.optional);
    }

    protected Time(Parcel in) {
        this.doctorId = in.readInt();
        this.week = in.readInt();
        //noinspection WrongConstant
        this.type = in.readInt();
        this.from = in.readString();
        this.to = in.readString();
        this.updatedAt = in.readString();
        this.createdAt = in.readString();
        this.id = in.readInt();
        this.reserva = in.readInt();
        this.optional = in.readInt();
    }

    public static final Creator<Time> CREATOR = new Creator<Time>() {
        public Time createFromParcel(Parcel source) {
            return new Time(source);
        }

        public Time[] newArray(int size) {
            return new Time[size];
        }
    };

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TYPE_UNDEFINE, TYPE_DETAIL, TYPE_QUICK, TYPE_BREAK})
    public @interface Type {
    }
}
