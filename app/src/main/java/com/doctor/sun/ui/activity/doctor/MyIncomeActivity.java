package com.doctor.sun.ui.activity.doctor;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ItemIncomeOverviewBinding;
import com.doctor.sun.entity.Description;
import com.doctor.sun.event.ConfigChangedEvent;
import com.doctor.sun.module.wraper.IncomeModuleWrapper;
import com.doctor.sun.ui.activity.SingleFragmentActivity;
import com.doctor.sun.ui.fragment.BillFragment;
import com.doctor.sun.vm.BaseItem;
import com.doctor.sun.vm.BaseMenu;
import com.squareup.otto.Subscribe;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by rick on 15/2/2017.
 */
public class MyIncomeActivity extends CoordinatorActivity {

    IncomeModuleWrapper api = IncomeModuleWrapper.getInstance();
    private ItemIncomeOverviewBinding headerBinding;


    @Override
    protected void initHeader(FrameLayout flyHeader) {
        headerBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.item_income_overview, flyHeader, true);
        setIncomeOverview();
        api.refreshIncomeOverView();
    }

    @Subscribe
    public void onEventMainThread(ConfigChangedEvent e) {
        String key = e.getKey();
        switch (key) {
            case Constants.INCOME_OVERVIEW: {
                setIncomeOverview();
            }
        }
    }

    public void setIncomeOverview() {
        if (headerBinding != null) {
            headerBinding.setData(api.getIncomeOverView());
        }
        if (getAdapter() != null) {
            getAdapter().insert(getTotalFee());
        }
    }

    @Override
    protected void loadMore() {
        insertSpace(0);
        getAdapter().insert(getTotalFee());
        insertSpace(2);

        getAdapter().insert(monthRevenue());
        getAdapter().insert(weekRevenue());
        getAdapter().insert(dayRevenue());
        insertLargeSpace(6);
            getAdapter().insert(getOderReminder());
    }

    private void insertSpace(int p) {
        BaseItem item = new BaseItem(R.layout.divider_8dp);
        item.setItemId("SPACE" + p);
        item.setBackgroundColor(R.color.gray_eb);
        item.setPosition(p);
        getAdapter().insert(item);
    }

    private Description getTotalFee() {
        Description totalFee = new Description(R.layout.item_blue_description, api.getIncomeOverView().total_fee);
        totalFee.setSubContent("元");
        totalFee.setItemId("total_fee");
        totalFee.setTitle("历史累计总额");
        totalFee.setTitleColor(R.color.colorPrimaryDark);
        totalFee.setBackgroundColor(R.color.white);
        totalFee.setPosition(1);
        return totalFee;
    }

    private BaseMenu monthRevenue() {
        BaseMenu baseMenu = new BaseMenu(R.layout.item_income_menu, 0, "月度账户收入明细") {
            @Override
            public View.OnClickListener itemClick() {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar instance = Calendar.getInstance();
                        DateFormat format = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
                        String currentTime = format.format(instance.getTime());
                        Bundle args = BillFragment.getArgs("");
                        Intent intent = SingleFragmentActivity.intentFor(v.getContext(), "当月收入明细", args);
                        v.getContext().startActivity(intent);
                    }
                };
            }
        };
        baseMenu.setItemId("month_revenue");
        baseMenu.setPosition(3);
        baseMenu.setBackgroundColor(R.color.white);
        baseMenu.setTitleColor(R.color.dark_36);
        return baseMenu;
    }

    private BaseMenu weekRevenue() {
        BaseMenu baseMenu = new BaseMenu(R.layout.item_income_menu, 0, "近七日账户收入明细") {
            @Override
            public View.OnClickListener itemClick() {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle args = BillFragment.getArgs("recent_seven_days");
                        Intent intent = SingleFragmentActivity.intentFor(v.getContext(), "近七日账单", args);
                        v.getContext().startActivity(intent);
                    }
                };
            }
        };
        baseMenu.setItemId("week_revenue");
        baseMenu.setPosition(4);
        baseMenu.setBackgroundColor(R.color.white);
        baseMenu.setTitleColor(R.color.dark_36);
        return baseMenu;
    }

    private BaseMenu dayRevenue() {
        BaseMenu baseMenu = new BaseMenu(R.layout.item_income_menu, 0, "当天账户收入明细") {
            @Override
            public View.OnClickListener itemClick() {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle args = BillFragment.getArgs("today");
                        Intent intent = SingleFragmentActivity.intentFor(v.getContext(), "当天账单", args);
                        v.getContext().startActivity(intent);
                    }
                };
            }
        };
        baseMenu.setItemId("day_revenue");
        baseMenu.setPosition(5);
        baseMenu.setBackgroundColor(R.color.white);
        baseMenu.setTitleColor(R.color.dark_36);
        return baseMenu;
    }

    private void insertLargeSpace(int p) {
        BaseItem item = new BaseItem(R.layout.space_150dp);
        item.setItemId("SPACE" + p);
        item.setBackgroundColor(R.color.gray_eb);
        item.setPosition(p);
        getAdapter().insert(item);
    }

    private Description getOderReminder() {
        Description getReminder = new Description(R.layout.item_order_reminder, api.getIncomeOverView().explain);
        getReminder.setItemId("explain");
        getReminder.setSpan(12);
        getReminder.setPosition(7);
        return getReminder;
    }
}
