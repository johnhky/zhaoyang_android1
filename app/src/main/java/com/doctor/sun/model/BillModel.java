package com.doctor.sun.model;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.BillDetail;
import com.doctor.sun.entity.constans.AppointmentType;
import com.doctor.sun.module.wraper.IncomeModuleWrapper;
import com.doctor.sun.ui.activity.BundlesTabActivity;
import com.doctor.sun.ui.activity.SingleFragmentActivity;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.fragment.HistoryBillFragment;
import com.doctor.sun.ui.fragment.PrescriptionsFragment;
import com.doctor.sun.ui.fragment.SubsidyDetailFragment;
import com.doctor.sun.vm.BaseItem;
import com.doctor.sun.vm.BillMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rick on 16/2/2017.
 */
public class BillModel {

    public List<SortedItem> parseData(final String time) {
        List<SortedItem> result = new ArrayList<>();

        final BillDetail billDetail = IncomeModuleWrapper.getInstance().getBillDetail(time);
//        AppointmentType.PREMIUM
        BillMenu premium = new BillMenu(R.layout.item_bill_menu, 0, "专属咨询") {
            @Override
            public View.OnClickListener itemClick() {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle args = HistoryBillFragment.getArgs(time, AppointmentType.PREMIUM);
                        String title = String.format("专属咨询(%s)", billDetail.detail_consult_num);
                        Intent intent = SingleFragmentActivity.intentFor(v.getContext(), title, args);
                        v.getContext().startActivity(intent);
                    }
                };
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
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle args = HistoryBillFragment.getArgs(time, AppointmentType.STANDARD);
                        String title = String.format("闲时咨询(%s)", billDetail.simple_consult_num);
                        Intent intent = SingleFragmentActivity.intentFor(v.getContext(), title, args);
                        v.getContext().startActivity(intent);
                    }
                };
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
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle tab1 = PrescriptionsFragment.getArgs(time);
                        tab1.putString(Constants.FRAGMENT_TITLE, "处方量");
                        Bundle tab2 = SubsidyDetailFragment.getArgs(time);
                        tab2.putString(Constants.FRAGMENT_TITLE, "详情");
                        Intent intent = BundlesTabActivity.intentFor(v.getContext(), tab1, tab2);
                        intent.putExtra(Constants.ACTIVITY_TITLE, "补贴");
                        v.getContext().startActivity(intent);
                    }
                };
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

        BaseItem baseItem = new BaseItem(R.layout.item_total_bill);
        baseItem.setItemId("TOTAL_FEE");
        baseItem.setTitle(billDetail.total_fee);
        baseItem.setPosition(3);

        result.add(baseItem);

        ModelUtils.insertSpace(result, R.layout.space_285dp_grey);

        return result;
    }

}
