package com.doctor.sun.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.Patient;
import com.doctor.sun.event.ActivityResultEvent;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.model.EditPatientInfoModel;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.widget.TwoChoiceDialog;
import com.doctor.sun.vo.BaseItem;
import com.doctor.sun.vo.ItemPickImage;
import com.squareup.otto.Subscribe;

import java.util.List;

/**
 * Created by kb on 16-9-26.
 */

public class EditPatientInfoFragment extends SortedListFragment {
    public static final String TAG = EditPatientInfoFragment.class.getSimpleName();

    private EditPatientInfoModel model;
    private Patient patient;
    private boolean isEditMode = false;

    public static Bundle getArgs(Patient data) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FRAGMENT_NAME, TAG);
        bundle.putParcelable(Constants.DATA, data);

        return bundle;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new EditPatientInfoModel();
        patient = getArguments().getParcelable(Constants.DATA);
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

        List<SortedItem> items = model.parseData(patient);
        binding.swipeRefresh.setRefreshing(false);
        getAdapter().insertAll(items);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (!isEditMode) {
            inflater.inflate(R.menu.menu_edit, menu);
        } else {
            inflater.inflate(R.menu.menu_finish, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_edit:
                TwoChoiceDialog.show(getContext(), " 您好，昭阳医生不可以随便更改用户资料，所有用户资料的申请需要经过后台审核", "取消", "确定", new TwoChoiceDialog.Options() {
                    @Override
                    public void onApplyClick(MaterialDialog dialog) {
                        dialog.dismiss();

                        enableItems();
                        isEditMode = true;
                        getActivity().invalidateOptionsMenu();
                        Toast.makeText(getContext(), "编辑完成后请点击右上角保存", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelClick(MaterialDialog dialog) {
                        dialog.dismiss();
                    }
                });
                break;
            case R.id.action_finish:
                save();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            ItemPickImage.handleRequest(getContext(), getAdapter(), data, requestCode);
        }
    }

    @Subscribe
    public void onActivityResultEvent(ActivityResultEvent event) {
        onActivityResult(event.getRequestCode(), event.getResultCode(), event.getData());
    }

    private void save() {

        model.savePatientInfo(getAdapter(), new SimpleCallback<Patient>() {
            @Override
            protected void handleResponse(Patient response) {
                Toast.makeText(getContext(), "保存成功,请耐心等待资料审核", Toast.LENGTH_SHORT).show();

                getActivity().finish();
            }
        });
    }

    private void enableItems() {

        for (int i = 0; i < getAdapter().size(); i++) {
            ((BaseItem) getAdapter().get(i)).setEnabled(true);
        }
    }
}
