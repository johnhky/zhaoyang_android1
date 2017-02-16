package com.doctor.sun.model;

import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.entity.BillDetail;
import com.doctor.sun.module.wraper.IncomeModuleWrapper;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.vm.BillMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rick on 16/2/2017.
 */
public class BillModel {

    public List<SortedItem> parseData(String time) {
        List<SortedItem> result = new ArrayList<>();

        BillDetail billDetail = IncomeModuleWrapper.getInstance().getBillDetail(time);
//        AppointmentType.PREMIUM
        BillMenu premium = new BillMenu(R.layout.item_bill_menu, 0, "专属咨询") {
            @Override
            public View.OnClickListener itemClick() {
                return null;
            }
        };
        premium.setSubTitle("次数:" + billDetail.detail_consult_num + "次");
        premium.setTitleColor(R.color.dark_36);
        premium.setBackgroundColor(R.color.white);
        premium.setPosition(0);
        premium.setItemId("PREMIUM");
        premium.setTotalRevenue("收益总额:" + billDetail.detail_consult_fee + "元");
        result.add(premium);

        ModelUtils.insertDividerNoMargin(result);


//        AppointmentType.STANDARD
        BillMenu standard = new BillMenu(R.layout.item_bill_menu, 0, "闲时咨询") {
            @Override
            public View.OnClickListener itemClick() {
                return null;
            }
        };
        standard.setSubTitle("次数:" + billDetail.simple_consult_num + "次");
        standard.setTitleColor(R.color.dark_36);
        standard.setBackgroundColor(R.color.white);
        standard.setPosition(1);
        standard.setItemId("STANDARD");
        standard.setTotalRevenue("收益总额:" + billDetail.simple_consult_fee + "元");
        result.add(standard);

        ModelUtils.insertDividerNoMargin(result);

        BillMenu subsidy = new BillMenu(R.layout.item_bill_menu, 0, "补贴") {
            @Override
            public View.OnClickListener itemClick() {
                return null;
            }
        };
        subsidy.setSubTitle("");
        subsidy.setTitleColor(R.color.dark_36);
        subsidy.setBackgroundColor(R.color.white);
        subsidy.setPosition(2);
        subsidy.setItemId("SUBSIDY");
        subsidy.setTotalRevenue("收益总额:" + billDetail.total_subsidy + "元");
        result.add(subsidy);

        ModelUtils.insertDividerNoMargin(result);

        return result;
    }

}
