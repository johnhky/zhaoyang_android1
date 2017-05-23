package com.doctor.sun.model;

import android.util.Log;

import com.doctor.sun.R;
import com.doctor.sun.entity.Address;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.vm.BaseItem;
import com.doctor.sun.vm.ClickMenu;
import com.doctor.sun.vm.ItemTextInput2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heky on 17/4/27.
 */

public class UploadAddressModel {

    public UploadAddressModel(){

    }

    public List<SortedItem> parseItem(Address data){
        List<SortedItem> result = new ArrayList<>();
        ItemTextInput2 receiver = new ItemTextInput2(R.layout.item_text_input2,"收件人    :","点击输入");
        receiver.setItemId("to");
        receiver.setResultNotEmpty();
        receiver.setPosition(result.size());
        result.add(receiver);
        BaseItem item = new BaseItem(R.layout.divider_1px);
        if (data.getId()!=0){
            item.setItemId("shippingAddressId");
            item.setResult(data.getId()+"");
            result.add(item);
        }
        ModelUtils.insertDividerMarginLR(result);

        ItemTextInput2 phone = new ItemTextInput2(R.layout.item_text_input2,"联系电话:","点击输入");
        phone.setItemId("phone");
        phone.setResultNotEmpty();
        phone.setPosition(result.size());
        result.add(phone);

        ModelUtils.insertDividerMarginLR(result);

        ItemTextInput2 city = new ItemTextInput2(R.layout.item_choosecity,"所在地区:","点击选择");
        city.setPosition(result.size());
        city.setItemId("province");
        city.setResultNotEmpty();
        result.add(city);

        ModelUtils.insertDividerMarginLR(result);

        ItemTextInput2 address = new ItemTextInput2(R.layout.item_address_2,"详细地址","请输入街道、门牌号等");
        address.setPosition(result.size());
        address.setResultNotEmpty();
        address.setItemId("address");
        result.add(address);

        ModelUtils.insertDividerMarginLR(result);

        final ItemTextInput2 remark = new ItemTextInput2(R.layout.item_address_2, "备注", "点击输入");
        remark.setItemId("remark");
        remark.setPosition(result.size());
        result.add(remark);

        ModelUtils.insertDividerMarginLR(result);

        final ClickMenu mock = new ClickMenu(R.layout.item_mockaddress, 0, "默认地址", null);
        mock.setPosition(result.size());
        mock.setEnabled(false);
        mock.setItemId("defaults");
        result.add(mock);
        if (null!=data){
            receiver.setResult(data.getTo());
            phone.setResult(data.getPhone());
            if (data.getProvince()!=null&&data.getCity()!=null&&data.getArea()!=null){
                city.setProvince(data.getProvince()+"-"+data.getCity()+"-"+data.getArea());
            }
            city.setResult(data.getProvince());
            city.setCity(data.getCity());
            city.setArea(data.getArea());
            address.setResult(data.getAddress());
            remark.setResult(data.getRemark());
            if (data.getDefaults()!=null){
                if (data.getDefaults().equals("1")){
                    mock.setEnabled(true);
                }else{
                    mock.setEnabled(false);
                }
            }
        }
        return result;
    }
}
