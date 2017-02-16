package com.doctor.sun.ui.activity.doctor;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.View;
import android.widget.FrameLayout;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ItemIncomeOverviewBinding;
import com.doctor.sun.entity.Description;
import com.doctor.sun.event.ConfigChangedEvent;
import com.doctor.sun.module.wraper.IncomeModuleWrapper;
import com.doctor.sun.vm.BaseItem;
import com.doctor.sun.vm.BaseMenu;
import com.squareup.otto.Subscribe;

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
//                        Intent intent = MainActivity.makeIntent(v.getContext());
//                        v.getContext().startActivity(intent);
                    }
                };
            }
        };
        baseMenu.setItemId("month_revenue");
        baseMenu.setPosition(3);
        baseMenu.setBackgroundColor(R.color.white);
        return baseMenu;
    }

    private BaseMenu weekRevenue() {
        BaseMenu baseMenu = new BaseMenu(R.layout.item_income_menu, 0, "近七日账户收入明细") {
            @Override
            public View.OnClickListener itemClick() {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = MainActivity.makeIntent(v.getContext());
//                        v.getContext().startActivity(intent);
                    }
                };
            }
        };
        baseMenu.setItemId("week_revenue");
        baseMenu.setPosition(4);
        baseMenu.setBackgroundColor(R.color.white);
        return baseMenu;
    }

    private BaseMenu dayRevenue() {
        BaseMenu baseMenu = new BaseMenu(R.layout.item_income_menu, 0, "当天账户收入明细") {
            @Override
            public View.OnClickListener itemClick() {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = MainActivity.makeIntent(v.getContext());
//                        v.getContext().startActivity(intent);
                    }
                };
            }
        };
        baseMenu.setItemId("day_revenue");
        baseMenu.setPosition(5);
        baseMenu.setBackgroundColor(R.color.white);
        return baseMenu;
    }
}
