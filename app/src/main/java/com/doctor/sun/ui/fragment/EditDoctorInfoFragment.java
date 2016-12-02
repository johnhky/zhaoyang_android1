package com.doctor.sun.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.doctor.auto.Factory;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.dto.IsChanged;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.constans.ReviewStatus;
import com.doctor.sun.event.ActivityResultEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.http.callback.TokenCallback;
import com.doctor.sun.model.EditDoctorInfoModel;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.activity.SingleFragmentActivity;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.util.JacksonUtils;
import com.doctor.sun.vo.ItemAddTag;
import com.doctor.sun.vo.ItemPickImage;
import com.squareup.otto.Subscribe;

import java.util.HashMap;
import java.util.List;

import io.ganguo.library.Config;

/**
 * Created by rick on 28/7/2016.
 */

@Factory(type = BaseFragment.class, id = "EditDoctorInfoFragment")
public class EditDoctorInfoFragment extends SortedListFragment {
    public static final String TAG = EditDoctorInfoFragment.class.getSimpleName();

    private EditDoctorInfoModel model;
    private Doctor data;

    public static Intent intentFor(Context context, Doctor doctor) {
        Intent i = new Intent(context, SingleFragmentActivity.class);
        i.putExtra(Constants.FRAGMENT_TITLE, "个人信息");
        i.putExtra(Constants.FRAGMENT_CONTENT_BUNDLE, getArgs(doctor));
        return i;
    }

    public static Bundle getArgs(Doctor doctor) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.DATA, doctor);
        bundle.putString(Constants.FRAGMENT_NAME, TAG);
        return bundle;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new EditDoctorInfoModel();
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
        getAdapter().insertAll(sortedItems);
        if (data.getReviewStatus().equals(ReviewStatus.STATUS_PENDING)) {
            Snackbar.make(binding.getRoot(), "资料正在审核，请耐心等待", Snackbar.LENGTH_INDEFINITE).show();
        }
    }

    public void save() {
        saveDoctorInfo();
        saveTags();
    }

    public void saveDoctorInfo() {
        model.saveDoctorInfo(getAdapter(), new SimpleCallback<IsChanged>() {
            @Override
            protected void handleResponse(IsChanged response) {
                Toast.makeText(getContext(), "保存成功,请耐心等待资料审核", Toast.LENGTH_SHORT).show();

                ProfileModule api = Api.of(ProfileModule.class);
                api.doctorProfile().enqueue(new ApiCallback<Doctor>() {
                    @Override
                    protected void handleResponse(Doctor response) {
                        Config.putString(Constants.DOCTOR_PROFILE, JacksonUtils.toJson(response));

                        TokenCallback.checkToken(getActivity());
                    }
                });
            }
        });
    }

    public void saveTags() {
        ItemAddTag item = (ItemAddTag) getAdapter().get(ItemAddTag.TAGS_END);
        HashMap<String, Object> tagsContainer = item.toJson(getAdapter());
        ProfileModule api = Api.of(ProfileModule.class);
        api.tags(JacksonUtils.toJson(tagsContainer.get("tags"))).enqueue(new SimpleCallback<String>() {
            @Override
            protected void handleResponse(String response) {

            }
        });
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

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            ItemPickImage.handleRequest(getActivity(), getAdapter(), data, requestCode);
        }
    }

    @Subscribe
    public void onActivityResultEvent(ActivityResultEvent event) {
        onActivityResult(event.getRequestCode(), event.getResultCode(), event.getData());
    }

    //
//    @Override
//    public void onRefresh() {
//        getBinding().swipeRefresh.setRefreshing(false);
//    }
//

}
