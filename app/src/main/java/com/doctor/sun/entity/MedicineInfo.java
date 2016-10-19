package com.doctor.sun.entity;

import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.TextView;

import com.doctor.sun.R;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.doctor.sun.vo.BaseItem;

/**
 * Created by kb on 16-9-16.
 */

public class MedicineInfo extends BaseItem implements LayoutId, Parcelable {

    private String medicine;
    private String orderId;
    private String medicinePrice;

    public MedicineInfo() {
    }

    public String getMedicine() {
        return medicine;
    }

    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }

    public String getMedicinePrice() {
        return medicinePrice;
    }

    public void setMedicinePrice(String medicinePrice) {
        this.medicinePrice = medicinePrice;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_medicine_info;
    }

    protected MedicineInfo(Parcel in) {
        medicine = in.readString();
        orderId = in.readString();
        medicinePrice = in.readString();
    }

    public static final Creator<MedicineInfo> CREATOR = new Creator<MedicineInfo>() {
        @Override
        public MedicineInfo createFromParcel(Parcel in) {
            return new MedicineInfo(in);
        }

        @Override
        public MedicineInfo[] newArray(int size) {
            return new MedicineInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(medicine);
        dest.writeString(orderId);
        dest.writeString(medicinePrice);
    }

    public int setStrikeThru(TextView textView) {
        textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        return R.drawable.bg_transparent;
    }
}
