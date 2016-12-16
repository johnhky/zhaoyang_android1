package com.doctor.sun.ui.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.doctor.auto.Factory;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ItemBottomButtonBinding;
import com.doctor.sun.entity.MedicalRecord;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.model.EditRecordModel;
import com.doctor.sun.ui.activity.patient.HistoryActivity;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.vm.BaseItem;

import java.util.List;

/**
 * Created by kb on 16-9-26.
 */

@Factory(type = BaseFragment.class, id = "EditRecordFragment")
public class EditRecordFragment extends SortedListFragment {

    public static final String TAG = EditRecordFragment.class.getSimpleName();

    private EditRecordModel model;
    private MedicalRecord data;

    private boolean isEditMode = false;

    public static Bundle getArgs(MedicalRecord data) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FRAGMENT_NAME, TAG);
        bundle.putParcelable(Constants.DATA, data);

        return bundle;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new EditRecordModel();
        data = getArguments().getParcelable(Constants.DATA);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 添加一个位于屏幕底部的按钮
        ItemBottomButtonBinding inflate = DataBindingUtil.inflate(getLayoutInflater(savedInstanceState), R.layout.item_bottom_button, binding.flRoot, false);
        inflate.setData(data);
        binding.flRoot.addView(inflate.getRoot());

        disableRefresh();
        setHasOptionsMenu(true);
        loadMore();
    }

    @Override
    protected void loadMore() {
        super.loadMore();

        List<SortedItem> items = model.parseItems(data);
        binding.swipeRefresh.setRefreshing(false);
        getAdapter().insertAll(items);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (!isEditMode) {
            inflater.inflate(R.menu.menu_edit, menu);
        } else {
            inflater.inflate(R.menu.menu_save, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                isEditMode = true;
                getActivity().invalidateOptionsMenu();
                enableItems();
                Toast.makeText(getContext(), "编辑完成后请点击右上角保存", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_save:
                saveRecord();
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveRecord() {
        model.saveRecord(getAdapter(), new SimpleCallback<MedicalRecord>() {
            @Override
            protected void handleResponse(MedicalRecord ignored) {
                if (ignored!=null){
                    Toast.makeText(getContext(), "成功修改病历", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(), "成功申请修改病历,请耐心等待审核", Toast.LENGTH_SHORT).show();
                }

                getActivity().finish();
            }
        });
    }

    private void enableItems() {
        if (isEditMode) {
            for (int i = 0; i < getAdapter().size(); i++) {
                ((BaseItem) getAdapter().get(i)).setEnabled(true);
            }
        }
    }
}
