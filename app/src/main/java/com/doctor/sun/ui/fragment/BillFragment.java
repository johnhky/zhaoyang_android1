package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.auto.Factory;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.event.ConfigChangedEvent;
import com.doctor.sun.event.OnTitleChangedEvent;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.model.BillModel;
import com.doctor.sun.module.wraper.IncomeModuleWrapper;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.vm.BaseItem;
import com.squareup.otto.Subscribe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.ganguo.library.core.event.EventHub;
import retrofit2.Call;

/**
 * Created by rick on 16/2/2017.
 */
@Factory(type = BaseFragment.class, id = "BillFragment")
public class BillFragment extends SortedListFragment {
    public static final String TAG = BillFragment.class.getSimpleName();

    private BillModel model;
    private String time;
    public static Bundle getArgs(String time) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FRAGMENT_NAME, TAG);

        bundle.putString(Constants.BILL_TIME, time);

        return bundle;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        time = getTime();
        setHasOptionsMenu(true);
        model = new BillModel();
        String string = getTime();
        IncomeModuleWrapper.getInstance().refreshBillDetail(string);
    }

    public String getTime() {
        return getArguments().getString(Constants.BILL_TIME);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        disableRefresh();
        loadMore();
    }

    @Override
    protected void loadMore() {
        super.loadMore();
        List<SortedItem> sortedItems = model.parseData(time);
        binding.swipeRefresh.setRefreshing(false);
        for (int i = 0; i < sortedItems.size(); i++) {
            BaseItem item = (BaseItem) sortedItems.get(i);
            item.setPosition(i);
            getAdapter().insert(item);
        }
    }

    @Subscribe
    public void onEventMainThread(ConfigChangedEvent event) {
        if (event.getKey().equals(time + Constants.BILL_DETAIL)) {
            loadMore();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_bill_history, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_bill_history:
                IncomeModuleWrapper.getInstance().monthList().enqueue(new SimpleCallback<ArrayList<String>>() {
                    @Override
                    protected void handleBody(ApiDTO<ArrayList<String>> body) {
                        super.handleBody(body);
                        if (body==null){
                            Toast.makeText(getContext(),"没有任何历史账单",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiDTO<ArrayList<String>>> call, Throwable t) {
                        super.onFailure(call, t);
                        if (t.getMessage()==null){
                            Toast.makeText(getContext(),"没有任何历史账单",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    protected void handleResponse(ArrayList<String> response) {
                        MaterialDialog.Builder builder = new MaterialDialog.Builder(getContext());
                        builder.title("历史账单");
                        builder.items(response)
                                .itemsCallback(new MaterialDialog.ListCallback() {
                                    @Override
                                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                        time = String.valueOf(text);
                                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
                                        try {
                                            Date parse = format.parse(time);
                                            Calendar instance = Calendar.getInstance();
                                            instance.setTime(parse);
                                            int month = instance.get(Calendar.MONTH) + 1;
                                            int year = instance.get(Calendar.YEAR);
                                            EventHub.post(new OnTitleChangedEvent(year + "年" + month + "月收入明细"));
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        IncomeModuleWrapper.getInstance().refreshBillDetail(time);
                                    }
                                });

                        builder.build().show();
                    }

                });
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
