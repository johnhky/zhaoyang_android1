package com.doctor.sun.entity;


import android.content.Context;
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
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.ui.fragment.DiagnosisFragment;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rick on 29/12/2015.
 * 用药信息
 */
public class Prescription extends BaseObservable implements Parcelable, LayoutId, SortedItem {


    /**
     * drugName : 1米没信号
     * scientificName : 还整个
     * interval : 4
     * numbers : [{"早":"1"},{"午":"1"},{"晚":"1"},{"睡前":"1"}]
     * unit : A
     * remark : mins尼米兹
     */

    private static final List<String> keys = new ArrayList<>();
    private static final List<String> fieldKeys = new ArrayList<>();

    static {
        keys.add("早");
        keys.add("午");
        keys.add("晚");
        keys.add("睡前");

        fieldKeys.add("morning");
        fieldKeys.add("noon");
        fieldKeys.add("night");
        fieldKeys.add("before_sleep");
    }

    public long position;
    public String itemId;

    @JsonProperty("mediaclName")
    private String drugName;
    @JsonProperty("productName")
    private String scientificName;
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
        result.drugName = drugName;
        result.scientificName = scientificName;
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

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
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

    public void modify(Context context) {
        Intent intent = EditPrescriptionActivity.makeIntent(context, Prescription.this);
        Messenger messenger = new Messenger(new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case DiagnosisFragment.EDIT_PRESCRITPION: {
                        Prescription parcelable = msg.getData().getParcelable(Constants.DATA);
                        if (parcelable == null) {
                            return false;
                        }
                        drugName = parcelable.drugName;
                        scientificName = parcelable.scientificName;
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
        context.startActivity(intent);
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
        builder.append(drugName);
        if (scientificName != null && !scientificName.isEmpty()) {
            builder.append("(").append(scientificName).append("),");
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
        String s = "<font color='#898989'>药名: </font>" + drugName;
        if (scientificName != null && !scientificName.equals("")) {
            s += "(" + scientificName + ")";
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
        if (remark != null && !remark.equals("")) {
            builder.append("<font color='#898989'>备注:   </font>");
            builder.append(remark);
        }
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
                "drugName='" + drugName + '\'' +
                ", scientificName='" + scientificName + '\'' +
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
        if (drugName != null ? !drugName.equals(that.drugName) : that.drugName != null)
            return false;
        if (scientificName != null ? !scientificName.equals(that.scientificName) : that.scientificName != null)
            return false;
        if (interval != null ? !interval.equals(that.interval) : that.interval != null)
            return false;
        if (numbers != null ? !numbers.equals(that.numbers) : that.numbers != null) return false;
        if (unit != null ? !unit.equals(that.unit) : that.unit != null) return false;
        return remark != null ? remark.equals(that.remark) : that.remark == null;

    }

    @Override
    public int hashCode() {
        int result = drugName != null ? drugName.hashCode() : 0;
        result = 31 * result + (scientificName != null ? scientificName.hashCode() : 0);
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
        dest.writeString(this.drugName);
        dest.writeString(this.scientificName);
        dest.writeString(this.interval);
        dest.writeList(this.numbers);
        dest.writeString(this.unit);
        dest.writeString(this.remark);
        dest.writeByte(isVisible ? (byte) 1 : (byte) 0);
    }

    protected Prescription(Parcel in) {
        this.drugName = in.readString();
        this.scientificName = in.readString();
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

    @Override
    public int getLayoutId() {
        return R.layout.item_prescription3;
    }

    @Override
    public long getCreated() {
        return -position;
    }

    @Override
    public String getKey() {
        return itemId;
    }

    @Override
    public float getSpan() {
        return 12;
    }

    public interface UrlToLoad {
        String url();

    }

    public void fromHashMap(Map<String, String> map) {
        drugName = map.get("drug_name");
        scientificName = map.get("scientific_name");
        interval = map.get("frequency");
        unit = map.get("drug_unit");
        remark = map.get("remark");
//        [{"早":"1"},{"午":"1"},{"晚":"1"},{"睡前":"1"}]
        numbers = new ArrayList<>();
        HashMap<String, String> morning = new HashMap<>();
        morning.put("早", map.get("morning"));
        numbers.add(morning);
        HashMap<String, String> afternoon = new HashMap<>();
        afternoon.put("午", map.get("noon"));
        numbers.add(afternoon);
        HashMap<String, String> evening = new HashMap<>();
        evening.put("晚", map.get("night"));
        numbers.add(evening);
        HashMap<String, String> night = new HashMap<>();
        night.put("睡前", map.get("before_sleep"));
        numbers.add(night);
    }

    public HashMap<String, String> toHashMap() {
        HashMap<String, String> result = new HashMap<String, String>();

        result.put("drug_name", drugName);
        result.put("scientific_name", scientificName);
        result.put("frequency", interval);
        result.put("drug_unit", unit);
        result.put("remark", remark);

        for (int i = 0; i < numbers.size(); i++) {
            String amount = numbers.get(i).get(keys.get(i));
            if (null != amount && !amount.equals("")) {
                result.put(fieldKeys.get(i), amount);
            } else {
                result.put(fieldKeys.get(i), "0");
            }
        }

        return result;
    }

    public HashMap<String, Object> toJson(SortedListAdapter adapter) {
        return null;
    }
}
