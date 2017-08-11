package com.doctor.sun.model;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.BillDetail;
import com.doctor.sun.entity.Consult;
import com.doctor.sun.entity.constans.AppointmentType;
import com.doctor.sun.module.wraper.IncomeModuleWrapper;
import com.doctor.sun.ui.activity.BundlesTabActivity;
import com.doctor.sun.ui.activity.SingleFragmentActivity;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.fragment.BillRulesFragment;
import com.doctor.sun.ui.fragment.HistoryBillFragment;
import com.doctor.sun.ui.fragment.PrescriptionsFragment;
import com.doctor.sun.ui.fragment.SubsidyDetailFragment;
import com.doctor.sun.vm.BaseMenu;
import com.doctor.sun.vm.BillMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rick on 16/2/2017.
 */
public class BillModel {
    Consult detailData = new Consult();
    Consult simpleData = new Consult();
    Consult surfaceData = new Consult();

    public List<SortedItem> parseData(final String time) {
        List<SortedItem> result = new ArrayList<>();
        BillDetail billDetail = IncomeModuleWrapper.getInstance().getBillDetail(time);
        if (null != billDetail.consult) {
            for (int i = 0; i < billDetail.consult.size(); i++) {
                if (billDetail.consult.get(i).type == AppointmentType.PREMIUM) {
                    detailData = billDetail.consult.get(i);
                }
                if (billDetail.consult.get(i).type == AppointmentType.STANDARD) {
                    simpleData = billDetail.consult.get(i);
                }
                if (billDetail.consult.get(i).type == AppointmentType.FACE) {
                    surfaceData = billDetail.consult.get(i);
                }
            }
        }
//        AppointmentType.PREMIUM
        BillMenu premium = new BillMenu(R.layout.item_bill_menu, 0, "VIP网诊") {
            @Override
            public View.OnClickListener itemClick() {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle args = HistoryBillFragment.getArgs(time, AppointmentType.PREMIUM);
                        String title = String.format("VIP网诊(%s)", detailData.consult_num);
                        Intent intent = SingleFragmentActivity.intentFor(v.getContext(), title, args);
                        v.getContext().startActivity(intent);
                    }
                };
            }
        };
        premium.setSubTitle("次数:" + detailData.consult_num + "次");
        premium.setTotalRevenue("收益总额:" + detailData.consult_fee + "元");
        premium.setTitleColor(R.color.dark_36);
        premium.setBackgroundColor(R.color.white);
        premium.setPosition(0);
        premium.setItemId("PREMIUM");

        result.add(premium);
        ModelUtils.insertDividerNoMargin(result);

        final BillMenu surface = new BillMenu(R.layout.item_bill_menu, 0, "诊所面诊") {
            @Override
            public View.OnClickListener itemClick() {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle args = HistoryBillFragment.getArgs(time, AppointmentType.FACE);
                        String title = String.format("诊所面诊(%s)", surfaceData.consult_num);
                        Intent intent = SingleFragmentActivity.intentFor(v.getContext(), title, args);
                        v.getContext().startActivity(intent);

                    }
                };
            }
        };

        surface.setSubTitle("次数:" + surfaceData.consult_num + "次");
        surface.setTotalRevenue("收益总额:" + surfaceData.consult_fee + "元");
        surface.setTitleColor(R.color.dark_36);
        surface.setBackgroundColor(R.color.white);
        surface.setPosition(1);
        surface.setItemId("SURFACE");
        result.add(surface);
        ModelUtils.insertDividerNoMargin(result);
//        AppointmentType.STANDARD
        BillMenu standard = new BillMenu(R.layout.item_bill_menu, 0, "简易复诊") {
            @Override
            public View.OnClickListener itemClick() {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle args = HistoryBillFragment.getArgs(time, AppointmentType.STANDARD);
                        String title = String.format("简易复诊(%s)", simpleData.consult_num);
                        Intent intent = SingleFragmentActivity.intentFor(v.getContext(), title, args);
                        v.getContext().startActivity(intent);

                    }
                };
            }
        };
        standard.setSubTitle("次数:" + simpleData.consult_num + "次");
        standard.setTotalRevenue("收益总额:" + simpleData.consult_fee + "元");
        standard.setTitleColor(R.color.dark_36);
        standard.setBackgroundColor(R.color.white);
        standard.setPosition(1);
        standard.setItemId("STANDARD");
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
                        Intent intent = BundlesTabActivity.intentFor(v.getContext(), tab2, tab1);
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

        BaseMenu baseItem = new BaseMenu(R.layout.item_total_bill, 0, "") {
            @Override
            public View.OnClickListener itemClick() {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle args = BillRulesFragment.getArgs(time);
                        Intent intent = SingleFragmentActivity.intentFor(v.getContext(), "账单规则", args);
                        v.getContext().startActivity(intent);
                    }
                };
            }
        };
        baseItem.setItemId("TOTAL_FEE");
        baseItem.setTitle(billDetail.total_fee);
        baseItem.setPosition(3);

        result.add(baseItem);

        ModelUtils.insertSpace(result, R.layout.space_285dp_grey);
        return result;
    }

}
