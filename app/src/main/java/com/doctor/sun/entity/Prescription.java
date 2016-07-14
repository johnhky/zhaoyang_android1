package com.doctor.sun.entity;


import android.content.Intent;
import android.databinding.BaseObservable;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.ui.activity.ViewPrescriptionActivity;
import com.doctor.sun.ui.activity.doctor.EditPrescriptionActivity;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.doctor.sun.ui.fragment.DiagnosisFragment;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by rick on 29/12/2015.
 * 用药信息
 */
public class Prescription extends BaseObservable implements Parcelable, LayoutId {


    /**
     * mediaclName : 1米没信号
     * productName : 还整个
     * interval : 4
     * numbers : [{"早":"1"},{"午":"1"},{"晚":"1"},{"睡前":"1"}]
     * unit : A
     * remark : mins尼米兹
     */

    private static final List<String> keys = new ArrayList<>();

    static {
        keys.add("早");
        keys.add("午");
        keys.add("晚");
        keys.add("睡前");
    }


    @JsonProperty("mediaclName")
    private String mediaclName;
    @JsonProperty("productName")
    private String productName;
    @JsonProperty("interval")
    private String interval;
    @JsonProperty("numbers")
    private List<HashMap<String, String>> numbers;
    @JsonProperty("unit")
    private String unit;
    @JsonProperty("remark")
    private String remark;
    @JsonIgnore
    private boolean isVisible = true;


    public Prescription() {
    }

    public Prescription copy() {
        Prescription result = new Prescription();
        result.mediaclName = mediaclName;
        result.productName = productName;
        result.interval = interval;
        result.numbers = numbers;
        result.unit = unit;
        result.remark = remark;
        result.isVisible = isVisible;
        return result;
    }

    @JsonIgnore
    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public String getMediaclName() {
        return mediaclName;
    }

    public void setMediaclName(String mediaclName) {
        this.mediaclName = mediaclName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public View.OnClickListener modify() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = EditPrescriptionActivity.makeIntent(v.getContext(), Prescription.this);
                Messenger messenger = new Messenger(new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        switch (msg.what) {
                            case DiagnosisFragment.EDIT_PRESCRITPION: {
                                Prescription parcelable = msg.getData().getParcelable(Constants.DATA);
                                mediaclName = parcelable.mediaclName;
                                productName = parcelable.productName;
                                interval = parcelable.interval;
                                numbers = parcelable.numbers;
                                unit = parcelable.unit;
                                remark = parcelable.remark;
                                isVisible = parcelable.isVisible;
                                notifyChange();
                            }
                        }
                        return false;
                    }
                }));
                intent.putExtra(Constants.HANDLER, messenger);
                v.getContext().startActivity(intent);
            }
        };
    }

    public View.OnClickListener viewDetail() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ViewPrescriptionActivity.makeIntent(v.getContext(), Prescription.this);
                v.getContext().startActivity(intent);
            }
        };
    }

    public List<HashMap<String, String>> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<HashMap<String, String>> numbers) {
        this.numbers = numbers;
    }

    @JsonIgnore
    public String getLabel() {
        StringBuilder builder = new StringBuilder();
        builder.append(mediaclName);
        if (productName != null && !productName.isEmpty()) {
            builder.append("(").append(productName).append("),");
        } else {
            builder.append(",");
        }
        if (!interval.equals("每天")) {
            builder.append(interval).append(":");
        }

        for (int i = 0; i < numbers.size(); i++) {
            String amount = numbers.get(i).get(keys.get(i));
            if (null != amount && !amount.equals("")) {
                builder.append(keys.get(i)).append(amount).append(unit);
                builder.append(",");
            }
        }

        if (remark != null && !remark.isEmpty()) {
            builder.append(remark);
        } else {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

    @JsonIgnore
    public String getName() {
        String s = "<font color='#898989'>药名: </font>" + mediaclName;
        if (productName != null && !productName.equals("")) {
            s += "(" + productName + ")";
        } else {
            s += "";
        }
        return s;
    }

    @JsonIgnore
    public String getIntervalWithLabel() {
        String builder = "<font color='#898989'>间隔:   </font>" +
                interval;
        return builder;
    }

    @JsonIgnore
    public String getAmount() {
        StringBuilder builder = new StringBuilder();
        builder.append("<font color='#898989'>数量:   </font>");
        for (int i = 0; i < numbers.size(); i++) {
            String amount = numbers.get(i).get(keys.get(i));
            if (null != amount && !amount.equals("")) {
                builder.append(keys.get(i)).append(amount).append(unit).append(",");
            }
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    @JsonIgnore
    public String getRemarkLabel() {
        StringBuilder builder = new StringBuilder();
        builder.append("<font color='#898989'>备注:   </font>");
        builder.append(remark);
        return builder.toString();
    }

    @JsonIgnore
    public String getNumberLabel() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < numbers.size(); i++) {
            String amount = numbers.get(i).get(keys.get(i));
            if (null != amount && !amount.equals("")) {
                builder.append(keys.get(i)).append(amount).append(unit).append(",");
            }
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        return "Prescription{" +
                "mediaclName='" + mediaclName + '\'' +
                ", productName='" + productName + '\'' +
                ", interval='" + interval + '\'' +
                ", numbers=" + numbers +
                ", unit='" + unit + '\'' +
                ", remark='" + remark + '\'' +
                ", isVisible=" + isVisible +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Prescription that = (Prescription) o;

        if (isVisible != that.isVisible) return false;
        if (mediaclName != null ? !mediaclName.equals(that.mediaclName) : that.mediaclName != null)
            return false;
        if (productName != null ? !productName.equals(that.productName) : that.productName != null)
            return false;
        if (interval != null ? !interval.equals(that.interval) : that.interval != null)
            return false;
        if (numbers != null ? !numbers.equals(that.numbers) : that.numbers != null) return false;
        if (unit != null ? !unit.equals(that.unit) : that.unit != null) return false;
        return remark != null ? remark.equals(that.remark) : that.remark == null;

    }

    @Override
    public int hashCode() {
        int result = mediaclName != null ? mediaclName.hashCode() : 0;
        result = 31 * result + (productName != null ? productName.hashCode() : 0);
        result = 31 * result + (interval != null ? interval.hashCode() : 0);
        result = 31 * result + (numbers != null ? numbers.hashCode() : 0);
        result = 31 * result + (unit != null ? unit.hashCode() : 0);
        result = 31 * result + (remark != null ? remark.hashCode() : 0);
        result = 31 * result + (isVisible ? 1 : 0);
        return result;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mediaclName);
        dest.writeString(this.productName);
        dest.writeString(this.interval);
        dest.writeList(this.numbers);
        dest.writeString(this.unit);
        dest.writeString(this.remark);
        dest.writeByte(isVisible ? (byte) 1 : (byte) 0);
    }

    protected Prescription(Parcel in) {
        this.mediaclName = in.readString();
        this.productName = in.readString();
        this.interval = in.readString();
        this.numbers = new ArrayList<HashMap<String, String>>();
        in.readList(this.numbers, List.class.getClassLoader());
        this.unit = in.readString();
        this.remark = in.readString();
        this.isVisible = in.readByte() != 0;
    }

    public static final Creator<Prescription> CREATOR = new Creator<Prescription>() {
        public Prescription createFromParcel(Parcel source) {
            return new Prescription(source);
        }

        public Prescription[] newArray(int size) {
            return new Prescription[size];
        }
    };

    @Override
    @JsonIgnore
    public int getItemLayoutId() {
        return R.layout.item_prescription;
    }

    public interface UrlToLoad {
        String url();
    }

}
