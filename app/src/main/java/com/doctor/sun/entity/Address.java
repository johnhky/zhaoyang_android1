package com.doctor.sun.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.doctor.sun.R;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.doctor.sun.vo.BaseItem;

/**
 * Created by kb on 16-9-16.
 * 寄药地址
 */

public class Address extends BaseItem implements LayoutId, Parcelable {

    private String name;
    private String phone;
    private String address;

    public Address() {

    }

    public String getName() {
        return "收件人：" + name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return "手机号码：" + phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return "收货地址：" + address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_address;
    }

    protected Address(Parcel in) {
        name = in.readString();
        phone = in.readString();
        address = in.readString();
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(address);
    }
}
