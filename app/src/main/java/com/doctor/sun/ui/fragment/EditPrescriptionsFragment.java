package com.doctor.sun.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.Prescription;
import com.doctor.sun.model.EditPrescriptionModel;
import com.doctor.sun.ui.activity.SingleFragmentActivity;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.vo.BaseItem;

import java.util.List;

/**
 * Created by rick on 28/7/2016.
 */

public class EditPrescriptionsFragment extends SortedListFragment {
    public static final String TAG = EditPrescriptionsFragment.class.getSimpleName();

    private EditPrescriptionModel model;
    private Prescription data;

    public static Intent intentFor(Context context, Prescription data) {
        Intent i = new Intent(context, SingleFragmentActivity.class);
        i.putExtra(Constants.FRAGMENT_TITLE, "个人信息");
        i.putExtra(Constants.FRAGMENT_CONTENT_BUNDLE, getArgs(data));
        return i;
    }

    public static Bundle getArgs(Prescription data) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.DATA, data);
        bundle.putString(Constants.FRAGMENT_NAME, TAG);
        return bundle;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new EditPrescriptionModel();
        data = getArguments().getParcelable(Constants.DATA);
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
        List<SortedItem> sortedItems = model.parseData(data);
        binding.swipeRefresh.setRefreshing(false);
        for (int i = 0; i < sortedItems.size(); i++) {
            BaseItem item = (BaseItem) sortedItems.get(i);
            item.setPosition(i);
            getAdapter().insert(item);
        }
    }

    public void save() {
        Toast.makeText(getContext(), "save answer", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_finish, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_finish: {
                save();
            }
        }
        return super.onOptionsItemSelected(item);
    }

}
